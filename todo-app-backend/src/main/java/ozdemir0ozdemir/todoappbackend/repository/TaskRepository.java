package ozdemir0ozdemir.todoappbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ozdemir0ozdemir.todoappbackend.model.Task;

public interface TaskRepository extends JpaRepository<Task, Long> {
}
