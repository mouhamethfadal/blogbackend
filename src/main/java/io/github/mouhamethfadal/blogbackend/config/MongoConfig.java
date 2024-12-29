package io.github.mouhamethfadal.blogbackend.config;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.context.scope.refresh.RefreshScopeRefreshedEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories
@ConfigurationProperties(prefix = "spring.data.mongodb")
@Slf4j
@Getter
@Setter
@RefreshScope
public class MongoConfig {
    private String uri;

    @PostConstruct
    public void logMongoSettings() {
        log.info("MongoDB Configurations:");
        String maskedUri = uri.replaceAll("://[^:]*:[^@]*@", "://*****:*****@");
        log.info("URI: {}", maskedUri);
        String dbName = uri.substring(uri.lastIndexOf("/") + 1);
        log.info("Database name: {}", dbName);
    }

    @EventListener(RefreshScopeRefreshedEvent.class)
    public void onRefresh() {
        logMongoSettings();
    }
}
