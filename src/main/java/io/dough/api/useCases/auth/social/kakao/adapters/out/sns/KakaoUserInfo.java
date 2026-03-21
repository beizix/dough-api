package io.dough.api.useCases.auth.social.kakao.adapters.out.sns;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class KakaoUserInfo {

  private Long id;

  @JsonProperty("kakao_account")
  private KakaoAccount kakaoAccount;

  @Getter
  @Setter
  @NoArgsConstructor
  public static class KakaoAccount {
    private String email;
    private Profile profile;

    @Getter
    @Setter
    @NoArgsConstructor
    public static class Profile {
      private String nickname;

      @JsonProperty("thumbnail_image_url")
      private String thumbnailImageUrl;

      @JsonProperty("profile_image_url")
      private String profileImageUrl;
    }
  }
}
