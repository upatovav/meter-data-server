package com.github.upatovav.meterDataServer.persistence;

import com.github.upatovav.meterDataServer.controllers.MeterDataDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;

import static org.mockito.BDDMockito.times;
import static org.mockito.BDDMockito.verify;

@RunWith(SpringRunner.class)
@ContextConfiguration
public class MeterDataServiceTest {

    @TestConfiguration
    @EnableCaching
    static class MeterDataServiceTestConfiguration {

        @Bean
        public CacheManager cacheManager() {
            return new ConcurrentMapCacheManager(
                    "meterDataByUser", "lastMeterDataByUser");
        }

        @MockBean
        MeterData.MeterDataRepository meterDataRepository;

        @Bean
        public MeterDataService meterDataService(){
            return new MeterDataService(meterDataRepository);
        }
    }

    @Autowired
    MeterDataService meterDataService;

    @Autowired
    MeterData.MeterDataRepository meterDataRepositoryMock;

    @Test
    public void testCacheEvict(){
        long userId = 100500L;
        MeterDataDto dto = MeterDataDto.builder()
                .coldWater(new BigDecimal(100))
                .hotWater(new BigDecimal(101))
                .gas(new BigDecimal(10234))
                .build();
        //retrieve [absent] data
        meterDataService.getMeterData(userId);
        meterDataService.getLastMeterDataForUser(userId);
        //repository called once
        verify(meterDataRepositoryMock).findAllByUserId(userId);
        verify(meterDataRepositoryMock).findFirstByUserIdOrderByDateDesc(userId);

        //retrieve data once again
        meterDataService.getMeterData(userId);
        meterDataService.getLastMeterDataForUser(userId);
        //repository still called once
        verify(meterDataRepositoryMock).findAllByUserId(userId);
        verify(meterDataRepositoryMock).findFirstByUserIdOrderByDateDesc(userId);

        //save record
        meterDataService.saveMeterData(userId, dto);

        //read data
        meterDataService.getMeterData(userId);
        meterDataService.getLastMeterDataForUser(userId);

        //repository called twice, so cache evicted after saving data
        verify(meterDataRepositoryMock, times(2)).findAllByUserId(userId);
        verify(meterDataRepositoryMock, times(2)).findFirstByUserIdOrderByDateDesc(userId);
    }
}
