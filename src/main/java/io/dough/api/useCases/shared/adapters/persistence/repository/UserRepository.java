package io.dough.api.useCases.shared.adapters.persistence.repository;

import io.dough.api.useCases.shared.adapters.persistence.entity.UserEntity;
import io.dough.api.useCases.shared.domain.auth.Role;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface UserRepository
    extends JpaRepository<UserEntity, UUID>, JpaSpecificationExecutor<UserEntity> {

  Optional<UserEntity> findByEmailAndRole(String email, Role role);

  boolean existsByEmailAndRole(String email, Role role);

  Optional<UserEntity> findByRefreshToken(String refreshToken);
}
