package io.dough.api.useCases.user.profile.getProfile.application;

import io.dough.api.useCases.file.resolveURL.application.ResolveURLUseCase;
import io.dough.api.useCases.user.profile.getProfile.application.model.GetProfileCmd;
import io.dough.api.useCases.user.profile.getProfile.application.model.ProfileLoaded;
import io.dough.api.useCases.user.profile.getProfile.application.model.UserProfile;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetProfileService implements GetProfileUseCase {

  private final LoadProfile loadProfile;
  private final ResolveURLUseCase resolveURLUseCase;

  @Override
  public UserProfile operate(GetProfileCmd cmd) {
    ProfileLoaded loaded = loadProfile.operate(cmd.id());

    return new UserProfile(
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
