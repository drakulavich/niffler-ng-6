package guru.qa.niffler.test.web;

import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.service.impl.AuthApiClient;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

//@Disabled
public class OAuthTest {

  private final AuthApiClient authApi = new AuthApiClient();

  @User
  @Test
  void oauthTest(UserJson user) {
    String codeVerifier = authApi.preRequest();
    String code = authApi.login(user.username(), user.testData().password());

    String token = authApi.token(code, codeVerifier);
    assertThat(token).isNotEmpty();
  }

}
