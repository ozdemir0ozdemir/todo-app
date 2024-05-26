package ozdemir0ozdemir.todoappbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ozdemir0ozdemir.todoappbackend.model.Authority;

public interface AuthorityRepository extends JpaRepository<Authority, Long> {
}
