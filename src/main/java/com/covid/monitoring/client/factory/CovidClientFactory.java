package com.covid.monitoring.client.factory;

import com.covid.monitoring.client.CovidDataClient;
import com.covid.monitoring.client.impl.CovidDataClientImpl;

public class CovidClientFactory {

    private static final CovidDataClient covidDataClient = new CovidDataClientImpl();

    /**
     * We use factory method here
     * Because we can use different http client in future
     * Factory gives us flexibility to switch
     * Without changing other classes
     */
    public CovidDataClient getClient() {
        return covidDataClient;
    }
}
