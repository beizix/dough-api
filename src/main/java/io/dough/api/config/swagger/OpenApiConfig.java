package io.dough.api.config.swagger;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

  private final String securitySchemeName = "bearerAuth";

  @Bean
  public OpenAPI openAPI() {
    return new OpenAPI()
        .info(new Info().title("Dough API").version("v1").description("Dough API 명세서"));
  }

  @Bean
  public GroupedOpenApi publicApi() {
    return GroupedOpenApi.builder()
        .group("1. Public API")
        .pathsToMatch("/api/v1/auth/**", "/api/v1/signup/user")
        .build();
  }

  @Bean
  public GroupedOpenApi commonApi() {
    return GroupedOpenApi.builder()
        .group("2. Shared API")
        .pathsToMatch("/api/v1/upload/**")
        .addOpenApiCustomizer(this::addSecurityItem)
        .build();
  }

  @Bean
  public GroupedOpenApi userApi() {
    return GroupedOpenApi.builder()
        .group("3. User API")
        .pathsToMatch("/api/v1/user/**")
        .addOpenApiCustomizer(this::addSecurityItem)
        .build();
  }

  @Bean
  public GroupedOpenApi managerApi() {
    return GroupedOpenApi.builder()
        .group("4. Manager API")
        .pathsToMatch("/api/v1/manager/**")
        .addOpenApiCustomizer(this::addSecurityItem)
        .build();
  }

  private void addSecurityItem(OpenAPI openApi) {
    if (openApi.getComponents() == null) {
      openApi.setComponents(new Components());
    }

    openApi
        .getComponents()
        .addSecuritySchemes(
            securitySchemeName,
            new SecurityScheme()
                .name(securitySchemeName)
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT"));

    openApi.addSecurityItem(new SecurityRequirement().addList(securitySchemeName));
  }
}
