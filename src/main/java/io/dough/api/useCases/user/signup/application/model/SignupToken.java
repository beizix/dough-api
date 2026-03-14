package io.dough.api.useCases.user.signup.application.model;

/**
 * 계정 생성 성공 후 발급된 인증 토큰 세트입니다.
 *
 * @param accessToken 액세스 토큰
 * @param refreshToken 리프레시 토큰
 */
public record SignupToken(String accessToken, String refreshToken) {}
