package io.dough.api.useCases.user.profile.retrieveProfile.application.service;

import io.dough.api.useCases.file.resolveURL.application.port.in.ResolveURLUseCase;
import io.dough.api.useCases.user.profile.retrieveProfile.application.port.in.RetrieveProfile;
import io.dough.api.useCases.user.profile.retrieveProfile.application.port.in.RetrieveProfileCmd;
import io.dough.api.useCases.user.profile.retrieveProfile.application.port.in.RetrieveProfileUseCase;
import io.dough.api.useCases.user.profile.retrieveProfile.application.port.out.LoadProfile;
import io.dough.api.useCases.user.profile.retrieveProfile.application.port.out.ProfileLoaded;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RetrieveProfileService implements RetrieveProfileUseCase {

  private final LoadProfile loadProfile;
  private final ResolveURLUseCase resolveURLUseCase;

  @Override
  public RetrieveProfile operate(RetrieveProfileCmd cmd) {
    ProfileLoaded loaded = loadProfile.operate(cmd.id());

    return new RetrieveProfile(
        loaded.id(),
        loaded.email(),
        loaded.displayName(),
        loaded.createdAt(),
        loaded.profileImageId(),
        loaded.profileImageId() != null
            ? resolveURLUseCase.operate(loaded.profileImageId())
            : null);
  }
}
