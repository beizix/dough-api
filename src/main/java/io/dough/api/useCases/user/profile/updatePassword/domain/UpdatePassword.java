package io.dough.api.useCases.user.profile.updatePassword.domain;

import io.dough.api.common.application.utils.MessageUtils;
import java.util.UUID;
import org.springframework.security.crypto.password.PasswordEncoder;

public record UpdatePassword(
    UUID id,
    String encodedPassword
) {

  public void verify(String rawPassword, PasswordEncoder passwordEncoder) {
    if (!passwordEncoder.matches(rawPassword, this.encodedPassword)) {
      throw new IllegalArgumentException(MessageUtils.get("error.password.current.incorrect"));
    }
  }

  public UpdatePassword update(String newRawPassword, PasswordEncoder passwordEncoder) {
    return new UpdatePassword(this.id, passwordEncoder.encode(newRawPassword));
  }
}
