package io.dough.api.useCases.user.profile.updatePassword.domain;

import java.util.UUID;
import org.springframework.security.crypto.password.PasswordEncoder;

public record UpdatedPassword(UUID id, String encodedPassword) {

  public void verify(String rawPassword, PasswordEncoder passwordEncoder) {
    if (!passwordEncoder.matches(rawPassword, this.encodedPassword)) {
      throw new IllegalArgumentException("error.password.current.incorrect");
    }
  }

  public UpdatedPassword update(String newRawPassword, PasswordEncoder passwordEncoder) {
    return new UpdatedPassword(this.id, passwordEncoder.encode(newRawPassword));
  }
}
