package io.dough.api.config.storage;

import jakarta.annotation.PostConstruct;
import java.nio.file.Files;
import java.nio.file.Paths;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@Slf4j
@ConditionalOnProperty(
    name = "app.storage.local.enabled",
    havingValue = "true",
    matchIfMissing = true)
public class FileStorageConfig implements WebMvcConfigurer {

  @Value("${app.upload.path:#{null}}")
  private String uploadPath;

  @PostConstruct
  public void initialize() {
    if (uploadPath == null || uploadPath.isBlank()) {
      throw new IllegalStateException(
          "로컬 저장소가 활성화(app.storage.local.enabled=true)되었으나, 필수 설정인 'app.upload.path'가 누락되었습니다.");
    }

    if (!Files.exists(Paths.get(uploadPath))) {
      log.warn(
          "설정된 파일 보관 경로가 실제 파일 시스템에 존재하지 않습니다: {}. 경로가 없으면 파일 저장 시 오류가 발생할 수 있습니다.", uploadPath);
    }
  }

  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    if (uploadPath != null && !uploadPath.isBlank()) {
      String resourcePath = "file:" + Paths.get(uploadPath).toAbsolutePath().toString() + "/";
      registry.addResourceHandler("/uploads/**").addResourceLocations(resourcePath);

      log.info("Mapped '/uploads/**' to local resource: {}", resourcePath);
    }
  }
}
