package com.covid.monitoring.client.impl;

import com.covid.monitoring.client.CovidDataClient;
import com.covid.monitoring.constant.CovidMonitoringConstants;
import com.covid.monitoring.dto.CovidCasesDto;
import com.covid.monitoring.dto.CovidHistoryDto;
import com.covid.monitoring.dto.CovidVaccineDto;
import com.covid.monitoring.util.HttpUtil;
import com.covid.monitoring.util.MapperUtil;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class CovidDataClientImpl implements CovidDataClient {

    private final HttpClient httpClient;
    private final HttpUtil httpUtil;

    /**
     * We instantiate fields within constructor for class being testable
     */
    public CovidDataClientImpl() {
        httpClient = HttpClientBuilder.create().build();
        httpUtil = new HttpUtil();
    }

    /**
     * We use CompletableFuture here for ability of executing request asynchronously
     */
    @Override
    public CompletableFuture<CovidCasesDto> getCovidCases(String country) {

        return CompletableFuture.supplyAsync(() -> {

            Map<String, String> params = new HashMap<>();
            params.put("country", country);

            try {
                HttpResponse httpResponse =
                        httpClient.execute(
                                httpUtil.createRequest(
                                        CovidMonitoringConstants.COVID_DATA_BASE_URL
                                                .concat(CovidMonitoringConstants.COVID_CASES_PATH),
                                        params));

                return MapperUtil
                        .getObjectMapper()
                        .readValue(httpResponse.getEntity().getContent(), CovidCasesDto.class);
            } catch (IOException e) {
                System.err.printf("Exception occurred during execution of getCovidCases: %s", e.getMessage());
            }

            return null;
        });
    }

    /**
     * We use CompletableFuture here for ability of executing request asynchronously
     */
    @Override
    public CompletableFuture<CovidVaccineDto> getCovidVaccines(String country) {

        return CompletableFuture.supplyAsync(() -> {

            Map<String, String> params = new HashMap<>();
            params.put("country", country);

            try {
                HttpResponse httpResponse =
                        httpClient.execute(
                                httpUtil.createRequest(
                                        CovidMonitoringConstants.COVID_DATA_BASE_URL
                                                .concat(CovidMonitoringConstants.COVID_VACCINES_PATH),
                                        params));

                return MapperUtil
                        .getObjectMapper()
                        .readValue(
                                httpResponse.getEntity().getContent(), CovidVaccineDto.class);
            } catch (IOException e) {
                System.err.printf("Exception occurred during execution of getCovidVaccines: %s", e.getMessage());
            }

            return null;
        });
    }

    /**
     * We use CompletableFuture here for ability of executing request asynchronously
     */
    @Override
    public CompletableFuture<CovidHistoryDto> getCovidHistory(String country, String status) {

        return CompletableFuture.supplyAsync(() -> {

            Map<String, String> params = new HashMap<>();
            params.put("country", country);
            params.put("status", status);

            try {
                HttpResponse httpResponse =
                        httpClient.execute(
                                httpUtil.createRequest(
                                        CovidMonitoringConstants.COVID_DATA_BASE_URL
                                                .concat(CovidMonitoringConstants.COVID_HISTORY_PATH),
                                        params));

                return MapperUtil
                        .getObjectMapper()
                        .readValue(
                                httpResponse.getEntity().getContent(), CovidHistoryDto.class);
            } catch (IOException e) {
                System.err.printf("Exception occurred during execution of getCovidHistory: %s", e.getMessage());
            }

            return null;
        });
    }
}
