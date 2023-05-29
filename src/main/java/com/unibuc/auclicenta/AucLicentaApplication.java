package com.unibuc.auclicenta;

import com.unibuc.auclicenta.service.ListingService;
import org.jobrunr.scheduling.JobScheduler;
import org.jobrunr.scheduling.cron.Cron;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;

@SpringBootApplication
public class AucLicentaApplication {
    @Autowired
    private JobScheduler jobScheduler;
    @Autowired
    private ListingService listingService;

    public static void main(String[] args) {
        SpringApplication.run(AucLicentaApplication.class, args);
    }

    @PostConstruct
    public void backgroundTasks() {
        jobScheduler.scheduleRecurrently(Cron.every30seconds(), () -> listingService.activateListing());
        jobScheduler.scheduleRecurrently(Cron.every30seconds(), () -> listingService.deactivateListing());
    }
}
