package io.dough.api.useCases.user.profile.saveProfileImg.application;

import io.dough.api.useCases.file.resolveURL.application.ResolveURLUseCase;
import io.dough.api.useCases.file.upload.application.UploadFileUseCase;
import io.dough.api.useCases.file.upload.application.model.UploadFileCmd;
import io.dough.api.useCases.shared.domain.file.FileUploadType;
import io.dough.api.useCases.user.profile.saveProfileImg.application.model.SaveProfileImgCmd;
import io.dough.api.useCases.user.profile.saveProfileImg.application.model.SavedProfileImg;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SaveProfileImgService implements SaveProfileImgUseCase {

  private final UploadFileUseCase uploadFileUseCase;
  private final UpdateUserProfileImg updateUserProfileImg;
  private final ResolveURLUseCase resolveURLUseCase;

  @Override
  public Optional<SavedProfileImg> operate(SaveProfileImgCmd cmd) {
    var file =
        uploadFileUseCase.operate(
            new UploadFileCmd(
                FileUploadType.MY_PROFILE_IMG,
                cmd.inputStream(),
                cmd.originalFilename(),
                cmd.fileSize()));

    updateUserProfileImg.operate(cmd.userId(), file.id());
    String referURL = resolveURLUseCase.operate(file.id());

    return Optional.of(
        new SavedProfileImg(
            file.id(), file.name(), file.originName(), file.fileLength(), referURL));
  }
}
