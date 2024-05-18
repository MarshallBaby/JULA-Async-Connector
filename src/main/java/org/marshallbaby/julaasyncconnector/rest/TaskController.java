package org.marshallbaby.julaasyncconnector.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.marshallbaby.julaasyncconnector.domain.Response;
import org.marshallbaby.julaasyncconnector.domain.Task;
import org.marshallbaby.julaasyncconnector.domain.TaskCreationRequest;
import org.marshallbaby.julaasyncconnector.domain.TaskUpdateRequest;
import org.marshallbaby.julaasyncconnector.service.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/task")
public class TaskController {

    private static final ResponseEntity<Task> NOT_FOUND_RESPONSE_ENTITY = new ResponseEntity<>(HttpStatus.NOT_FOUND);

    private final TaskService taskService;

    @PostMapping
    public String createRequest(@RequestBody @Valid TaskCreationRequest taskCreationRequest) {

        log.info("Creating task.");
        return taskService.createTask(taskCreationRequest);
    }

    @GetMapping
    public ResponseEntity<Task> getWaitingTask() {

        log.info("Getting waiting task");
        Task task = taskService.getWaitingTask();
        return Objects.isNull(task) ?
                NOT_FOUND_RESPONSE_ENTITY :
                new ResponseEntity<>(task, HttpStatus.OK);
    }

    @GetMapping("/{taskId}")
    public Response getResponse(@PathVariable UUID taskId) {

        log.info("Fetching response for task [{}].", taskId);
        return taskService.fetchResponse(taskId);
    }

    @PutMapping
    public void updateTask(@RequestBody @Valid TaskUpdateRequest taskUpdateRequest) {

        log.info("Updating task [{}].", taskUpdateRequest.getTaskId());
        taskService.updateTask(taskUpdateRequest);
    }

}
