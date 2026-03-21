package io.dough.api.useCases.auth.social.kakao.adapters.out.sns;

import io.dough.api.config.security.KakaoOauthConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
@RequiredArgsConstructor
public class KakaoAuthClient {

  private final KakaoOauthConfig kakaoOauthConfig;
  private final RestTemplate restTemplate = new RestTemplate();

  public KakaoTokenResponse fetchToken(String code) {
    String url = "https://kauth.kakao.com/oauth/token";

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

    MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
    body.add("grant_type", "authorization_code");
    body.add("client_id", kakaoOauthConfig.getClientId());
    body.add("client_secret", kakaoOauthConfig.getClientSecret());
    body.add("redirect_uri", kakaoOauthConfig.getRedirectUri());
    body.add("code", code);

    HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

    try {
      ResponseEntity<KakaoTokenResponse> response =
          restTemplate.postForEntity(url, request, KakaoTokenResponse.class);

      if (response.getStatusCode().isError() || response.getBody() == null) {
        log.error("Kakao Token Error: {}", response.getStatusCode());
        throw new RuntimeException("exception.auth.kakao_token_failed");
      }

      return response.getBody();
    } catch (Exception e) {
      log.error("Kakao Token Fetch Failed", e);
      throw new RuntimeException("exception.auth.kakao_token_failed");
    }
  }

  public KakaoUserInfo fetchUserInfo(String accessToken) {
    String url = "https://kapi.kakao.com/v2/user/me";

    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(accessToken);
    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

    HttpEntity<Void> request = new HttpEntity<>(headers);

    try {
      ResponseEntity<KakaoUserInfo> response =
          restTemplate.postForEntity(url, request, KakaoUserInfo.class);

      if (response.getStatusCode().isError() || response.getBody() == null) {
        log.error("Kakao User Info Error: {}", response.getStatusCode());
        throw new RuntimeException("exception.auth.kakao_user_failed");
      }

      return response.getBody();
    } catch (Exception e) {
      log.error("Kakao User Info Fetch Failed", e);
      throw new RuntimeException("exception.auth.kakao_user_failed");
    }
  }
}
