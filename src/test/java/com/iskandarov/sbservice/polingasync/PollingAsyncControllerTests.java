package com.iskandarov.sbservice.polingasync;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.time.Duration;

import static io.restassured.RestAssured.given;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.equalTo;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PollingAsyncControllerTests {

    @LocalServerPort
    private int port;

    @BeforeEach
    public void setUp() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
    }


    @Test
    public void testAsyncPollingTaskCompletion() {
        // start async task (Expect initial 202)
        String polingUrl = given()
                .contentType("text/plain")
                .body("123")
                .when()
                    .post("/api/PollingAsync/startTask")
                .then()
                    .statusCode(202)
                .extract()
                    .header("Location");

        // Awaitility method await
        await()
                .atMost(Duration.ofSeconds(30))    // Max timeout
                .pollInterval(Duration.ofSeconds(2)) // Polling interval
                .untilAsserted(() -> {

                    given()
                    .when()
                        .get(polingUrl)
                    .then()
                         .statusCode(200)
                            .body("status", equalTo("COMPLETED"));;// Polls until this assertion passes

                });
    }


}
