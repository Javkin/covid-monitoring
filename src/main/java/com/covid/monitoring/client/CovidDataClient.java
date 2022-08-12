package com.covid.monitoring.client;

import com.covid.monitoring.dto.CovidCasesDto;
import com.covid.monitoring.dto.CovidHistoryDto;

import com.covid.monitoring.dto.CovidVaccineDto;
import retrofit2.http.Query;

import java.util.concurrent.CompletableFuture;

public interface CovidDataClient {

    CompletableFuture<CovidCasesDto> getCovidCases(@Query("country") String country);

    CompletableFuture<CovidVaccineDto> getCovidVaccines(@Query("country") String country);

    CompletableFuture<CovidHistoryDto> getCovidHistory(@Query("country") String country, @Query("status") String status);
}
