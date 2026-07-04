package likelion.demo.reservation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class MyReservationListResponse {

    private int totalReservations;
    private List<ReservationResponse> reservations;
}
