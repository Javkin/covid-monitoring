package com.covid.monitoring.constant;

public class ErrorConstants {

    public static final String GENERAL_ERROR = "Error occurred when loading covid data";
    public static final String INVALID_COUNTRY = "Invalid country name";

    /**
     * Here we declare constructor private and prevent instantiation
     * Because this class is utility class
     */
    private ErrorConstants() {
        throw new UnsupportedOperationException(String.format("Cannot create instance of %s", getClass()));
    }
}
