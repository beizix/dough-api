package io.dough.api.useCases.shared.adapters.web;

import java.util.NoSuchElementException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ExceptionTestController {

  @GetMapping("/test/no-such-element")
  public void throwNoSuchElementException(String message) {
    throw new NoSuchElementException(message);
  }

  @GetMapping("/test/illegal-argument")
  public void throwIllegalArgumentException(String message) {
    throw new IllegalArgumentException(message);
  }

  @GetMapping("/test/unhandled-exception")
  public void throwUnhandledException(String message) {
    throw new RuntimeException(message);
  }
}
