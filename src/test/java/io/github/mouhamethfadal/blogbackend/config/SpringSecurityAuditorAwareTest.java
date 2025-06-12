package io.github.mouhamethfadal.blogbackend.config;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mock.Strictness.LENIENT;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SpringSecurityAuditorAwareTest {
    @Mock(strictness = LENIENT)
    private SecurityContext securityContext;
    @Mock
    private Authentication authentication;

    @InjectMocks
    private SpringSecurityAuditorAware springSecurityAuditorAware;

    private String executeWithMockedSecurityContext(Supplier<String> testAction) {
        try (MockedStatic<SecurityContextHolder> mockedSecurityContextHolder = Mockito.mockStatic(SecurityContextHolder.class)) {
            mockedSecurityContextHolder.when(SecurityContextHolder::getContext).thenReturn(securityContext);

            return testAction.get();
        }
    }
    @Nested
    @DisplayName("Test cases where 'System' should be the auditor")
    class SystemAuditorTest {
        private static final String SYSTEM_AUDITOR = "System";
        @Test
        void getCurrenAuditor_WhenAuthenticationIsNull_ShouldReturnSystem() {
            //
            when(securityContext.getAuthentication()).thenReturn(null);

            String auditor = executeWithMockedSecurityContext(() -> springSecurityAuditorAware.getCurrentAuditor().orElse(null));

            assertThat(auditor).isEqualTo(SYSTEM_AUDITOR);

        }

        @Test
        void getCurrentAuditor_WhenAuthenticationIsAnonymous_ShouldReturnSystem() {

            // Arrange
            AnonymousAuthenticationToken anonymousAuthenticationToken = new AnonymousAuthenticationToken(
                    "anonymous",
                    "anonymous",
                    List.of(new SimpleGrantedAuthority("ROLE_ANONYMOUS"))
            );
            when(securityContext.getAuthentication()).thenReturn(anonymousAuthenticationToken);

            String auditor = executeWithMockedSecurityContext(() -> springSecurityAuditorAware.getCurrentAuditor().orElse(null));

            assertThat(auditor).isEqualTo(SYSTEM_AUDITOR);
        }
    }

    @Nested
    @DisplayName("Test cases where auditor is not System")
    class NotSystemAuditorTest{
        @Test
        void getCurrentAuditor_WhenAuthenticationIsValid_ShouldReturnAuditor() {
            // Arrange
            String expectedAuditor = "john_doe";
            when(securityContext.getAuthentication()).thenReturn(authentication);
            when(authentication.getName()).thenReturn(expectedAuditor);

            // Act & Assert

            String auditor = executeWithMockedSecurityContext(() -> springSecurityAuditorAware.getCurrentAuditor().orElse(null));

            assertThat(auditor).isEqualTo(expectedAuditor);

        }
    }
}
