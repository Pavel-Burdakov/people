package com.example.servingwebcontent.client;
import com.example.servingwebcontent.exception.ServiceException;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;

@Component
public class cbrClient {
    private OkHttpClient okHttpClient = new OkHttpClient();
    @Value("${cbr.currency.rates.xml.url}")
    String url;
    String mockUrl = "http://localhost:8081/rate";
    //TODO переписать интеграцию

    public String getCurrencyRatesXML() throws ServiceException {
        // формируем запрос
        var request = new Request.Builder()
                .url(url)
                .build();
        // выполняем запрос и сохраняем ответ в переменной response
        // конструкция auto close и может выбрасывать исключения, поэтому try with resources
        try (var response = okHttpClient.newCall(request).execute();) {
            var body = response.body();
            if (body == null) {
                return null;
            }
            return body.string();
        } catch (IOException e) {
            throw new ServiceException("Ошибка получения курсов валют", e);
        }
    }
}
