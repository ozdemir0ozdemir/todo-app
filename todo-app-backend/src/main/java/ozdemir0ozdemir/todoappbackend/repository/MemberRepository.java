package ozdemir0ozdemir.todoappbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ozdemir0ozdemir.todoappbackend.model.Member;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByUsername(String username);
}
