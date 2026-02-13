package io.dough.api.common.application.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MessageUtils {

  private final MessageSource messageSource;
  private static MessageUtils instance;

  @jakarta.annotation.PostConstruct
  public void init() {
    instance = this;
  }

  public String getMessage(String code) {
    return messageSource.getMessage(code, null, LocaleContextHolder.getLocale());
  }

  public String getMessage(String code, Object[] args) {
    return messageSource.getMessage(code, args, LocaleContextHolder.getLocale());
  }

  public static String get(String code) {
    return instance.getMessage(code);
  }

  public static String get(String code, Object[] args) {
    return instance.getMessage(code, args);
  }
}
