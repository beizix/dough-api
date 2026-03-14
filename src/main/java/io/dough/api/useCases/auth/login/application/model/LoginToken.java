package io.dough.api.useCases.auth.login.application.model;

/**
 * 로그인 성공 후 발급된 인증 토큰 세트입니다.
 *
 * @param accessToken 액세스 토큰
 * @param refreshToken 리프레시 토큰
 */
public record LoginToken(String accessToken, String refreshToken) {}
