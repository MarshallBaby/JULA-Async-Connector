package org.marshallbaby.julaasyncconnector.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.marshallbaby.julaasyncconnector.domain.*;
import org.marshallbaby.julaasyncconnector.repo.TaskRepository;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static java.lang.String.format;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskService {

    private static final Response TASK_INCOMPLETE_RESPONSE = Response.builder()
            .message("TASK_INCOMPLETE")
            .build();

    private final TaskRepository taskRepository;
    private final Clock applicationClock;

    public String createTask(TaskCreationRequest taskCreationRequest) {

        Task task = buildTask(taskCreationRequest);
        UUID taskId = taskRepository.save(task).getTaskId();
        return taskId.toString();
    }

    public Task getWaitingTask() {

        Optional<Task> task = taskRepository.findFirstByTaskStatusOrderByCreationTimestampAsc(TaskStatus.WAITING);
        return task.map(this::updateTaskInProgressStatus).orElse(null);
    }

    public void updateTask(TaskUpdateRequest taskUpdateRequest) {

        UUID taskId = taskUpdateRequest.getTaskId();
        Task task = findTask(taskId);

        if (task.getTaskStatus() != TaskStatus.IN_PROGRESS) {
            throw new IllegalStateException(String.format("Task [%s] has been already updated", taskId));
        }

        task.setResponsePayload(taskUpdateRequest.getResponsePayload());
        task.setTaskStatus(TaskStatus.COMPLETE);

        taskRepository.save(task);
    }

    public Response fetchResponse(UUID taskId) {

        Task task = findTask(taskId);

        if (task.getTaskStatus() != TaskStatus.COMPLETE) {
            return TASK_INCOMPLETE_RESPONSE;
        }

        taskRepository.delete(task);

        return Response.builder()
                .message(TaskStatus.COMPLETE.toString())
                .payload(task.getResponsePayload())
                .build();
    }

    private Task buildTask(TaskCreationRequest taskCreationRequest) {

        return Task.builder()
                .requestPayload(taskCreationRequest.getRequestPayload())
                .creationTimestamp(LocalDateTime.now(applicationClock))
                .taskStatus(TaskStatus.WAITING)
                .build();
    }

    private Task updateTaskInProgressStatus(Task task) {

        task.setTaskStatus(TaskStatus.IN_PROGRESS);
        log.info("Setting task [{}] in status IN_PROGRESS.", task.getTaskId());
        return taskRepository.save(task);
    }

    private Task findTask(UUID taskId) {

        return taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException(format("Could not find task with id: [%s].", taskId)));
    }

}
