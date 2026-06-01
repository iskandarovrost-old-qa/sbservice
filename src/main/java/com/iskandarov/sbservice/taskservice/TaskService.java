package com.iskandarov.sbservice.taskservice;

import com.iskandarov.sbservice.api.pollingasync.PollingAsyncController;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class TaskService {

    Random random = new Random();
    // Normal distribution
    double mean = 500.0;
    double standardDeviation = 10.0;


    //TODO add some persistent storage
    Map<String, PollingAsyncController.TaskStatusResponse> tasks = new ConcurrentHashMap<>();

    public PollingAsyncController.TaskStatusResponse getTaskStatus(String taskId) {
        return tasks.get(taskId);
    }

    @Async
    public void startTask(String taskId, String payload) {
        tasks.put(taskId, new PollingAsyncController.TaskStatusResponse(taskId,
                PollingAsyncController.TaskStatus.RUNNING, payload));


        try {
            long sleepValue = (long) (mean + tasks.values().stream()
                    .filter(val -> val.status().equals(PollingAsyncController.TaskStatus.RUNNING))
                    .count() //mean rizes with threads count
                    + (random.nextGaussian() * standardDeviation)); //and STD too
            // Simulation of hard work
            Thread.sleep(sleepValue);

//TODO do not create a new status, modify the curent
            tasks.put(taskId, new PollingAsyncController.TaskStatusResponse(taskId,
                    PollingAsyncController.TaskStatus.COMPLETED, "Processed: " + payload));

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            tasks.put(taskId, new PollingAsyncController.TaskStatusResponse(taskId, PollingAsyncController.TaskStatus.FAILED,"Error:" + payload));
        }
    }
}
