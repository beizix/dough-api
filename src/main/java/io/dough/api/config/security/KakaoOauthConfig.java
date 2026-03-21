package io.dough.api.config.security;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "app.oauth.kakao")
@Getter
@Setter
public class KakaoOauthConfig {
  private String clientId;
  private String clientSecret;
  private String redirectUri;
}
