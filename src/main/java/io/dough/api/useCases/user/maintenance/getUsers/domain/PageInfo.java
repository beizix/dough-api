package io.dough.api.useCases.user.maintenance.getUsers.domain;

public record PageInfo(long totalElements, int totalPages, int size, int number) {}
