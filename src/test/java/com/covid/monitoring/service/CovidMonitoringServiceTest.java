package com.covid.monitoring.service;

import com.covid.monitoring.client.CovidDataClient;
import com.covid.monitoring.constant.ErrorConstants;
import com.covid.monitoring.dto.CovidCasesDataDto;
import com.covid.monitoring.dto.CovidCasesDto;
import com.covid.monitoring.dto.CovidHistoryDataDto;
import com.covid.monitoring.dto.CovidHistoryDto;
import com.covid.monitoring.dto.CovidMonitoringDto;
import com.covid.monitoring.dto.CovidVaccineDataDto;
import com.covid.monitoring.dto.CovidVaccineDto;
import com.covid.monitoring.service.impl.CovidMonitoringServiceImpl;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith({MockitoExtension.class})
public class CovidMonitoringServiceTest {

    @Mock
    CovidDataClient covidDataClient;

    @InjectMocks
    CovidMonitoringServiceImpl covidMonitoringService;

    static final String TEST_COUNTRY = "Test country";
    static final String TEST_STATUS = "Confirmed";

    CovidCasesDto covidCasesDto = CovidCasesDto.builder()
            .covidCasesData(CovidCasesDataDto.builder()
                        .confirmed(1200000)
                        .deaths(15000)
                        .population(20000000)
                        .recovered(1000000)
                        .updated(LocalDateTime.now())
            .build())
            .build();

    @Test
    @SneakyThrows
    void whenGetCovidMonitoringDataThenGetSuccessResult() {
        Map<String, Integer> casesHistory = new HashMap<>();
        casesHistory.put(LocalDate.now().minusDays(1).toString(), 1182000);

        CovidHistoryDto covidHistoryDto = CovidHistoryDto.builder()
                .covidHistoryData(CovidHistoryDataDto.builder()
                        .dates(casesHistory)
                        .build())
                .build();

        CovidVaccineDto covidVaccineDto = CovidVaccineDto.builder()
                .covidVaccineData(CovidVaccineDataDto.builder()
                        .peopleVaccinated(14000000)
                        .build())
                .build();

        when(covidDataClient.getCovidCases(TEST_COUNTRY)).thenReturn(CompletableFuture.completedFuture(covidCasesDto));
        when(covidDataClient.getCovidHistory(TEST_COUNTRY, TEST_STATUS)).thenReturn(CompletableFuture.completedFuture(covidHistoryDto));
        when(covidDataClient.getCovidVaccines(TEST_COUNTRY)).thenReturn(CompletableFuture.completedFuture(covidVaccineDto));

        CovidMonitoringDto monitoringDto = covidMonitoringService.getCovidMonitoringData(TEST_COUNTRY).get();

        assertNotNull(monitoringDto);
        assertEquals(monitoringDto.getCovidCountryCases().getCovidCasesData().getConfirmed(), covidCasesDto.getCovidCasesData().getConfirmed());
        assertEquals(monitoringDto.getCovidCountryCases().getCovidCasesData().getDeaths(), covidCasesDto.getCovidCasesData().getDeaths());
        assertEquals(monitoringDto.getCovidCountryCases().getCovidCasesData().getPopulation(), covidCasesDto.getCovidCasesData().getPopulation());
        assertEquals(monitoringDto.getCovidCountryCases().getCovidCasesData().getRecovered(), covidCasesDto.getCovidCasesData().getRecovered());

        double expectedVaccinedLevelPercentage = (double) covidVaccineDto.getCovidVaccineData().getPeopleVaccinated() * 100 / covidCasesDto.getCovidCasesData().getPopulation();

        assertEquals(monitoringDto.getVaccinationLevelPercentage(), expectedVaccinedLevelPercentage);
    }

    @Test
    @SneakyThrows
    void whenGetCovidMonitoringDataThenCovidCasesIsNullThenError() {
        when(covidDataClient.getCovidCases(TEST_COUNTRY)).thenReturn(CompletableFuture.completedFuture(null));

        CompletableFuture<CovidMonitoringDto> monitoringDto = covidMonitoringService.getCovidMonitoringData(TEST_COUNTRY);

        assertNotNull(monitoringDto.get().getError());
        assertEquals(ErrorConstants.INVALID_COUNTRY, monitoringDto.get().getError());
    }

    @Test
    @SneakyThrows
    void whenGetCovidMonitoringDataThenCovidHistoryIsNullThenError() {
        when(covidDataClient.getCovidCases(TEST_COUNTRY)).thenReturn(CompletableFuture.completedFuture(covidCasesDto));
        when(covidDataClient.getCovidHistory(TEST_COUNTRY, TEST_STATUS)).thenReturn(CompletableFuture.completedFuture(null));

        CompletableFuture<CovidMonitoringDto> monitoringDto = covidMonitoringService.getCovidMonitoringData(TEST_COUNTRY);

        assertNotNull(monitoringDto.get().getError());
        assertEquals(ErrorConstants.GENERAL_ERROR, monitoringDto.get().getError());
    }

    @Test
    @SneakyThrows
    void whenGetCovidMonitoringDataThenCovidVaccinesIsNullThenError() {
        when(covidDataClient.getCovidCases(TEST_COUNTRY)).thenReturn(CompletableFuture.completedFuture(covidCasesDto));
        when(covidDataClient.getCovidVaccines(TEST_COUNTRY)).thenReturn(CompletableFuture.completedFuture(null));

        CompletableFuture<CovidMonitoringDto> monitoringDto = covidMonitoringService.getCovidMonitoringData(TEST_COUNTRY);

        assertNotNull(monitoringDto.get().getError());
        assertEquals(ErrorConstants.GENERAL_ERROR, monitoringDto.get().getError());
    }
}
