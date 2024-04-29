package org.loong.job;

import org.springframework.stereotype.Component;

@Component
public class TestJob {
//    @Scheduled(cron = "0/5 * * * * ?")
    public void testjob() {
        System.out.println("testjob");
    }
}
