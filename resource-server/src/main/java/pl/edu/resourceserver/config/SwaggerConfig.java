package pl.edu.resourceserver.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.OAuthFlow;
import io.swagger.v3.oas.models.security.OAuthFlows;
import io.swagger.v3.oas.models.security.Scopes;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.edu.resourceserver.config.props.OpenApiProperties;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class SwaggerConfig {

    private final OpenApiProperties openApiProperties;

    @Bean
    public OpenAPI customOpenAPI() {

        return new OpenAPI().info(new Info()
                        .title("Library API Documentation")
                        .version("1.0")
                        .description("Provides REST endpoints for managing books in library system"))
                .servers(List.of(new Server().url(openApiProperties.getResourceServerUrl())
                        .description(openApiProperties.getServerDescription())))
                .components(new Components()
                        .addSecuritySchemes("oauth2", new SecurityScheme()
                                .type(SecurityScheme.Type.OAUTH2)
                                .flows(new OAuthFlows()
                                        .authorizationCode(new OAuthFlow()
                                                .authorizationUrl("http://localhost:9000/oauth2/authorize")
                                                .tokenUrl("http://localhost:9000/oauth2/token")
                                                .scopes(new Scopes()
                                                        .addString("read", "Read access")
                                                        .addString("openid", "OpenID Connect"))
                                        )
                                )
                        )
                )
                .addSecurityItem(new SecurityRequirement().addList("oauth2"));
    }
}
