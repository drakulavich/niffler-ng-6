package guru.qa.niffler.test.fake;

import guru.qa.niffler.jupiter.annotation.ApiLogin;
import guru.qa.niffler.jupiter.annotation.Token;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.UserJson;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Disabled
public class OAuthTest {

  @Test
  @User(username = "duck")
  @ApiLogin
  void oauthTest(@Token String token,  UserJson user) {
    System.out.println(user);
    assertThat(token).isNotEmpty();
  }
}
