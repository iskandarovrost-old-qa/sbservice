package com.iskandarov.sbservice.utils;

import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

@Component
public class Monitoring {

    // Enable the custom graphite
    @Value("${graphite.custom.enabled:false}")
    boolean enabled;

    // host for the custom graphite
    @Value("${graphite.custom.host:localhost}")
    String host;

    // port for the custom graphite
    @Value("${graphite.custom.port:2003}")
    int port;

    // port for the custom graphite
    @Value("${spring.application.name}")
    String appName;

    @Autowired
    MeterRegistry meterRegistry;

    @Scheduled(fixedRateString = "${monitoring.resources.interval:10s}")
    public void monitorMemory() {

        if (!enabled) return;

        //heap size
        double usedHeap = meterRegistry.get("jvm.memory.used")
                .tag("area", "heap")
                .gauge()
                .value();

        //max heap size
        double maxHeap = meterRegistry.get("jvm.memory.max")
                .tag("area", "heap")
                .gauge()
                .value();

        if (usedHeap > 0.1) { //need to filter it here
            double usedMegabytes = usedHeap / (1024 * 1024);
            sendMetricDirectly(appName+".memoryUsage", usedMegabytes);

            if (maxHeap > 0.2 ){
                double maxMegabytes = maxHeap / (1024 * 1024);
                double percentage = (usedHeap / maxHeap) * 100;
                sendMetricDirectly(appName + ".memoryUtilisation", percentage);

                System.out.printf("Memory usage: %.2f MB of %.2f MB (%.2f%%)%n",
                        usedMegabytes, maxMegabytes, percentage);
            }

        }


    }
    void sendMetricDirectly(String metricPath, double value) {

        long timestamp = System.currentTimeMillis() / 1000; // Graphite ждет секунды, не мс

        // Graphite: path value timestamp\n
        // using "+" leads to value is not converted according to locale and always sent with "."
        // perhaps
        String payload = metricPath + " " + value + " " + timestamp + "\n";

        try (Socket socket = new Socket(host, port);
             OutputStream os = socket.getOutputStream();
             PrintWriter writer = new PrintWriter(os, true)) {
            writer.print(payload);
        } catch (Exception e) {
            System.err.println("Error sending custom metrics to Graphite: " + e.getMessage());
        }
    }


}
