package io.github.mouhamethfadal.blogbackend.config;

import nl.altindag.log.LogCaptor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.aggregator.ArgumentsAccessor;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MongoConfigTest {
    @InjectMocks
    private MongoConfig mongoConfig;

    private LogCaptor logCaptor;

    @BeforeEach
    void setUp() {
        logCaptor = LogCaptor.forClass(MongoConfig.class);
    }

    @AfterEach
    void tearDown() {
        if(logCaptor != null) {
            logCaptor.close();
        }
    }

    @Test
    void logMongoSettings_WhenUriIsNull_ShouldNotLog() {
        // Arrange
        mongoConfig.setUri(null);

        // Act
        mongoConfig.logMongoSettings();

        // Assert
        assertThat(logCaptor.getInfoLogs()).isEmpty();
        assertThat(logCaptor.getWarnLogs()).hasSize(1);
        assertThat(logCaptor.getWarnLogs().getFirst()).isEqualTo("MongoDB URI is null");

    }

    @ParameterizedTest(name ="#{index} - Test with uri: {0}" )
    @MethodSource("provideUriTestCases")
    void logMongoSettings_WithVariousUris_ShouldLogAppropriately(ArgumentsAccessor args){

        // Arrange
        mongoConfig.setUri(args.getString(0));

        // Act
        mongoConfig.logMongoSettings();

        // Assert
        assertThat(logCaptor.getInfoLogs()).hasSize(3);
        assertThat(logCaptor.getInfoLogs().getFirst()).isEqualTo("MongoDB Configurations:");
        assertThat(logCaptor.getInfoLogs().get(1)).isEqualTo("URI: " + args.getString(1));
        assertThat(logCaptor.getInfoLogs().get(2)).isEqualTo("Database name: " + args.getString(2));

    }


    private static Stream<Arguments> provideUriTestCases() {
        return Stream.of(
                Arguments.of("", "", ""),
                Arguments.of("mongodb://userTest:passer@localhost:27017/",
                        "mongodb://*****:*****@localhost:27017/", ""),
                Arguments.of("mongodb://userTest:passer@localhost:27017/testDb",
                        "mongodb://*****:*****@localhost:27017/testDb", "testDb")
        );
    }

    @Test
    void logMongoSettings_WhenExceptionThrown_ShouldLog() {
       // Arrange
        MongoConfig spyConfig = spy(mongoConfig);
        spyConfig.setUri("mongodb://userTest:passer@localhost:27017/testDb");
        doThrow(new RuntimeException("Simulated error"))
                .when(spyConfig).getMaskedUri();

        // Act
        spyConfig.logMongoSettings();

        // Assert
        assertThat(logCaptor.getInfoLogs()).hasSize(1);
        assertThat(logCaptor.getInfoLogs().getFirst()).isEqualTo("MongoDB Configurations:");
        assertThat(logCaptor.getWarnLogs()).hasSize(1);
        assertThat(logCaptor.getWarnLogs().getFirst()).contains("Fail to parse MongoDB URI");
    }

    @Test
    void onRefresh_ShouldCallLogMongoSettings() {
        // Arrange
        MongoConfig spyConfig = spy(mongoConfig);

        // Act
        spyConfig.onRefresh();

        // Verify
        verify(spyConfig, times(1)).logMongoSettings();
    }
}
