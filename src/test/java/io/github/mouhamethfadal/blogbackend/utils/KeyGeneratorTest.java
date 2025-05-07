package io.github.mouhamethfadal.blogbackend.utils;


import nl.altindag.log.LogCaptor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.util.Base64;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.Mockito.times;

class KeyGeneratorTest {
    @Nested
    @DisplayName("generateKey Test Cases")
    class generateKeyTest {
        @Test
        void generateKey_ShouldReturnBase64EncodedString()  {
            // Act
            String key = KeyGenerator.generateKey();

            // Assert
            assertThat(key).isNotEmpty();
            assertThatCode(() -> Base64.getDecoder().decode(key)).doesNotThrowAnyException();

        }

        @Test
        void generateKey_ShouldReturn44CharacterString()  {
            // Act
            String key = KeyGenerator.generateKey();

            // Assert
            assertThat(key).hasSize(44);

        }

        @RepeatedTest(6)
        void generateKey_ShouldReturnDifferentKeysForEachCall() {
            // Act
            String key1 = KeyGenerator.generateKey();
            String key2 = KeyGenerator.generateKey();

            // Assert
            assertThat(key1).isNotEqualTo(key2);
        }

        @Test
        void generateKey_DecodedKeyShouldBe32Bytes() {
            // Act
            String key = KeyGenerator.generateKey();

            // Assert
            assertThat(Base64.getDecoder().decode(key)).hasSize(32);
        }
    }

    @Nested
    @DisplayName("generateAndPrintKey Test Cases")
    class generateAndPrintKeyTest {
        @Test
        void generateAndPrintKey_ShouldCallGenerateKey() {
            // Arrange
            try(MockedStatic<KeyGenerator> mockedKeyGenerator = Mockito.mockStatic(KeyGenerator.class)) {
                mockedKeyGenerator.when(KeyGenerator::generateKey).thenReturn("random-base-64-key");
                mockedKeyGenerator.when(KeyGenerator::generateAndPrintKey).thenCallRealMethod();
                // Act
                KeyGenerator.generateAndPrintKey();

                // Verify
                mockedKeyGenerator.verify(KeyGenerator::generateKey, times(1));
            }

        }

        @Test
        void generateAndPrintKey_ShouldLogCorrectMessages() {
            // Arrange
          try(MockedStatic<KeyGenerator> mockedKeyGenerator = Mockito.mockStatic(KeyGenerator.class)) {
              // Arrange
              String mockKey = "random-base-64-key";
              mockedKeyGenerator.when(KeyGenerator::generateKey).thenReturn(mockKey);
              mockedKeyGenerator.when(KeyGenerator::generateAndPrintKey).thenCallRealMethod();

              //Act and Assert
              try (LogCaptor logCaptor = LogCaptor.forClass(KeyGenerator.class)) {
                  KeyGenerator.generateAndPrintKey();
                  assertThat(logCaptor.getInfoLogs())
                          .hasSize(5)
                          .contains( "\n===== JWT KEY GENERATOR =====",
                                  "Generated Key: " + mockKey,
                                  "Add this to your environment variables:",
                                  "JWT_SECRET=" + mockKey,
                                  "===============================\n");
              }


          }

        }
    }

    @Test
    void main_ShouldCallGenerateAndPrintKey() {
        try (MockedStatic<KeyGenerator> mockedKeyGenerator = Mockito.mockStatic(KeyGenerator.class)) {
            // Arrange
            mockedKeyGenerator.when(() -> KeyGenerator.main(Mockito.any())).thenCallRealMethod();
            mockedKeyGenerator.when(KeyGenerator::generateAndPrintKey).thenCallRealMethod();

            // Act
            KeyGenerator.main(new String[]{});

            // Verify
            mockedKeyGenerator.verify(KeyGenerator::generateAndPrintKey, times(1));
        }
    }


}
