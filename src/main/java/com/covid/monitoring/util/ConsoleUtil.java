package com.covid.monitoring.util;

import com.covid.monitoring.dto.CovidMonitoringDto;

import java.util.Scanner;

public class ConsoleUtil {

    private static final String COVID_MONITORING_RESULT_TEMPLATE = "confirmed: %s\nrecovered: %s\ndeaths: %s\nnew cases since last update: %s\nvaccinated level: %s";

    public static String readCountry() {

        System.out.print("Please enter the country name: ");

        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }

    public static void printResult(CovidMonitoringDto covidMonitoringDto) {
        System.out.printf(
                COVID_MONITORING_RESULT_TEMPLATE,
                covidMonitoringDto.getCovidCountryCases().getCovidCasesData().getConfirmed(),
                covidMonitoringDto.getCovidCountryCases().getCovidCasesData().getRecovered(),
                covidMonitoringDto.getCovidCountryCases().getCovidCasesData().getDeaths(),
                covidMonitoringDto.getCasesSinceLastData(),
                //This will round percentage to 2 digits after .
                String.format("%.4g%n", covidMonitoringDto.getVaccinationLevelPercentage()));
    }

    public static void printError(String error) {
        System.err.println(error);
    }
}
