package com.rbouaro.notificationatscale;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Disabled("Requires docker compose (postgres + kafka) to be running")
class NotificationAtScaleApplicationTests {

    @Test
    void contextLoads() {
    }
}
