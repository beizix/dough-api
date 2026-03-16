package io.dough.api.config.security;

import io.dough.api.useCases.auth.resolveToken.domain.TokenResolver;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final SecretKey key;

  public JwtAuthenticationFilter(@Value("${jwt.secret}") String secret) {
    this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
  }

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {

    Optional<String> tokenOptional = resolveToken(request);

    tokenOptional.ifPresent(
        token -> {
          TokenResolver tokenResolver = new TokenResolver(key, token);
          if (tokenResolver.validate()) {
            String userUuid = tokenResolver.getSubject();
            List<SimpleGrantedAuthority> authorities =
                Stream.concat(
                        Stream.of(tokenResolver.getRole()),
                        tokenResolver.getPrivileges().stream())
                    .filter(Objects::nonNull)
                    .map(SimpleGrantedAuthority::new)
                    .toList();

            UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(userUuid, null, authorities);
            SecurityContextHolder.getContext().setAuthentication(authentication);
          }
        });

    filterChain.doFilter(request, response);
  }

  private Optional<String> resolveToken(HttpServletRequest request) {
    String bearerToken = request.getHeader("Authorization");
    if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
      return Optional.of(bearerToken.substring(7));
    }
    return Optional.empty();
  }
}
