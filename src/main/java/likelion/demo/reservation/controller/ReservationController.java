package likelion.demo.reservation.controller;

import jakarta.servlet.http.HttpSession;
import likelion.demo.global.common.ApiResponse;
import likelion.demo.global.exception.UnauthorizedException;
import likelion.demo.reservation.dto.ReservationRequest;
import likelion.demo.reservation.dto.ReservationResponse;
import likelion.demo.reservation.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    @PostMapping
    public ResponseEntity<ApiResponse<ReservationResponse>> createReservation(
            @RequestBody ReservationRequest request,
            HttpSession session) {

        Long memberId = (Long) session.getAttribute("memberId");
        if (memberId == null) {
            throw new UnauthorizedException("로그인이 필요합니다.");
        }

        ReservationResponse response = reservationService.createReservation(memberId, request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(201, "예약 신청 성공", response));
    }
}
