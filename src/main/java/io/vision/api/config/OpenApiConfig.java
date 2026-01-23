package io.vision.api.config;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

  @Bean
  public GroupedOpenApi publicApi() {
    return GroupedOpenApi.builder()
        .group("1. Public API")
        .pathsToMatch("/api/v1/auth/**", "/api/v1/signup/user")
        .build();
  }

  @Bean
  public GroupedOpenApi userApi() {
    return GroupedOpenApi.builder()
        .group("2. User API")
        .pathsToMatch("/api/v1/user/**")
        .build();
  }

  @Bean
  public GroupedOpenApi managerApi() {
    return GroupedOpenApi.builder()
        .group("3. Manager API")
        .pathsToMatch("/api/v1/manager/**")
        .build();
  }
}
