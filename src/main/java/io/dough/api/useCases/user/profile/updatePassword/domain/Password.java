package io.dough.api.useCases.user.profile.updatePassword.domain;

import java.util.UUID;
import org.springframework.security.crypto.password.PasswordEncoder;

public record Password(UUID id, String encodedPassword) {

  public void verify(String rawPassword, PasswordEncoder passwordEncoder) {
    if (!passwordEncoder.matches(rawPassword, this.encodedPassword)) {
      throw new IllegalArgumentException("error.password.current.incorrect");
    }
  }

  public Password update(String newRawPassword, PasswordEncoder passwordEncoder) {
    return new Password(this.id, passwordEncoder.encode(newRawPassword));
  }
}
