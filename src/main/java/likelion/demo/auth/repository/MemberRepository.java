package likelion.demo.auth.repository;

import likelion.demo.auth.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    // TODO: JpaRepository<Member, Long> 구현
    boolean existsByLoginId(String loginId);
    Optional<Member> findByLoginId(String loginId);
}
