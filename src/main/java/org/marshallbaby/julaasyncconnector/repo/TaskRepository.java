package org.marshallbaby.julaasyncconnector.repo;

import org.marshallbaby.julaasyncconnector.domain.Task;
import org.marshallbaby.julaasyncconnector.domain.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface TaskRepository extends JpaRepository<Task, UUID> {

    Optional<Task> findFirstByTaskStatusOrderByCreationTimestampAsc(TaskStatus taskStatus);
}
