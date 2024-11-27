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
    String token = authApi.getToken(user.username(), user.testData().password());
    assertThat(token).isNotEmpty();
  }
}
