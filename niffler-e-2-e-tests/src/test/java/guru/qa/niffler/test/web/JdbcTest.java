package guru.qa.niffler.test.web;

import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.service.SpendDbClient;
import guru.qa.niffler.service.UsersDbClient;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Date;

@Disabled
public class JdbcTest {

  static SpendDbClient spendDbClient = new SpendDbClient();
  static UsersDbClient usersDbClient = new UsersDbClient();

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
    "val-8"
  })
  @ParameterizedTest
  void jdbcTxTest(String username) {
    System.out.println("username: " + username);
    UserJson user = usersDbClient.createUser(
        username,
        "12345"
    );

    usersDbClient.addIncomeInvitation(user, 2);
    usersDbClient.addOutcomeInvitation(user, 3);
    usersDbClient.addFriend(user, 2);
  }

  @Test
  void jdbcFriendshipTest() {
    String userPrefix = "27";
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
