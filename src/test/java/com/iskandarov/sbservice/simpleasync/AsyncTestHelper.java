package com.iskandarov.sbservice.simpleasync;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.*;

public class AsyncTestHelper {
    public static boolean getWithTimeout (String getObjectUrl, long periodMillis, long timeoutMillis)
    {
        boolean getResult = false;
        
        ExecutorService executor = Executors.newSingleThreadExecutor();
        HttpClient client = HttpClient.newBuilder().build();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(getObjectUrl))
                .headers("Content-Type", "application/json") // Set the Content-Type header
                .GET().build();


        //start a thread with GET in loop
        Future<Boolean> future = executor.submit(() -> {
            Boolean result = false;
            try {
                while (true) {
                    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                    if (200 == response.statusCode()) {
                        result = true;
                        break;
                    }
                    Thread.sleep(periodMillis);
                }
            }
            catch (Exception e){
                result = false;
                //throw e;
            }
            return result;
        });
        
        try {
            // wait a maximum of timeoutMills
            getResult = future.get(timeoutMillis, TimeUnit.MILLISECONDS);
        } 
        catch (TimeoutException e) {
            System.err.println("GET timed out!");
            getResult = false;
            future.cancel(true);
        } 
        catch (InterruptedException | ExecutionException e) {
            System.err.println("Task execution failed: " + e.getMessage());
        }
        finally {
            executor.shutdown();
        }
        
        return getResult;
    }
}
