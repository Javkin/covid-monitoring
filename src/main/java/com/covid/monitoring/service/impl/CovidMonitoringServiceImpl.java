package com.covid.monitoring.service.impl;

import com.covid.monitoring.client.CovidDataClient;
import com.covid.monitoring.client.factory.CovidClientFactory;
import com.covid.monitoring.constant.CovidMonitoringConstants;
import com.covid.monitoring.constant.ErrorConstants;
import com.covid.monitoring.dto.CovidCasesDto;
import com.covid.monitoring.dto.CovidHistoryDto;
import com.covid.monitoring.dto.CovidMonitoringDto;
import com.covid.monitoring.dto.CovidVaccineDto;
import com.covid.monitoring.service.CovidMonitoringService;
import com.covid.monitoring.util.DateUtil;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class CovidMonitoringServiceImpl implements CovidMonitoringService {

    private final CovidDataClient covidDataClient;

    public CovidMonitoringServiceImpl() {
        covidDataClient = new CovidClientFactory().getClient();
    }

    /**
     * Declared this constructor for mocking CovidDataClient
     */
    public CovidMonitoringServiceImpl(CovidDataClient covidDataClient) {
        this.covidDataClient = covidDataClient;
    }

    @Override
    public CompletableFuture<CovidMonitoringDto> getCovidMonitoringData(String country) {
        return covidDataClient.getCovidCases(country)
                .thenApply(data -> {
                    CovidMonitoringDto.CovidMonitoringDtoBuilder covidMonitoringDtoBuilder = CovidMonitoringDto.builder();

                    if (Objects.isNull(data)) {
                        return setErrorAndReturn(data, covidMonitoringDtoBuilder);
                    }

                    covidMonitoringDtoBuilder.covidCountryCases(data);

                    CompletableFuture<Void> covidVaccineCases = getCovidVaccines(country, data, covidMonitoringDtoBuilder);
                    CompletableFuture<Void> covidHistory = getCovidHistory(country, CovidMonitoringConstants.COVID_CONFIRMED_STATUS, data, covidMonitoringDtoBuilder);

                    if (Objects.isNull(covidVaccineCases) || Objects.isNull(covidHistory)) {
                        return setErrorAndReturn(data, covidMonitoringDtoBuilder);
                    }

                    //Here we collect all futures together for executing them in parallel
                    CompletableFuture<Void> completableFuture = CompletableFuture.allOf(covidVaccineCases, covidHistory);

                    try {
                        //This will execute getCovidVaccines and getCovidHistory in parallel
                        completableFuture.get();
                    } catch (InterruptedException | ExecutionException e) {
                        setError(data, covidMonitoringDtoBuilder);
                    }

                    return covidMonitoringDtoBuilder.build();
                });
    }

    private CompletableFuture<Void> getCovidVaccines(String country,
                                                     CovidCasesDto covidCasesDto,
                                                     CovidMonitoringDto.CovidMonitoringDtoBuilder covidMonitoringDtoBuilder) {

        CompletableFuture<CovidVaccineDto> covidVaccineDtoFuture = covidDataClient.getCovidVaccines(country);

        if (Objects.isNull(covidVaccineDtoFuture)) {
            return null;
        }

        return covidVaccineDtoFuture
                .thenAccept(vaccineData ->
                        covidMonitoringDtoBuilder.vaccinationLevelPercentage(
                                calculateVaccineLevelPercentage(covidCasesDto, vaccineData.getCovidVaccineData().getPeopleVaccinated())));
    }

    private CompletableFuture<Void> getCovidHistory(String country,
                                                    String status,
                                                    CovidCasesDto covidCasesDto,
                                                    CovidMonitoringDto.CovidMonitoringDtoBuilder covidMonitoringDtoBuilder) {

        CompletableFuture<CovidHistoryDto> covidHistoryDtoFuture = covidDataClient.getCovidHistory(country, status);

        if (Objects.isNull(covidHistoryDtoFuture)) {
            return null;
        }

        return covidHistoryDtoFuture
                .thenAccept(historyData ->
                        covidMonitoringDtoBuilder
                                .casesSinceLastData(calculateNewCasesSinceLastUpdate(historyData, covidCasesDto)));
    }

    private void setError(CovidCasesDto covidCasesDto, CovidMonitoringDto.CovidMonitoringDtoBuilder covidMonitoringDtoBuilder) {
        if (Objects.isNull(covidCasesDto) || Objects.isNull(covidCasesDto.getCovidCasesData())) {
            covidMonitoringDtoBuilder.error(ErrorConstants.INVALID_COUNTRY);
        } else {
            covidMonitoringDtoBuilder.error(ErrorConstants.GENERAL_ERROR);
        }
    }

    private double calculateVaccineLevelPercentage(CovidCasesDto covidCasesDto, int peopleVaccinated) {
        return (double) peopleVaccinated * 100 / covidCasesDto.getCovidCasesData().getPopulation();
    }

    private int calculateNewCasesSinceLastUpdate(CovidHistoryDto covidHistoryDto, CovidCasesDto covidCasesDto) {
        int lastCases = covidHistoryDto
                .getCovidHistoryData()
                .getDates()
                .get(DateUtil.getPreviousDayInFormat(
                        covidCasesDto.getCovidCasesData().getUpdated()));

        return covidCasesDto.getCovidCasesData().getConfirmed() - lastCases;
    }

    private CovidMonitoringDto setErrorAndReturn(CovidCasesDto covidCasesDto, CovidMonitoringDto.CovidMonitoringDtoBuilder covidMonitoringDtoBuilder) {
        setError(covidCasesDto, covidMonitoringDtoBuilder);
        return covidMonitoringDtoBuilder.build();
    }
}
