package com.example.servingwebcontent.test;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.io.IOException;
import java.util.logging.Logger;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static java.nio.charset.Charset.defaultCharset;
import static org.springframework.util.StreamUtils.copyToString;

@WireMockTest
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient(timeout = "10000")
@ActiveProfiles({"test"})
public class RateTest {
    @Autowired
    private WebTestClient webTestClient;
    private static Logger logger = Logger.getLogger(RateTest.class.getName());
    static String apiUrl = "/rate";
    private static final String RESPONSE_FILE = "XML_daily.asp";
    //private static WireMockServer wireMockServer = new WireMockServer(new WireMockConfiguration().dynamicPort());
    private static WireMockServer wireMockServer = new WireMockServer(8081);

    @BeforeEach
    void configure() throws IOException {
        wireMockServer.start();
        configureFor("localhost", wireMockServer.port());
        stubFor(get(urlEqualTo(apiUrl))
                .willReturn(aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withBody(
                                copyToString(RateTest.class.getClassLoader().getResourceAsStream(RESPONSE_FILE), defaultCharset())
                        )
                ));
    }

    @AfterEach
    void stop() {
        wireMockServer.stop();
    }
    // TODO: 11.12.2023 точка останова на вызове контроллера и проверить что он идет на stub

    @Test
    void apiCbrTest() throws Exception {
        webTestClient
                .get()
                .uri("/account/8/EUR")
                .exchange()
                .expectAll(
                        responseSpec -> responseSpec.expectStatus().isOk(),
                        responseSpec -> responseSpec.expectHeader().contentType(MediaType.APPLICATION_JSON),
                        responseSpec -> responseSpec.expectBody().json("10000.0")
                );
    }
}