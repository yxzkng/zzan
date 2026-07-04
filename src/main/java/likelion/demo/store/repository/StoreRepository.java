package likelion.demo.store.repository;

import likelion.demo.store.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StoreRepository extends JpaRepository<Store, Long> {

    @Query("SELECT DISTINCT s FROM Store s LEFT JOIN FETCH s.closedDays")
    List<Store> findAllWithClosedDays();
}
