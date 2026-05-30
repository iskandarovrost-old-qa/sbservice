package com.iskandarov.sbservice.api.simpleasync;

import com.iskandarov.sbservice.simpleasync.DemoAsyncService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/SimpleAsync")
public class SimpleAsyncController {

    @Autowired
    private DemoAsyncService demoService;


    static String result = "{\"result\":\"123\"}";

    // POST endpoint: http://localhost:8080/api/SimpleAsync/setAsyncResult
    @PostMapping("setAsyncResult")
    public ResponseEntity<Void> prepAsyncResult(@RequestBody  String request) {
        demoService.processAsyncDemoJob(request);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }


    // GET endpoint: http://localhost:8080/api/SimpleAsync/getAsyncResult
    @GetMapping("getAsyncResult")
    public ResponseEntity<String> getAsyncResult(@RequestParam("value") String value) {



        result = demoService.getValue(value);
        if (result == "not found") return ResponseEntity.notFound().build();

        return ResponseEntity.ok("{\"result\":\""+result+"\"}");
    }




}
