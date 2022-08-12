package com.covid.monitoring.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CovidMonitoringDto {

    private CovidCasesDto covidCountryCases;
    private double vaccinationLevelPercentage;
    private int casesSinceLastData;
    private String error;
}
