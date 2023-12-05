package com.example.servingwebcontent.services;
import com.example.servingwebcontent.exception.ServiceException;

public interface ExchangeRateService {
    String getUSDCurrencyRate() throws ServiceException;

    String getEURCurrencyRate() throws ServiceException;
}
