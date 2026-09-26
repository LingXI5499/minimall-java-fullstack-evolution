package com.lingxi.minimall.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/** Clock → @Scheduled → Service：这条链不经过浏览器和 Controller。 */
@Component
public class OrderExpiryTask {
    private static final Logger log = LoggerFactory.getLogger(OrderExpiryTask.class);
    private final OrderExpiryService service;
    public OrderExpiryTask(OrderExpiryService service) { this.service = service; }

    @Scheduled(fixedDelayString = "${app.orders.expiry-scan-ms:60000}")
    public void scan() {
        int count = service.closeExpired();
        if (count > 0) log.info("Scheduled task closed {} expired orders", count);
    }
}
