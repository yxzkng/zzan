package likelion.demo.reservation.service;

import likelion.demo.global.exception.ConflictException;
import likelion.demo.global.exception.NotFoundException;
import likelion.demo.member.entity.Member;
import likelion.demo.member.repository.MemberRepository;
import likelion.demo.reservation.dto.MyReservationListResponse;
import likelion.demo.reservation.dto.ReservationRequest;
import likelion.demo.reservation.dto.ReservationResponse;
import likelion.demo.reservation.entity.Reservation;
import likelion.demo.reservation.entity.ReservationStatus;
import likelion.demo.reservation.repository.ReservationRepository;
import likelion.demo.store.entity.Store;
import likelion.demo.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final StoreRepository storeRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public ReservationResponse createReservation(Long memberId, ReservationRequest request) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException("회원을 찾을 수 없습니다."));

        Store store = storeRepository.findById(request.getStoreId())
                .orElseThrow(() -> new NotFoundException("해당 술집을 찾을 수 없습니다."));

        // 예약은 하루 전까지만 가능
        if (!request.getDate().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("예약은 하루 전까지만 가능합니다.");
        }

        // 시간 구간 유효성 검사
        int startMin = toMinutes(request.getStartTime());
        int endMin = toMinutes(request.getEndTime());
        if (startMin >= endMin) {
            throw new IllegalArgumentException("시작 시간은 끝 시간보다 앞서야 합니다.");
        }

        // 영업 시간 내인지 확인
        int openMin = toMinutes(store.getOpenTime());
        int closeMin = toMinutes(store.getCloseTime());
        if (startMin < openMin || endMin > closeMin) {
            throw new IllegalArgumentException("영업 시간 내에만 예약할 수 있습니다.");
        }

        // 휴무일 확인
        if (store.getClosedDays().contains(request.getDate().getDayOfWeek())) {
            throw new IllegalArgumentException("해당 날짜는 휴무일입니다.");
        }

        // 자리 충돌 검증: 요청 구간의 모든 슬롯에 자리가 있는지 확인
        validateSlotAvailability(store, request);

        Reservation reservation = Reservation.builder()
                .member(member)
                .store(store)
                .date(request.getDate())
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .headcount(request.getHeadcount())
                .status(ReservationStatus.REQUESTED)
                .build();

        reservationRepository.save(reservation);

        return ReservationResponse.from(reservation);
    }

    public MyReservationListResponse getMyReservations(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException("회원을 찾을 수 없습니다."));

        List<Reservation> reservations = reservationRepository.findByMember(member);
        List<ReservationResponse> responses = reservations.stream()
                .map(ReservationResponse::from)
                .toList();

        return MyReservationListResponse.builder()
                .totalReservations(responses.size())
                .reservations(responses)
                .build();
    }

    private void validateSlotAvailability(Store store, ReservationRequest request) {
        List<Reservation> existingReservations =
                reservationRepository.findActiveByStoreAndDate(store.getId(), request.getDate());

        // 슬롯별 예약 인원 합산 (분 단위로 처리)
        Map<Integer, Integer> slotUsage = new HashMap<>();
        for (Reservation r : existingReservations) {
            int rStart = toMinutes(r.getStartTime());
            int rEnd = toMinutes(r.getEndTime());
            for (int m = rStart; m < rEnd; m += 60) {
                slotUsage.merge(m, r.getHeadcount(), Integer::sum);
            }
        }

        // 요청 구간의 모든 슬롯에 충분한 자리가 있는지 확인
        int reqStart = toMinutes(request.getStartTime());
        int reqEnd = toMinutes(request.getEndTime());
        for (int m = reqStart; m < reqEnd; m += 60) {
            int used = slotUsage.getOrDefault(m, 0);
            int remaining = store.getSeatCapacity() - used;
            if (remaining < request.getHeadcount()) {
                throw new ConflictException("선택한 시간에 예약 가능한 자리가 부족합니다.");
            }
        }
    }

    private int toMinutes(LocalTime time) {
        int m = time.getHour() * 60 + time.getMinute();
        return m == 0 ? 1440 : m;
    }
}
