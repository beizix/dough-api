package io.dough.api.useCases.shared.domain.auth;

public enum Token {
  access("액세스 토큰"),
  refresh("리프레시 토큰");

  private final String desc;

  Token(String desc) {
    this.desc = desc;
  }

  public String getDesc() {
    return desc;
  }
}
