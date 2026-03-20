package io.dough.api.useCases.user.mgmt.searchUsers.adapters.out.persistence;

import io.dough.api.useCases.shared.adapters.out.persistence.entity.UserEntity;
import io.dough.api.useCases.shared.adapters.out.persistence.repository.UserRepository;
import io.dough.api.useCases.shared.application.service.model.PageInfo;
import io.dough.api.useCases.user.mgmt.searchUsers.application.port.out.FindUsers;
import io.dough.api.useCases.user.mgmt.searchUsers.application.port.in.SearchUsers;
import io.dough.api.useCases.user.mgmt.searchUsers.application.port.in.SearchUsersCmd;
import io.dough.api.useCases.user.mgmt.searchUsers.application.port.in.SearchedUser;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FindUsersPersistAdapter implements FindUsers {

  private final UserRepository userRepository;

  @Override
  public SearchUsers operate(SearchUsersCmd cmd) {
    Specification<UserEntity> spec = createSpecification(cmd);
    PageRequest pageRequest = createPageRequest(cmd);

    Page<UserEntity> userPage = userRepository.findAll(spec, pageRequest);

    List<SearchedUser> userList =
        userPage.getContent().stream()
            .map(u -> new SearchedUser(u.getId(), u.getEmail(), u.getDisplayName(), u.getRole()))
            .toList();

    PageInfo pageInfo =
        new PageInfo(
            userPage.getTotalElements(),
            userPage.getTotalPages(),
            userPage.getSize(),
            userPage.getNumber());

    return new SearchUsers(userList, pageInfo);
  }

  private Specification<UserEntity> createSpecification(SearchUsersCmd cmd) {
    return (root, query, cb) -> {
      List<Predicate> predicates = new ArrayList<>();

      if (cmd.email() != null && !cmd.email().isBlank()) {
        predicates.add(cb.like(root.get("email"), "%" + cmd.email() + "%"));
      }

      if (cmd.displayName() != null && !cmd.displayName().isBlank()) {
        predicates.add(cb.like(root.get("displayName"), "%" + cmd.displayName() + "%"));
      }

      if (cmd.role() != null) {
        predicates.add(cb.equal(root.get("role"), cmd.role()));
      }

      return cb.and(predicates.toArray(new Predicate[0]));
    };
  }

  private PageRequest createPageRequest(SearchUsersCmd cmd) {
    // Sort 문자열을 Sort 객체로 변환 (예: "id: DESC" -> Sort.by(Sort.Direction.DESC, "id"))
    // 간단한 구현을 위해 여기서는 파싱 로직을 포함하거나 기본 정렬을 사용합니다.
    String[] sortParts = cmd.sort().split(": ");
    Sort sort = Sort.unsorted();
    if (sortParts.length == 2) {
      String property = sortParts[0];
      Sort.Direction direction = Sort.Direction.fromString(sortParts[1]);
      sort = Sort.by(direction, property);
    }

    return PageRequest.of(cmd.page(), cmd.size(), sort);
  }
}
