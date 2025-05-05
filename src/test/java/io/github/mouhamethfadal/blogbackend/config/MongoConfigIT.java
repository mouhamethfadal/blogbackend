package io.github.mouhamethfadal.blogbackend.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.context.scope.refresh.RefreshScopeRefreshedEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
class MongoConfigIT {
    @Autowired
    private ApplicationContext applicationContext;

    @MockitoBean
    MongoConfig mongoConfig;

    @Test
    void refreshEvent_ShouldTriggerOnRefresh() {

        // Arrange
        RefreshScopeRefreshedEvent event = new RefreshScopeRefreshedEvent();
        applicationContext.publishEvent(event);

        // Verify
        verify(mongoConfig, times(1)).onRefresh();

    }
}
