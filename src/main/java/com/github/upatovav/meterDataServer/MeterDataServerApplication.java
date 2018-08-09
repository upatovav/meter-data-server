package com.github.upatovav.meterDataServer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(considerNestedRepositories = true)
@EnableCaching
public class MeterDataServerApplication {

	//TODO any security here?

	@Bean
	public CacheManager cacheManager() {
		return new ConcurrentMapCacheManager(
				"meterDataByUser", "lastMeterDataByUser");
	}

	public static void main(String[] args) {
		SpringApplication.run(MeterDataServerApplication.class, args);
	}
}
