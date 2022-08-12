package com.covid.monitoring.dto;

import com.covid.monitoring.util.DateUtil;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.Objects;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CovidCasesDto {

    @JsonProperty("All")
    private CovidCasesDataDto covidCasesData;

    /**
     * Here we use anySetter for cases when updated field doesn't come in "All" key (For example United Kingdom)
     * In this case we should get updated field from one of the other keys
     */
    @JsonAnySetter
    public void anySetter(String key, Map<String, Object> data) {

        if (Objects.nonNull(covidCasesData) && Objects.isNull(covidCasesData.getUpdated())) {
            if (data.containsKey("updated")) {
                covidCasesData.setUpdated(DateUtil.parseUpdatedField(String.valueOf(data.get("updated"))));
            }
        }
    }
}
