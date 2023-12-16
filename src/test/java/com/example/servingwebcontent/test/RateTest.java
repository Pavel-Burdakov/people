package com.example.servingwebcontent.test;
import com.example.servingwebcontent.client.cbrClient;
import com.example.servingwebcontent.models.Person;
import com.github.dockerjava.zerodep.shaded.org.apache.hc.client5.http.classic.methods.HttpGet;
import com.github.dockerjava.zerodep.shaded.org.apache.hc.client5.http.impl.classic.BasicHttpClientResponseHandler;
import com.github.dockerjava.zerodep.shaded.org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import com.github.dockerjava.zerodep.shaded.org.apache.hc.client5.http.impl.classic.HttpClients;
import com.github.dockerjava.zerodep.shaded.org.apache.hc.core5.http.HttpResponse;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.junit.jupiter.api.*;
import org.junit.platform.engine.TestExecutionResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultMatcher;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.util.StreamUtils.copyToString;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.logging.Logger;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static java.nio.charset.Charset.defaultCharset;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@WireMockTest
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles({"test"})
public class RateTest {
    @Autowired
    private WebTestClient webTestClient;
    @Autowired
    MockMvc mockMvc;
    private static Logger logger = Logger.getLogger(RateTest.class.getName());
    //  https://www.cbr.ru/scripts/XML_daily.asp
    static String apiUrl = "/rate";
    private static final String RESPONSE_FILE = "XML_daily.asp";
    private static WireMockServer wireMockServer = new WireMockServer(8081);

    @BeforeEach
    void configure() throws IOException {
        wireMockServer.start();
        configureFor("localhost", 8081);
        stubFor(get(urlEqualTo(apiUrl))
                .willReturn(aResponse()
                        .withStatus(HttpStatus.OK.value())
                        //.withHeader("Content-Type", String.valueOf(MediaType.APPLICATION_JSON_VALUE))
                        .withBody(
                                copyToString(RateTest.class.getClassLoader().getResourceAsStream(RESPONSE_FILE), defaultCharset())
                        )
                ));
    }

    // TODO: 11.12.2023 точка останова на вызове контроллера и проверить что он идет на stub
    @Test
    void problemTest1() throws Exception {
        mockMvc.perform((RequestBuilder) get("/account/8/EUR"))
                .andExpect((ResultMatcher) status(200));
    }

    @Test
    void problemTest2() throws Exception {
        mockMvc.perform(get("/account/8/EUR"))
                .andExpect(status().isOk());
    }

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