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

import static guru.qa.niffler.utils.RandomDataUtils.randomUsername;

@Disabled
public class JdbcTest {

  @Test
  void txTest() {
    SpendDbClient spendDbClient = new SpendDbClient();

    SpendJson spend = spendDbClient.createSpend(
        new SpendJson(
            null,
            new Date(),
            new CategoryJson(
                null,
                "cat-name-tx-2",
                "duck",
                false
            ),
            CurrencyValues.RUB,
            1000.0,
            "spend-name-tx",
            null
        )
    );

    System.out.println(spend);
  }

  @Test
  void springJdbcTxTest() {
    String username = randomUsername();
    System.out.println("username: " + username);
    UsersDbClient usersDbClient = new UsersDbClient();
    UserJson user = usersDbClient.createUser(
        new UserJson(
            null,
          username,
            null,
            null,
            null,
            CurrencyValues.RUB,
            null,
            null,
            null
        )
    );
    System.out.println(user);
  }

  @Test
  void springJdbcWithoutTxTest() {
    String username = randomUsername();
    System.out.println("username: " + username);
    UsersDbClient usersDbClient = new UsersDbClient();
    UserJson user = usersDbClient.createUserWithoutTx(
      new UserJson(
        null,
        username,
        null,
        null,
        null,
        CurrencyValues.RUB,
        null,
        null,
        null
      )
    );
    System.out.println(user);
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
  void jdbcWithoutTxTest() {
    String username = randomUsername();
    System.out.println("username: " + username);
    UsersDbClient usersDbClient = new UsersDbClient();
    UserJson user = usersDbClient.createUserWithoutTxJdbc(
        new UserJson(
            null,
            username,
            null,
            null,
            null,
            CurrencyValues.RUB,
            null,
            null,
            null
        )
    );
    System.out.println(user);
  }

  @Test
  void jdbcFriendshipTest() {
    UsersDbClient usersDbClient = new UsersDbClient();
    String userPrefix = "23";
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
