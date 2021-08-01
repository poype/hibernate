package com.poype.second.model;

public enum Gender {
    MALE("M"),

    FEMALE("F")
    ;

    private String code;

    Gender(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "XXX Gender{" +
                "code='" + code + '\'' +
                '}';
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
