package guru.qa.niffler.test.web;

import guru.qa.niffler.jupiter.extension.UsersClientExtension;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.service.UsersClient;
import guru.qa.niffler.service.impl.SpendDbClient;
import guru.qa.niffler.service.impl.UsersDbClient;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Date;

@Disabled
@ExtendWith(UsersClientExtension.class)
public class JdbcTest {

  static SpendDbClient spendDbClient = new SpendDbClient();
  static UsersDbClient usersDbClient = new UsersDbClient();
  private UsersClient usersClient;

  @Test
  void txTest() {
    SpendJson spend = spendDbClient.createSpend(
        new SpendJson(
            null,
            new Date(),
            new CategoryJson(
                null,
                "cat-name-tx-8",
                "duck",
                false
            ),
            CurrencyValues.RUB,
            1000.0,
            "spend-name-tx-8",
            "duck"
        )
    );

    System.out.println(spend);
  }

  @ValueSource(strings = {
    "val-13"
  })
  @ParameterizedTest
  void jdbcTxTest(String username) {
    System.out.println("username: " + username);
    UserJson user = usersClient.createUser(
        username,
        "12345"
    );

    usersClient.addIncomeInvitation(user, 2);
    usersClient.addOutcomeInvitation(user, 3);
    usersClient.addFriend(user, 2);
  }

  @Test
  void jdbcFriendshipTest() {
    String userPrefix = "28";
    UserJson myself = usersDbClient.createUser(
      "myself" + userPrefix,
      "12345"
    );

    UserJson friend = usersDbClient.createUser(
      "friend" + userPrefix,
      "12345"
    );

    UserJson income = usersDbClient.createUser(
      "income" + userPrefix,
      "12345"
    );

    UserJson outcome = usersDbClient.createUser(
      "outcome" + userPrefix,
      "12345"
    );

    usersDbClient.addInvitation(income, myself);
    usersDbClient.addInvitation(myself, outcome);
    usersDbClient.addFriends(myself, friend);
  }
}
