package guru.qa.niffler.test.web;

import guru.qa.niffler.api.UserDataApiClient;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.UserJson;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Order(1)
public class FirstApiTest {

  @User
  @Test
  void allUsersReturnsEmptyList(UserJson user) {
    UserDataApiClient userDataApiClient = new UserDataApiClient();
    List<UserJson> allUsers = userDataApiClient.allUsers(user.username(), null);

    assertThat(allUsers).isEmpty();
  }
}
