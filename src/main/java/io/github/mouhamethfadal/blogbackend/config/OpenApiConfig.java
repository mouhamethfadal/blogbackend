package io.github.mouhamethfadal.blogbackend.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.examples.Example;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.responses.ApiResponse;
import lombok.Generated;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;

import java.util.ArrayList;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Blog API",
                version = "1.0",
                description = "API documentation for Blog Backend services",
                contact = @Contact(
                        name = "Fadal",
                        email = "mouhamethfadalaidara@gmail.com",
                        url = "https://github.com/mouhamethfadal"
                ),
                license = @License(
                        name = "MIT License",
                        url = "https://opensource.org/licenses/MIT"
                )
        ),
        servers = {
                @Server(
                        description = "Local Environment",
                        url = "http://blog.localhost"
                ),
                @Server(
                        description = "Production Environment",
                        url = "https://api.your-domain.com"
                )
        },
        security = {
                @SecurityRequirement(name = "bearerAuth")
        }
)
@SecurityScheme(
        name = "bearerAuth",
        description = "JWT authentication. Enter the token:",
        scheme = "bearer",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER
)

@Slf4j
@Generated
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .addSecurityItem(new io.swagger.v3.oas.models.security.SecurityRequirement().addList("bearerAuth"))
                .components(new io.swagger.v3.oas.models.Components()
                        .addResponses("BadRequest", createApiResponse("Bad request due to invalid input", "Invalid request"))
                        .addResponses("Conflict", createApiResponse("Resource already exists", "Conflict with existing resource"))
                        .addResponses("Unauthorized", createApiResponse("Authentication failed", "Unauthorized"))
                        .addResponses("Forbidden", createApiResponse("Insufficient permissions", "Forbidden"))
                        .addResponses("NotFound", createApiResponse("Resource not found", "Not Found"))
                        .addResponses("ServerError", createApiResponse("Server encountered an error", "Server Error")));
    }

    private ApiResponse createApiResponse(String description, String message) {
        return new ApiResponse()
                .description(description)
                .content(new Content().addMediaType(MediaType.APPLICATION_JSON_VALUE,
                        new io.swagger.v3.oas.models.media.MediaType().addExamples("default",
                                new Example().value("{ \"message\": \"" + message + "\" }"))));
    }

    @Bean
    public OpenApiCustomizer publicPathsCustomizer() {
        return openApi -> {
            if (openApi.getPaths() != null) {
                openApi.getPaths().forEach((path, pathItem) -> {
                    if (path.startsWith("/api/v1/auth")) {
                        pathItem.readOperations().forEach(operation ->
                                operation.setSecurity(new ArrayList<>())
                        );
                    }
                });
            }
        };
    }

}
