package com.github.upatovav.meterDataServer.controllers;

import com.github.upatovav.meterDataServer.persistence.MeterData;
import com.github.upatovav.meterDataServer.persistence.MeterDataService;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

@RestController
public class MeterDataController {

    private final MeterDataService meterDataService;

    public MeterDataController(MeterDataService meterDataService){
        this.meterDataService = meterDataService;
    }

    @PostMapping("/{userId}/submitMeterData")
    void postMeterData(@PathVariable Long userId,
                       @RequestBody @Valid MeterDataDto meterDataDto,
                       BindingResult bindingResult) throws BindException {
        //TODO in case of adding security it is possible to check more "natural" way as in validation context we shall know userId
        Optional<MeterData> lastMeterDataForUser = meterDataService.getLastMeterDataForUser(userId);
        if (lastMeterDataForUser.isPresent() ) {
            checkData(lastMeterDataForUser.get(), meterDataDto, bindingResult);
        }
        if (bindingResult.getErrorCount() > 0){
            throw new BindException(bindingResult);
        }
        meterDataService.saveMeterData(userId, meterDataDto);
    }

    @GetMapping("/{userId}/meterData")
    List<MeterData> getMeterData(@PathVariable Long userId){
        return meterDataService.getMeterData(userId);
    }

    /**
     * Check new data against last submitted for this user. We assert that meter values never decrease
     * @param lastMeterData last value to check against
     * @param newMeterDataDto new value from user
     * @param bindingResult binding result to add error to
     */
    private void checkData(MeterData lastMeterData, MeterDataDto newMeterDataDto, BindingResult bindingResult) {
        checkField(bindingResult, lastMeterData.getColdWater(), newMeterDataDto.getColdWater(), "coldWater");
        checkField(bindingResult, lastMeterData.getHotWater(), newMeterDataDto.getHotWater(), "hotWater");
        checkField(bindingResult, lastMeterData.getGas(), newMeterDataDto.getGas(), "gas");
    }

    /**
     * Check that new value is more or equal than old, else add error to binding result
     */
    private void checkField(
            BindingResult bindingResult,
            BigDecimal oldValue,
            BigDecimal newValue,
            String fieldName) {

        if (newValue == null) return;

        if (oldValue.compareTo(newValue) > 0) {
            //TODO all static validation errors should be stripped for consistency
            bindingResult.rejectValue(fieldName, "Min", getMinErrorMessage(oldValue.toString()));
        }
    }

    //TODO maybe there is better options than format string?
    private String getMinErrorMessage(String value) {
        //TODO weird stuff: on idea ultimate system locale had been overridden by spring settings,
        // on  community it was still russian, so now it is fixed to english
        return String.format(
                ResourceBundle.getBundle("org.hibernate.validator.ValidationMessages", Locale.ENGLISH)
                    .getString("javax.validation.constraints.Min.message")
                        .replace("{value}", "%s"), value);
    }

}
