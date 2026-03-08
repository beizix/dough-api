package io.dough.api.support;

import io.dough.api.useCases.auth.issueToken.application.IssueTokenUseCase;
import io.dough.api.useCases.auth.resolveToken.application.ResolveTokenUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

@AutoConfigureMockMvc // 시큐리티 필터 적용 (기본값 addFilters = true)
@ActiveProfiles("test")
public abstract class WebMvcTestBase {

  @Autowired protected MockMvc mockMvc;

  @Autowired protected ObjectMapper objectMapper;

  @MockitoBean protected IssueTokenUseCase issueTokenUseCase;
  @MockitoBean protected ResolveTokenUseCase resolveTokenUseCase;

  protected String json(Object obj) throws Exception {
    return objectMapper.writeValueAsString(obj);
  }
}
