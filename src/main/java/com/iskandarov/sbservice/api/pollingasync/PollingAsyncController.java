package com.iskandarov.sbservice.api.pollingasync;

import com.iskandarov.sbservice.taskservice.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

import static com.iskandarov.sbservice.api.pollingasync.PollingAsyncController.TaskStatus.RUNNING;

@RestController
@RequestMapping("/api/PollingAsync")
public class PollingAsyncController {

    public record TaskStatusResponse(String taskId, TaskStatus status, String result) {}
    public enum TaskStatus {RUNNING, COMPLETED, FAILED};

    @Autowired
    TaskService taskService;

    @PostMapping("/startTask")
    public ResponseEntity<Void> startAsyncTask(@RequestBody String payload) {
        String taskId = UUID.randomUUID().toString();//TODO make it unique indeed
        taskService.startTask(taskId, payload);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequestUri()
                .path("/{id}")
                .buildAndExpand(taskId)
                .toUri();


        return ResponseEntity.accepted().location(location).build();
    }

    @GetMapping("/startTask/{taskId}")
    public ResponseEntity<TaskStatusResponse> getStatus(@PathVariable String taskId) {
        TaskStatusResponse task = taskService.getTaskStatus(taskId);

        if (task == null) {
            return ResponseEntity.notFound().build();
        }

        if (RUNNING.equals(task.status())) {
            // if the task still runs - return 202
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(task);
        }

        // If the task completed - 200 OK
        return ResponseEntity.ok(task);


    }
}
