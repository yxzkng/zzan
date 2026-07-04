package likelion.demo.reservation.repository;

import likelion.demo.member.entity.Member;
import likelion.demo.reservation.entity.Reservation;
import likelion.demo.reservation.entity.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    @Query("SELECT r FROM Reservation r " +
           "WHERE r.date = :date " +
           "AND r.status <> likelion.demo.reservation.entity.ReservationStatus.CANCELED")
    List<Reservation> findActiveByDate(@Param("date") LocalDate date);

    @Query("SELECT r FROM Reservation r " +
           "WHERE r.store.id = :storeId " +
           "AND r.date = :date " +
           "AND r.status <> likelion.demo.reservation.entity.ReservationStatus.CANCELED")
    List<Reservation> findActiveByStoreAndDate(@Param("storeId") Long storeId,
                                               @Param("date") LocalDate date);

    List<Reservation> findByMember(Member member);
}
