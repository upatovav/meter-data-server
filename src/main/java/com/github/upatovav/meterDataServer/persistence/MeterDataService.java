package com.github.upatovav.meterDataServer.persistence;


import com.github.upatovav.meterDataServer.controllers.MeterDataDto;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class MeterDataService {

    private final MeterData.MeterDataRepository meterDataRepository;

    MeterDataService(MeterData.MeterDataRepository meterDataRepository){
        this.meterDataRepository = meterDataRepository;
    }

    @CacheEvict(value = {"meterDataByUser", "lastMeterDataByUser"}, key = "#userId")
    public void saveMeterData(long userId, MeterDataDto meterDataDto){
        meterDataRepository.save(new MeterData(userId, ZonedDateTime.now(), meterDataDto));
    }

    @Cacheable(value = "meterDataByUser", key = "#userId")
    public List<MeterData> getMeterData(long userId){
        return meterDataRepository.findAllByUserId(userId);
    }

    @Cacheable(value = "lastMeterDataByUser", key = "#userId")
    public Optional<MeterData> getLastMeterDataForUser(long userId){
         return meterDataRepository.findFirstByUserIdOrderByDateDesc(userId);
    }
}
