package io.dough.api.useCases.user.mgmt.registerManager.adapters.in.web;

public record RegisterManagerRequest(String email, String displayName, String password) {}
