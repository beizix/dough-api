package io.dough.api.useCases.shared.application.service.auth;

public enum TokenType {
  access("액세스 토큰"),
  refresh("리프레시 토큰");

  private final String desc;

  TokenType(String desc) {
    this.desc = desc;
  }

  public String getDesc() {
    return desc;
  }
}
