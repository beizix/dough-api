package io.dough.api.useCases.user.mgmt.updateManager.adapters.in.web;

public record UpdateManagerRequest(String email, String displayName, String password) {}
