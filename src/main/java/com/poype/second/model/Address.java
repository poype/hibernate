package com.poype.second.model;

public class Address {

    private String province;

    private String city;

    private String postCode;

    public Address() {
    }

    public Address(String province, String city, String postCode) {
        this.province = province;
        this.city = city;
        this.postCode = postCode;
    }

    @Override
    public String toString() {
        return this.province + "*" + this.city + "*" + this.postCode;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }
}
