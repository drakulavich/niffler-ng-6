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

  @Test
  void txTest() {
    SpendJson spend = spendDbClient.createSpend(
        new SpendJson(
            null,
            new Date(),
            new CategoryJson(
                null,
                "cat-name-tx-3",
                "duck",
                false
            ),
            CurrencyValues.RUB,
            1000.0,
            "spend-name-tx-3",
            "duck"
        )
    );

    System.out.println(spend);
  }

  static UsersDbClient usersDbClient = new UsersDbClient();

  @ValueSource(strings = {
    "val-5"
  })
  @ParameterizedTest
  void jdbcTxTest(String username) {
    System.out.println("username: " + username);
    UserJson user = usersDbClient.createUserHibernate(
        username,
        "12345"
    );

    usersDbClient.addIncomeInvitation(user, 1);
    usersDbClient.addOutcomeInvitation(user, 1);
  }

  @Test
  void jdbcFriendshipTest() {
    UsersDbClient usersDbClient = new UsersDbClient();
    String userPrefix = "24";
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
