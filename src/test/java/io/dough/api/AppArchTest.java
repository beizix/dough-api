package io.dough.api;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.library.Architectures;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.library.Architectures.layeredArchitecture;

public class AppArchTest {
  final static String DOMAIN = "DOMAIN";
  final static String APPLICATION = "APPLICATION";
  final static String INBOUND_ADAPTER = "INBOUND_ADAPTER";
  final static String OUTBOUND_ADAPTER = "OUTBOUND_ADAPTER";
  final static String CONFIG = "CONFIG";

  final JavaClasses classes =
    new ClassFileImporter()
      .withImportOption(new ImportOption.DoNotIncludeTests())
      .importPackages("io.dough.api..");

  final Architectures.LayeredArchitecture layeredArchitecture =
    layeredArchitecture()
      .consideringOnlyDependenciesInAnyPackage("io.dough.api..")
      // `도매인 계층` 정의
      .layer(DOMAIN)
      .definedBy("..domain..")
      // `애플리케이션 계층` 정의
      .layer(APPLICATION)
      .definedBy("..application..")
      // `인바운드 어댑터` 계층 정의
      .layer(INBOUND_ADAPTER)
      .definedBy("..adapters.web..")
      // `아웃바운드 어댑터` 계층 정의
      .layer(OUTBOUND_ADAPTER)
      .definedBy("..adapters.persistence..", "..adapters.ai..", "..adapters.storage..")
      // `설정` 계층 정의
      .layer(CONFIG)
      .definedBy("..config..");

  @DisplayName("`인바운드 어댑터` 계층은 오직 `애플리케이션 계층` 만 참조할 수 있다.")
  @Test
  void webLayerMayOnlyAccessToApplication() {
    layeredArchitecture.whereLayer(INBOUND_ADAPTER).mayOnlyAccessLayers(APPLICATION, DOMAIN).check(classes);
  }

  @DisplayName("`인바운드 어댑터` 계층은 오직 `설정` 계층 만 접근을 허용 한다.")
  @Test
  void webLayerMayOnlyBeAccessedByLayers() {
    layeredArchitecture
      .whereLayer(INBOUND_ADAPTER)
      .mayOnlyBeAccessedByLayers(CONFIG)
      .check(classes);
  }

  @DisplayName("`아웃바운드 어댑터` 계층은 오직 `애플리케이션 계층`과 `도메인 계층` 만 참조할 수 있다.")
  @Test
  void persistenceLayerMayOnlyAccessToApplication() {
    layeredArchitecture.whereLayer(OUTBOUND_ADAPTER).mayOnlyAccessLayers(APPLICATION, DOMAIN).check(classes);
  }

  @DisplayName("`아웃바운드 어댑터` 계층은 오직 `애플리케이션 계층` 과 `설정` 계층 만 접근을 허용 한다.")
  @Test
  void mayOnlyBeAccessedByLayers() {
    layeredArchitecture
      .whereLayer(OUTBOUND_ADAPTER)
      .mayOnlyBeAccessedByLayers(APPLICATION, CONFIG)
      .check(classes);
  }

  @DisplayName("`애플리케이션` 계층은 `도매인` 계층만 참조 한다(중요).")
  @Test
  void applicationMayOnlyAccessDomainLayer() {
    layeredArchitecture.whereLayer(APPLICATION).mayOnlyAccessLayers(DOMAIN).check(classes);
  }

  @DisplayName("`도매인` 계층은 어느 계층도 참조하지 않는다(중요).")
  @Test
  void domainMayNotAccessAnyLayer() {
    layeredArchitecture.whereLayer(DOMAIN).mayNotAccessAnyLayer().check(classes);
  }

  @DisplayName("`애플리케이션` 계층은 모든 계층의 접근을 허용 한다.")
  @Test
  void applicationLayerMayOnlyBeAccessedByLayers() {
    layeredArchitecture
      .whereLayer(APPLICATION)
      .mayOnlyBeAccessedByLayers(INBOUND_ADAPTER, OUTBOUND_ADAPTER, CONFIG)
      .check(classes);
  }

  @DisplayName("`설정` 계층은 모든 계층을 참조 한다.")
  @Test
  void configMayOnlyAccessLayers() {
    layeredArchitecture
      .whereLayer(CONFIG)
      .mayOnlyAccessLayers(DOMAIN, APPLICATION, INBOUND_ADAPTER, OUTBOUND_ADAPTER)
      .check(classes);
  }

  @DisplayName("`설정` 계층은 모든 계층의 접근을 허용하지 않는다.")
  @Test
  void configMayNotAccessAnyLayer() {
    layeredArchitecture.whereLayer(CONFIG).mayNotBeAccessedByAnyLayer().check(classes);
  }
}
