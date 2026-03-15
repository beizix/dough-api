package io.dough.api.useCases.user.profile.retrieveProfile.application;

import io.dough.api.useCases.file.resolveURL.application.ResolveURLUseCase;
import io.dough.api.useCases.user.profile.retrieveProfile.application.model.ProfileLoaded;
import io.dough.api.useCases.user.profile.retrieveProfile.application.model.RetrieveProfile;
import io.dough.api.useCases.user.profile.retrieveProfile.application.model.RetrieveProfileCmd;
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
