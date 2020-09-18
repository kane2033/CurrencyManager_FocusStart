package com.fosusstart.currency.objects;


import android.os.Parcel;
import android.os.Parcelable;

import com.squareup.moshi.Json;

public class CurrencyItem implements Parcelable {
    @Json(name = "NumCode")
    private String numCode;
    @Json(name = "CharCode")
    private String charCode;
    @Json(name = "Nominal")
    private int nominal;
    @Json(name = "Name")
    private String name;
    @Json(name = "Value")
    private double value;
    @Json(name = "Previous")
    private double previous;

    public CurrencyItem(String numCode, String charCode, int nominal, String name, double value, double previous) {
        this.numCode = numCode;
        this.charCode = charCode;
        this.nominal = nominal;
        this.name = name;
        this.value = value;
        this.previous = previous;
    }

    public String getNumCode() {
        return numCode;
    }

    public void setNumCode(String numCode) {
        this.numCode = numCode;
    }

    public String getCharCode() {
        return charCode;
    }

    public void setCharCode(String charCode) {
        this.charCode = charCode;
    }

    public int getNominal() {
        return nominal;
    }

    public void setNominal(int nominal) {
        this.nominal = nominal;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public double getPrevious() {
        return previous;
    }

    public void setPrevious(double previous) {
        this.previous = previous;
    }

    /**
     * Имплементация parcelable интерфейса
     * для передачи через bundle
     * */
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.numCode);
        dest.writeString(this.charCode);
        dest.writeInt(this.nominal);
        dest.writeString(this.name);
        dest.writeDouble(this.value);
        dest.writeDouble(this.previous);
    }

    protected CurrencyItem(Parcel in) {
        this.numCode = in.readString();
        this.charCode = in.readString();
        this.nominal = in.readInt();
        this.name = in.readString();
        this.value = in.readDouble();
        this.previous = in.readDouble();
    }

    public static final Creator<CurrencyItem> CREATOR = new Creator<CurrencyItem>() {
        @Override
        public CurrencyItem createFromParcel(Parcel source) {
            return new CurrencyItem(source);
        }

        @Override
        public CurrencyItem[] newArray(int size) {
            return new CurrencyItem[size];
        }
    };
}