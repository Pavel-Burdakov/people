package com.example.servingwebcontent.client;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "cbrRate", url = "${cbr.currency.rates.xml.url}")
public interface cbrClientFeign {
    @RequestMapping(method = RequestMethod.GET)
    String readCurrentRate();
}
