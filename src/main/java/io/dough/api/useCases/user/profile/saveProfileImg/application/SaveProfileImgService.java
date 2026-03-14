package io.dough.api.useCases.user.profile.saveProfileImg.application;

import io.dough.api.useCases.file.getFileURL.application.GetFileURLUseCase;
import io.dough.api.useCases.file.saveFile.application.SaveFileUseCase;
import io.dough.api.useCases.shared.application.file.FileUploadType;
import io.dough.api.useCases.user.profile.saveProfileImg.application.model.SaveProfileImgCmd;
import io.dough.api.useCases.user.profile.saveProfileImg.application.model.SavedProfileImg;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SaveProfileImgService implements SaveProfileImgUseCase {

  private final SaveFileUseCase saveFileUseCase;
  private final UpdateUserProfileImg updateUserProfileImg;
  private final GetFileURLUseCase getFileURLUseCase;

  @Override
  public Optional<SavedProfileImg> operate(SaveProfileImgCmd cmd) {
    var file =
        saveFileUseCase.operate(
            new io.dough.api.useCases.file.saveFile.application.model.SaveFileCmd(
                FileUploadType.MY_PROFILE_IMG,
                cmd.inputStream(),
                cmd.originalFilename(),
                cmd.fileSize()));

    updateUserProfileImg.operate(cmd.userId(), file.id());
    String referURL = getFileURLUseCase.operate(file.id());

    return Optional.of(
        new SavedProfileImg(
            file.id(), file.name(), file.originName(), file.fileLength(), referURL));
  }
}
