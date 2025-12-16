package com.asset.AssetManagement.scheduler;

import com.asset.AssetManagement.constants.Constants;
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
    private final ExpiredAssetService expiredAssetService;

    @Autowired
    public ExpiredAssetScheduler(ExpiredAssetService expiredAssetService) {
        this.expiredAssetService = expiredAssetService;
    }

    @Scheduled(cron = Constants.EXPIRED_ASSET_CRON)
    public void run(){
        expiredAssetService.moveExpiredAssets();
        logger.info(Constants.EXPIRED_ASSET);
    }
}
