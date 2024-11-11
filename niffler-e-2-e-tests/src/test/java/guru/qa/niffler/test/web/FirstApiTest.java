package guru.qa.niffler.test.web;

import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.service.impl.UsersApiClient;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Order(1)
public class FirstApiTest {

  private final UsersApiClient usersClient = new UsersApiClient();

  @User
  @Test
  void allUsersReturnsEmptyList(UserJson user) {
    List<UserJson> allUsers = usersClient.allUsers(user.username(), null);
    assertThat(allUsers).isEmpty();
  }
}
