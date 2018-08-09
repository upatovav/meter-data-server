package com.github.upatovav.meterDataServer.controllers;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@Builder
@JsonIgnoreProperties
public class MeterDataDto {

    @NotNull
    @Min(value = 0)
    private BigDecimal coldWater;

    @NotNull
    @Min(value = 0)
    private BigDecimal hotWater;

    @NotNull
    @Min(value = 0)
    private BigDecimal gas;

}
