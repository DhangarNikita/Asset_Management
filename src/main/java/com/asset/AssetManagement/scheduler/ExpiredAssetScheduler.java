package com.asset.AssetManagement.scheduler;

import com.asset.AssetManagement.service.ExpiredAssetService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
public class ExpiredAssetScheduler {

    private static final Logger logger = LoggerFactory.getLogger(ExpiredAssetScheduler.class);
    @Autowired
    private ExpiredAssetService expiredAssetService;

    @Scheduled(cron = "0 0 0 * * ?")
    public void run(){
        expiredAssetService.moveExpiredAssets();
        logger.info("Expired assets moved successfully.");
    }
}
