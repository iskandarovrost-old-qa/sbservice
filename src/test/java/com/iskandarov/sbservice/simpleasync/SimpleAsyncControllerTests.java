package com.iskandarov.sbservice.simpleasync;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.web.servlet.client.RestTestClient;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)

public class SimpleAsyncControllerTests {
    @LocalServerPort
    private int port; // Инжектируем реальный случайный порт Tomcat

    private RestTestClient restClient;

    @BeforeEach
    void setUp() {
        // Вручную собираем клиент и привязываем его к запущенному серверу
        this.restClient = RestTestClient.bindToServer()
                .baseUrl("http://localhost:" + port)
                .build();
    }

    @ParameterizedTest()
    @ValueSource(strings = {"123", "124", "6", "8", "Redhat girl"})
    public void testAsyncScenario(String value) throws InterruptedException {

        //do the POST
        restClient.post().uri( "/api/SimpleAsync/setAsyncResult")
                .body(value)
                .exchange()
                .expectStatus().isAccepted();


        Thread.sleep(5300); //mean + 3 std

        restClient.get().uri( "/api/SimpleAsync/getAsyncResult?value="+value)
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class).consumeWith(result -> {
                    String getbody = result.getResponseBody();
                    Assertions.assertTrue(getbody.contains(value)); //NPE if empty body. It's OK: no empty body allowed

                });


    }
    @ParameterizedTest()
    @ValueSource(strings = {"1333", "124333", "633", "833", "Redhat_girl_333"})
    public void testAsyncScenarioWaiter(String value) throws InterruptedException {

        //do the POST
        restClient.post().uri( "/api/SimpleAsync/setAsyncResult")
                .body(value)
                .exchange()
                .expectStatus().isAccepted();


        //here we do not wait the fixed time: as the GET returns 200 - the test is completed
        boolean waiterResult = AsyncTestHelper.getWithTimeout( "http://localhost:" +
                port + "/api/SimpleAsync/getAsyncResult?value="+value,100, 5300);
        Assertions.assertTrue(waiterResult,"the Object did not appear by timeout");


    }
}
