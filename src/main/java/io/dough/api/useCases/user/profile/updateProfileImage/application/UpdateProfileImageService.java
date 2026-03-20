package io.dough.api.useCases.user.profile.updateProfileImage.application;

import io.dough.api.useCases.file.resolveURL.application.ResolveURLUseCase;
import io.dough.api.useCases.file.upload.application.port.in.UploadFileCmd;
import io.dough.api.useCases.file.upload.application.port.in.UploadFileUseCase;
import io.dough.api.useCases.shared.domain.file.FileUploadType;
import io.dough.api.useCases.user.profile.updateProfileImage.application.model.ProfileImageUpdated;
import io.dough.api.useCases.user.profile.updateProfileImage.application.model.UpdateProfileImageCmd;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdateProfileImageService implements UpdateProfileImageUseCase {

  private final UploadFileUseCase uploadFileUseCase;
  private final UpdateUserProfileImage updateUserProfileImage;
  private final ResolveURLUseCase resolveURLUseCase;

  @Override
  public Optional<ProfileImageUpdated> operate(UpdateProfileImageCmd cmd) {
    var file =
        uploadFileUseCase.operate(
            new UploadFileCmd(
                FileUploadType.MY_PROFILE_IMG,
                cmd.inputStream(),
                cmd.originalFilename(),
                cmd.fileSize()));

    updateUserProfileImage.operate(cmd.userId(), file.id());
    String referURL = resolveURLUseCase.operate(file.id());

    return Optional.of(
        new ProfileImageUpdated(
            file.id(), file.name(), file.originName(), file.fileLength(), referURL));
  }
}
