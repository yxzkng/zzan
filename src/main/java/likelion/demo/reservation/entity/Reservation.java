package likelion.demo.reservation.entity;

import jakarta.persistence.*;
import likelion.demo.auth.entity.Member;
import likelion.demo.store.entity.Store;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private LocalTime startTime;

    @Column(nullable = false)
    private LocalTime endTime;

    @Column(nullable = false)
    private int headcount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReservationStatus status;

    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    @Builder
    public Reservation(Member member, Store store, LocalDate date,
                       LocalTime startTime, LocalTime endTime,
                       int headcount, ReservationStatus status) {
        this.member = member;
        this.store = store;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.headcount = headcount;
        this.status = status;
    }

    public void cancel() {
        this.status = ReservationStatus.CANCELED;
    }
}
