package com.iskandarov.sbservice.simpleasync;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class DemoAsyncService {
   public static List<String> values = new ArrayList<>();

    @Async // Runs a heavy long operation in separate thread
    public void processAsyncDemoJob(String name) {
        Random random = new Random();

        // Normal distribution
        double mean = 5000.0;
        double standardDeviation = 100.0;
        double randomNormalValue = + mean + (random.nextGaussian() * standardDeviation);

        try {
            // Simulate
            Thread.sleep((long)randomNormalValue);
            System.out.println("Processing finished for: " + name);
            values.add(name);

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public String getValue(String value) {
        if (values.contains(value)) return value;
        else return "not found";
    }
}
