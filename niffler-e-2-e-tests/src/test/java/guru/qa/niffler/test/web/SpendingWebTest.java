package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.jupiter.annotation.Spending;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.MainPage;
import org.junit.jupiter.api.Test;

public class SpendingWebTest extends BaseWebTest {
  private final MainPage mainPage = new MainPage();

  @User(
    spendings = {
      @Spending(
        category = "Обучение",
        description = "Обучение Advanced 2.0",
        amount = 79990),
      @Spending(
        category = "Еда",
        description = "Слева рестик",
        amount = 3333)
    }
  )
  @Test
  void categoryDescriptionShouldBeChangedFromTable(UserJson user) {
    final String newDescription = "Обучение Niffler Next Generation";

    String oldDescription = user.testData().spendings().getFirst().description();
    Selenide.open(CFG.frontUrl(), LoginPage.class)
        .login(user.username(), user.testData().password())
        .getSpendingTable().editSpending(oldDescription)
        .setNewSpendingDescription(newDescription)
        .save();

    mainPage.getSpendingTable().checkTableContains(newDescription);
  }

  @User
  @Test
  void userCanCreateSpending(UserJson user) {
    Selenide.open(CFG.frontUrl(), LoginPage.class)
        .login(user.username(), user.testData().password())
        .getHeader().addSpendingPage().createSpend("Museum", "Art", 300)
        .checkAlert("New spending is successfully created")
        .getSpendingTable().checkTableContains("Museum");
  }
}
