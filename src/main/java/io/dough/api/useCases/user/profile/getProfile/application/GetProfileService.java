package io.dough.api.useCases.user.profile.getProfile.application;

import io.dough.api.useCases.file.getFileURL.application.GetFileURLUseCase;
import io.dough.api.useCases.user.profile.getProfile.application.model.ProfileLoaded;
import io.dough.api.useCases.user.profile.getProfile.domain.GetProfileCmd;
import io.dough.api.useCases.user.profile.getProfile.domain.Profile;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetProfileService implements GetProfileUseCase {

  private final LoadProfile loadProfile;
  private final GetFileURLUseCase getFileURLUseCase;

  @Override
  public Profile operate(GetProfileCmd cmd) {
    ProfileLoaded loaded = loadProfile.operate(cmd.id());

    String profileImageUrl =
        loaded.profileImageId() != null ? getFileURLUseCase.operate(loaded.profileImageId()) : null;

    return new Profile(
        loaded.id(),
        loaded.email(),
        loaded.displayName(),
        loaded.createdAt(),
        loaded.profileImageId(),
        profileImageUrl);
  }
}
