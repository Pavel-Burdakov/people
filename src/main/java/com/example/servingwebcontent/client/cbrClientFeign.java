package com.example.servingwebcontent.client;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "cbrRate", url = "https://www.cbr.ru/scripts/XML_daily.asp/")
public interface cbrClientFeign {
    @RequestMapping(method = RequestMethod.GET)
    String readCurrentRate();
}
