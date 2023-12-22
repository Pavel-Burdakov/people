package com.example.servingwebcontent.client;
import com.example.servingwebcontent.exception.ServiceException;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class cbrClient {
    private OkHttpClient okHttpClient = new OkHttpClient();
    private final cbrClientFeign clientFeign;
    @Value("${cbr.currency.rates.xml.url}")
    String url;

    public cbrClient(cbrClientFeign clientFeign) {
        this.clientFeign = clientFeign;
    }
    //TODO переписать интеграцию

    public String getCurrencyRatesXML() throws ServiceException {
        // формируем запрос
        // выполняем запрос и сохраняем ответ в переменной response
        // конструкция auto close и может выбрасывать исключения, поэтому try with resources
        String response = clientFeign.readCurrentRate();
        //var body = response.body();
        if (response == null) {
            return null;
        }
        return response;
    }

    /*public String getCurrencyRatesXML() throws ServiceException {
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
    }*/
}
