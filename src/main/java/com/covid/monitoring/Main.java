package com.covid.monitoring;

import com.covid.monitoring.constant.ErrorConstants;
import com.covid.monitoring.dto.CovidMonitoringDto;
import com.covid.monitoring.service.CovidMonitoringService;
import com.covid.monitoring.service.impl.CovidMonitoringServiceImpl;
import com.covid.monitoring.util.ConsoleUtil;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class Main {

    private static final CovidMonitoringService covidMonitoringService = new CovidMonitoringServiceImpl();

    public static void main(String[] args) {

        while (true) {
            String country = ConsoleUtil.readCountry();

            //We don't accept null or empty country name
            if (Objects.isNull(country) || country.isEmpty()) {
                ConsoleUtil.printError(ErrorConstants.INVALID_COUNTRY);
                return;
            }

            CompletableFuture<CovidMonitoringDto> covidMonitoringDtoFuture = covidMonitoringService.getCovidMonitoringData(country);
            showResult(covidMonitoringDtoFuture);
        }
    }

    private static void showResult(CompletableFuture<CovidMonitoringDto> covidMonitoringDtoFuture) {
        try {
            CovidMonitoringDto covidMonitoringDto = covidMonitoringDtoFuture.get();

            if (Objects.nonNull(covidMonitoringDto.getError())) {
                ConsoleUtil.printError(covidMonitoringDto.getError());
            } else {
                ConsoleUtil.printResult(covidMonitoringDto);
            }
        } catch (InterruptedException | ExecutionException e) {
            ConsoleUtil.printError(ErrorConstants.GENERAL_ERROR);
        }
    }
}
