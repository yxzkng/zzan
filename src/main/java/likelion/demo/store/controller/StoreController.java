package likelion.demo.store.controller;

import jakarta.servlet.http.HttpSession;
import likelion.demo.global.common.ApiResponse;
import likelion.demo.global.exception.UnauthorizedException;
import likelion.demo.store.dto.StoreListResponse;
import likelion.demo.store.dto.TimeslotResponse;
import likelion.demo.store.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/stores")
@RequiredArgsConstructor
public class StoreController {

    private final StoreService storeService;

    @GetMapping
    public ResponseEntity<ApiResponse<StoreListResponse>> getAvailableStores(
            @RequestParam LocalDate date,
            @RequestParam int headcount,
            HttpSession session) {

        if (session.getAttribute("memberId") == null) {
            throw new UnauthorizedException("로그인이 필요합니다.");
        }

        StoreListResponse response = storeService.getAvailableStores(date, headcount);
        return ResponseEntity.ok(ApiResponse.success(200, "술집 목록 조회 성공", response));
    }

    @GetMapping("/{storeId}/timeslots")
    public ResponseEntity<ApiResponse<TimeslotResponse>> getTimeslots(
            @PathVariable Long storeId,
            @RequestParam LocalDate date,
            @RequestParam int headcount,
            HttpSession session) {

        if (session.getAttribute("memberId") == null) {
            throw new UnauthorizedException("로그인이 필요합니다.");
        }

        TimeslotResponse response = storeService.getTimeslots(storeId, date, headcount);
        return ResponseEntity.ok(ApiResponse.success(200, "예약 현황 조회 성공", response));
    }
}
