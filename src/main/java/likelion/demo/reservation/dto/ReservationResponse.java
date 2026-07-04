package likelion.demo.reservation.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import likelion.demo.reservation.entity.Reservation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@AllArgsConstructor
@Builder
public class ReservationResponse {

    private Long reservationId;
    private String storeName;
    private LocalDate date;

    @JsonFormat(pattern = "HH:mm")
    private LocalTime startTime;

    @JsonFormat(pattern = "HH:mm")
    private LocalTime endTime;

    private int headcount;
    private String status;

    public static ReservationResponse from(Reservation reservation) {
        return ReservationResponse.builder()
                .reservationId(reservation.getId())
                .storeName(reservation.getStore().getName())
                .date(reservation.getDate())
                .startTime(reservation.getStartTime())
                .endTime(reservation.getEndTime())
                .headcount(reservation.getHeadcount())
                .status(reservation.getStatus().name())
                .build();
    }
}
