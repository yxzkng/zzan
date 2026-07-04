package likelion.demo.reservation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class ReservationCancelResponse {

    private Long reservationId;
    private String status;
}
