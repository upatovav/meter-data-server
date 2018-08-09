package com.github.upatovav.meterDataServer.controllers;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;

@Data
@Builder
public class MeterDataDto {

    @NotNull
    @Min(value = 0)
    private BigDecimal coldWater;

    @NotNull
    @Min(value = 0)
    private BigDecimal hotWater;

    @NotNull
    @Min(value = 0)
    private BigDecimal electricity;

}
