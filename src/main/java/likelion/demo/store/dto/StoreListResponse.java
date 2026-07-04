package likelion.demo.store.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class StoreListResponse {

    private LocalDate date;
    private int headcount;
    private List<StoreItem> stores;

    @Getter
    @AllArgsConstructor
    @Builder
    public static class StoreItem {
        private Long id;
        private String name;
        private String address;
        private String openTime;
        private String closeTime;
        private int remainingSeatsMax;
    }
}
