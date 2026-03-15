package io.dough.api.useCases.user.mgmt.updateManager.adapters.web.model;

public record UpdateManagerRequest(String email, String displayName, String password) {}
