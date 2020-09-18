package com.fosusstart.currency.objects;

import com.squareup.moshi.Json;

import java.util.Date;
import java.util.Map;

public class CurrenciesDto {
    @Json(name = "Date")
    private Date date;
    @Json(name = "PreviousDate")
    private Date previousDate;
    @Json(name = "Timestamp")
    private Date timestamp;

    @Json(name = "Valute")
    private Map<String, CurrencyItem> currenciesList;

    public CurrenciesDto(Date date, Date previousDate, Date timestamp, Map<String, CurrencyItem> currenciesList) {
        this.date = date;
        this.previousDate = previousDate;
        this.timestamp = timestamp;
        this.currenciesList = currenciesList;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getPreviousDate() {
        return previousDate;
    }

    public void setPreviousDate(Date previousDate) {
        this.previousDate = previousDate;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public Map<String, CurrencyItem> getCurrenciesList() {
        return currenciesList;
    }

    public void setCurrenciesList(Map<String, CurrencyItem> currenciesList) {
        this.currenciesList = currenciesList;
    }
}
