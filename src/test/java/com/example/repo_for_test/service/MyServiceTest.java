package com.example.repo_for_test.service;

import com.example.repo_for_test.MyResponseClass;
import com.example.repo_for_test.config.Config;
import com.example.repo_for_test.service.MyService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static org.mockito.Mockito.when;

@WebFluxTest
@SpringBootTest(classes = {MyService.class, Config.class})
public class MyServiceTest {

    @Autowired
    private MyService myService;

    @MockBean
    private WebClient.Builder webClientBuilder;

    @Test
    public void testFetchData() {
        // Подготовка макетного ответа от WebClient
        MyResponseClass expectedResponse = new MyResponseClass();
        expectedResponse.setName("Test Name");

        when(webClientBuilder.baseUrl("https://api.example.com").build().get().uri("/data").retrieve().bodyToMono(MyResponseClass.class))
                .thenReturn(Mono.just(expectedResponse));

        // Вызов метода fetchData() и проверка результата
        Mono<MyResponseClass> resultMono = myService.fetchData();
        resultMono.subscribe(result -> {
            // Проверка, что результат равен ожидаемому объекту
            assert expectedResponse.getName().equals(result.getName());
        });
    }
}
