package com.github.upatovav.meterDataServer.persistence;

import com.github.upatovav.meterDataServer.controllers.MeterDataDto;
import lombok.Data;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

@Data
@Entity
public class MeterData {

    @Id
    @GeneratedValue
    private Long id;

    @Column
    @NotNull
    private Long userId;

    @Column
    private ZonedDateTime date;

    @Column
    private BigDecimal coldWater;

    @Column
    private BigDecimal hotWater;

    @Column
    private BigDecimal gas;

    MeterData(long userId,
                     ZonedDateTime date,
                     MeterDataDto dto){
        this.userId = userId;
        this.date = date;
        this.coldWater = dto.getColdWater();
        this.hotWater = dto.getHotWater();
        this.gas = dto.getGas();
    }

    interface MeterDataRepository extends JpaRepository<MeterData, Long> {
        List<MeterData> findAllByUserId(long userId);
        Optional<MeterData> findFirstByUserIdOrderByDateDesc(long userId);
    }
}
