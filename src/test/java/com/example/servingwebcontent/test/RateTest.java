package com.example.servingwebcontent.test;
import com.example.servingwebcontent.client.cbrClient;
import com.github.dockerjava.zerodep.shaded.org.apache.hc.client5.http.classic.methods.HttpGet;
import com.github.dockerjava.zerodep.shaded.org.apache.hc.client5.http.impl.classic.BasicHttpClientResponseHandler;
import com.github.dockerjava.zerodep.shaded.org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import com.github.dockerjava.zerodep.shaded.org.apache.hc.client5.http.impl.classic.HttpClients;
import com.github.dockerjava.zerodep.shaded.org.apache.hc.core5.http.HttpResponse;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.RequestBuilder;

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
public class RateTest {
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
    void simpleStubTesting(WireMockRuntimeInfo wmRuntimeInfo) throws IOException {
        HttpGet request = new HttpGet("http://localhost:8081/rate");
        CloseableHttpClient client = HttpClients.createDefault();
        String response = client.execute(request, new BasicHttpClientResponseHandler());
        logger.info(response);
        HttpGet request1 = new HttpGet("http://localhost:8080/account/8/EUR");
        CloseableHttpClient client1 = HttpClients.createDefault();
        String response1 = client.execute(request1, new BasicHttpClientResponseHandler());
        logger.info(response1);
        //String stringResponse = convertHttpResponseToString(httpResponse);

        //Hit API and check response
        String apiResponse = getContent(wmRuntimeInfo.getHttpBaseUrl() + apiUrl);
        //assertEquals("vvn", apiResponse);
        //Verify API is hit
        verify(getRequestedFor(urlEqualTo(apiUrl)));
        wireMockServer.stop();
    }

    private String getContent(String url) {
        TestRestTemplate testRestTemplate = new TestRestTemplate();
        return testRestTemplate.getForObject(url, String.class);
    }
}