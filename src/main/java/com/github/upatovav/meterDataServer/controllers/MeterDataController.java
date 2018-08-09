package com.github.upatovav.meterDataServer.controllers;

import com.github.upatovav.meterDataServer.persistence.MeterData;
import com.github.upatovav.meterDataServer.persistence.MeterDataService;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

@RestController
public class MeterDataController {

    private MeterDataService meterDataService;

    public MeterDataController(MeterDataService meterDataService){
        this.meterDataService = meterDataService;
    }

    @PostMapping("/{userId}/submitMeterData")
    void postMeterData(@PathVariable Long userId,
                       @RequestBody @Valid MeterDataDto meterDataDto,
                       BindingResult bindingResult) throws BindException {
        Optional<MeterData> lastMeterDataForUser = meterDataService.getLastMeterDataForUser(userId);
        lastMeterDataForUser.ifPresent(meterData -> checkData(meterData, meterDataDto, bindingResult));
        if (bindingResult.getErrorCount() > 0){
            throw new BindException(bindingResult);
        }
        meterDataService.saveMeterData(userId, meterDataDto);
    }

    @GetMapping("/{userId}/meterData")
    List<MeterData> getMeterData(@PathVariable Long userId){
        return meterDataService.getMeterData(userId);
    }

    private void checkData(MeterData meterData, MeterDataDto meterDataDto, BindingResult bindingResult) {
        if (meterData.getColdWater().compareTo(meterDataDto.getColdWater()) > 0) {
            bindingResult.rejectValue("coldWater", "Min",
                    getMinErrorMessage(meterData.getColdWater().toString()));
        }
        if (meterData.getHotWater().compareTo(meterDataDto.getHotWater()) > 0) {
            bindingResult.rejectValue("hotWater", "Min",
                    getMinErrorMessage(meterData.getHotWater().toString()));
        }
        if (meterData.getElectricity().compareTo(meterDataDto.getElectricity()) > 0) {
            bindingResult.rejectValue("electricity", "Min",
                    getMinErrorMessage(meterData.getElectricity().toString()));
        }
    }

    //TODO maybe there is better options than format string
    private String getMinErrorMessage(String value) {
        return String.format(
                ResourceBundle.getBundle("org.hibernate.validator.ValidationMessages")
                    .getString("javax.validation.constraints.Min.message")
                        .replace("{value}", "%s"), value);
    }

}
