package io.dough.api.useCases.user.getMyProfile.application;

import io.dough.api.useCases.user.getMyProfile.application.domain.model.GetMyProfileCmd;
import io.dough.api.useCases.user.getMyProfile.application.domain.model.MyProfile;

public interface GetMyProfileUseCase {
  MyProfile operate(GetMyProfileCmd cmd);
}
