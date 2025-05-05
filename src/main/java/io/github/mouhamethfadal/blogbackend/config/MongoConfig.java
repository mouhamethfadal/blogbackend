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

@Configuration
@ConfigurationProperties(prefix = "spring.data.mongodb")
@Slf4j
@Getter
@Setter
@RefreshScope
public class MongoConfig {
    private String uri;

    @PostConstruct
    public void logMongoSettings() {
      if(uri == null) {
          log.warn("MongoDB URI is null");
          return;
      }

      log.info("MongoDB Configurations:");

      try {
          String maskedUri = getMaskedUri();
          log.info("URI: {}", maskedUri);

          int lastSlashIndex = uri.lastIndexOf("/");

          String dbName = getDbName(lastSlashIndex);
          log.info("Database name: {}", dbName);
      } catch(Exception e) {
          log.warn("Fail to parse MongoDB URI: {}", e.getMessage());
      }

    }

    private String getDbName(int lastSlashIndex) {
        return lastSlashIndex >= 0 && lastSlashIndex < uri.length() - 1 ? uri.substring(lastSlashIndex + 1) : "";
    }

    protected String getMaskedUri() {
        return uri.replaceAll("://[^:]*:[^@]*@", "://*****:*****@");
    }

    @EventListener(RefreshScopeRefreshedEvent.class)
    public void onRefresh() {
        logMongoSettings();
    }
}
