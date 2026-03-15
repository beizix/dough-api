package io.dough.api.useCases.user.mgmt.registerManager.adapters.web.model;

public record RegisterManagerRequest(String email, String displayName, String password) {}
