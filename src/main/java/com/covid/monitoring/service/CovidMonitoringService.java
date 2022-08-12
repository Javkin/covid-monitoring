package com.covid.monitoring.service;

import com.covid.monitoring.dto.CovidMonitoringDto;

import java.util.concurrent.CompletableFuture;

public interface CovidMonitoringService {

    CompletableFuture<CovidMonitoringDto> getCovidMonitoringData(String country);
}
