package io.dough.api.useCases.user.mgmt.getUserDetail.application.port.in;

import java.util.UUID;

public record GetUserDetailCmd(UUID id) {}
