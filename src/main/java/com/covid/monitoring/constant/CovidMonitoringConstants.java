package com.covid.monitoring.constant;

public class CovidMonitoringConstants {

    public static final String COVID_DATA_BASE_URL = "https://covid-api.mmediagroup.fr/v1";
    public static final String COVID_CASES_PATH = "/cases";
    public static final String COVID_HISTORY_PATH = "/history";
    public static final String COVID_VACCINES_PATH = "/vaccines";
    public static final String COVID_CONFIRMED_STATUS = "Confirmed";

    /**
     * Here we declare constructor private and prevent instantiation
     * Because this class is utility class
     */
    private CovidMonitoringConstants() {
        throw new UnsupportedOperationException(String.format("Cannot create instance of %s", getClass()));
    }
}
