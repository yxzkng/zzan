package likelion.demo.store.service;

import likelion.demo.global.exception.NotFoundException;
import likelion.demo.reservation.entity.Reservation;
import likelion.demo.reservation.repository.ReservationRepository;
import likelion.demo.store.dto.StoreListResponse;
import likelion.demo.store.dto.TimeslotResponse;
import likelion.demo.store.entity.Store;
import likelion.demo.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StoreService {

    private final StoreRepository storeRepository;
    private final ReservationRepository reservationRepository;

    public StoreListResponse getAvailableStores(LocalDate date, int headcount) {
        List<Store> stores = storeRepository.findAllWithClosedDays();

        DayOfWeek dayOfWeek = date.getDayOfWeek();

        // 휴무일인 술집 제외
        List<Store> openStores = stores.stream()
                .filter(store -> !store.getClosedDays().contains(dayOfWeek))
                .toList();

        // 해당 날짜의 예약 현황: storeId -> (startTime -> 예약 인원 합)
        Map<Long, Map<LocalTime, Long>> reservedMap = buildReservedMap(date);

        // 예약 가능한 술집 필터링
        List<StoreListResponse.StoreItem> storeItems = new ArrayList<>();

        for (Store store : openStores) {
            List<LocalTime> timeslots = generateTimeslots(store.getOpenTime(), store.getCloseTime());
            Map<LocalTime, Long> storeReserved = reservedMap.getOrDefault(store.getId(), Collections.emptyMap());

            int remainingSeatsMax = 0;
            boolean hasAvailableSlot = false;

            for (LocalTime slotStart : timeslots) {
                long reserved = storeReserved.getOrDefault(slotStart, 0L);
                int remaining = store.getSeatCapacity() - (int) reserved;
                remaining = Math.max(remaining, 0);

                if (remaining > remainingSeatsMax) {
                    remainingSeatsMax = remaining;
                }
                if (remaining >= headcount) {
                    hasAvailableSlot = true;
                }
            }

            if (hasAvailableSlot) {
                storeItems.add(StoreListResponse.StoreItem.builder()
                        .id(store.getId())
                        .name(store.getName())
                        .address(store.getAddress())
                        .openTime(formatTime(store.getOpenTime()))
                        .closeTime(formatTime(store.getCloseTime()))
                        .remainingSeatsMax(remainingSeatsMax)
                        .build());
            }
        }

        return StoreListResponse.builder()
                .date(date)
                .headcount(headcount)
                .stores(storeItems)
                .build();
    }

    public TimeslotResponse getTimeslots(Long storeId, LocalDate date, int headcount) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new NotFoundException("해당 술집을 찾을 수 없습니다."));

        List<LocalTime> timeslots = generateTimeslots(store.getOpenTime(), store.getCloseTime());
        Map<Long, Map<LocalTime, Long>> reservedMap = buildReservedMap(date);
        Map<LocalTime, Long> storeReserved = reservedMap.getOrDefault(storeId, Collections.emptyMap());

        List<TimeslotResponse.SlotItem> slotItems = new ArrayList<>();
        for (LocalTime slotStart : timeslots) {
            long reserved = storeReserved.getOrDefault(slotStart, 0L);
            int remainingSeats = Math.max(store.getSeatCapacity() - (int) reserved, 0);
            boolean available = remainingSeats >= headcount;

            slotItems.add(TimeslotResponse.SlotItem.builder()
                    .startTime(slotStart)
                    .remainingSeats(remainingSeats)
                    .available(available)
                    .build());
        }

        return TimeslotResponse.builder()
                .storeId(store.getId())
                .storeName(store.getName())
                .date(date)
                .slotCapacity(store.getSeatCapacity())
                .slots(slotItems)
                .build();
    }

    private Map<Long, Map<LocalTime, Long>> buildReservedMap(LocalDate date) {
        List<Reservation> reservations = reservationRepository.findActiveByDate(date);
        Map<Long, Map<LocalTime, Long>> map = new HashMap<>();

        for (Reservation r : reservations) {
            Long storeId = r.getStore().getId();
            Map<LocalTime, Long> slotMap = map.computeIfAbsent(storeId, k -> new HashMap<>());

            int startMin = toMinutes(r.getStartTime());
            int endMin = toMinutes(r.getEndTime());
            for (int m = startMin; m < endMin; m += 60) {
                LocalTime slotTime = LocalTime.of(m / 60, m % 60);
                slotMap.merge(slotTime, (long) r.getHeadcount(), Long::sum);
            }
        }

        return map;
    }

    private List<LocalTime> generateTimeslots(LocalTime openTime, LocalTime closeTime) {
        List<LocalTime> slots = new ArrayList<>();
        int openMin = toMinutes(openTime);
        int closeMin = toMinutes(closeTime);

        for (int m = openMin; m < closeMin; m += 60) {
            slots.add(LocalTime.of(m / 60, m % 60));
        }

        return slots;
    }

    private int toMinutes(LocalTime time) {
        int m = time.getHour() * 60 + time.getMinute();
        return m == 0 ? 1440 : m;
    }

    private String formatTime(LocalTime time) {
        if (time.equals(LocalTime.MIDNIGHT)) {
            return "24:00";
        }
        return String.format("%02d:%02d", time.getHour(), time.getMinute());
    }
}
