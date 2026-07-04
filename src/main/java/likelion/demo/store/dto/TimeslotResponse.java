package likelion.demo.store.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class TimeslotResponse {

    private Long storeId;
    private String storeName;
    private LocalDate date;
    private int slotCapacity;
    private List<SlotItem> slots;

    @Getter
    @AllArgsConstructor
    @Builder
    public static class SlotItem {

        @JsonFormat(pattern = "HH:mm")
        private LocalTime startTime;

        private int remainingSeats;
        private boolean available;
    }
}
