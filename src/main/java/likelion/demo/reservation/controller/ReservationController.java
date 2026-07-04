package likelion.demo.reservation.controller;

import jakarta.servlet.http.HttpSession;
import likelion.demo.global.common.ApiResponse;
import likelion.demo.global.exception.UnauthorizedException;
import likelion.demo.reservation.dto.MyReservationListResponse;
import likelion.demo.reservation.dto.ReservationCancelResponse;
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

        String loginId = (String) session.getAttribute("LOGIN_MEMBER_ID");
        if (loginId == null) {
            throw new UnauthorizedException("로그인이 필요합니다.");
        }

        ReservationResponse response = reservationService.createReservation(loginId, request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(201, "예약 신청 성공", response));
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<MyReservationListResponse>> getMyReservations(HttpSession session) {

        String loginId = (String) session.getAttribute("LOGIN_MEMBER_ID");
        if (loginId == null) {
            throw new UnauthorizedException("로그인이 필요합니다.");
        }

        MyReservationListResponse response = reservationService.getMyReservations(loginId);
        return ResponseEntity.ok(ApiResponse.success(200, "예약 목록 조회 성공", response));
    }

    @PatchMapping("/{reservationId}/cancel")
    public ResponseEntity<ApiResponse<ReservationCancelResponse>> cancelReservation(
            @PathVariable Long reservationId,
            HttpSession session) {

        String loginId = (String) session.getAttribute("LOGIN_MEMBER_ID");
        if (loginId == null) {
            throw new UnauthorizedException("로그인이 필요합니다.");
        }

        ReservationCancelResponse response = reservationService.cancelReservation(loginId, reservationId);
        return ResponseEntity.ok(ApiResponse.success(200, "예약이 취소되었습니다.", response));
    }
}
