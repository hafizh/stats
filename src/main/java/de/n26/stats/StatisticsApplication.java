package de.n26.stats;

import de.n26.stats.services.StatisticsSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@EnableAsync
@EnableScheduling
@SpringBootApplication
public class StatisticsApplication {

	private static Logger logger = LoggerFactory.getLogger(StatisticsApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(StatisticsApplication.class, args);
	}

	@Bean
	public StatisticsSource statisticsSource() {
		return new StatisticsSource();
	}

	@Bean
	public Executor taskExecutor() {
		logger.info("Creating Async Task Executor");
		final ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(10);
		executor.setMaxPoolSize(20);
		executor.setQueueCapacity(100);
		executor.setThreadNamePrefix("StatsThread-");
		executor.initialize();
		return executor;
	}
}
