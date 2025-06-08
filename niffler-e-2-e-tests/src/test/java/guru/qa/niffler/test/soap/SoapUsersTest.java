package guru.qa.niffler.test.soap;

import guru.qa.jaxb.userdata.CurrentUserRequest;
import guru.qa.jaxb.userdata.UserResponse;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.annotation.meta.SoapTest;
import guru.qa.niffler.model.rest.UserJson;
import guru.qa.niffler.service.impl.UserdataSoapClient;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@SoapTest
public class SoapUsersTest {

  private final UserdataSoapClient userdataSoapClient = new UserdataSoapClient();

  @Test
  @User
  void currentUserTest(UserJson user) throws IOException {
    CurrentUserRequest currentUserRequest = new CurrentUserRequest();
    currentUserRequest.setUsername(user.username());

    UserResponse response = userdataSoapClient.getCurrent(currentUserRequest);
    assertThat(response.getUser().getUsername()).isEqualTo(user.username());
  }
}
