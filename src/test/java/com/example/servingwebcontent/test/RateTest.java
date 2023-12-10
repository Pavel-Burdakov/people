package com.example.servingwebcontent.test;
import com.github.dockerjava.zerodep.shaded.org.apache.hc.client5.http.classic.methods.HttpGet;
import com.github.dockerjava.zerodep.shaded.org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import com.github.dockerjava.zerodep.shaded.org.apache.hc.client5.http.impl.classic.HttpClients;
import com.github.dockerjava.zerodep.shaded.org.apache.hc.core5.http.HttpResponse;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@WireMockTest
public class RateTest {
    //@Value("${cbr,currency.rates.xml.url}")
    String apiUrl = "/api-url";
    private static final String REQUEST_PATH = "/some/path";
    private static final String RESPONSE_FILE = "classpath:response.xml";
    private static WireMockServer wireMockServer = new WireMockServer();
    String responseBody = "Hello World !!";

    @BeforeEach
    void configure() {
        stubFor(get(urlEqualTo(apiUrl))
                .willReturn(ok(responseBody)
                        .withBodyFile("files/{{request.pathSegments.[1]}}"))
        );
    }

    @Test
    void simpleStubTesting(WireMockRuntimeInfo wmRuntimeInfo) {
       /* wireMockServer.stubFor(WireMock.get(urlPathEqualTo(apiUrl))
                .willReturn(ok()
                        .withBody(responseBody)
                ));*/
        //stubFor(get(apiUrl).willReturn(ok(responseBody)));
        //Hit API and check response
        String apiResponse = getContent(wmRuntimeInfo.getHttpBaseUrl() + apiUrl);
        assertEquals(responseBody, apiResponse);
        //Verify API is hit
        verify(getRequestedFor(urlEqualTo(apiUrl)));
    }

    private String getContent(String url) {
        TestRestTemplate testRestTemplate = new TestRestTemplate();
        return testRestTemplate.getForObject(url, String.class);
    }
}