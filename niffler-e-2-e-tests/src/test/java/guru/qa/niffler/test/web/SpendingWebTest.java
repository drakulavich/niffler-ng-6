package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.annotation.Spending;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.MainPage;
import org.junit.jupiter.api.Test;

public class SpendingWebTest extends BaseWebTest {
  private final MainPage mainPage = new MainPage();

  @User(
      categories = {@Category(
          username = "duck",
          archived = true
      )},
      spendings = {
      @Spending(
          username = "duck",
          category = "Обучение",
          description = "Обучение Advanced 2.0",
          amount = 79990
      )
  })
  @Test
  void categoryDescriptionShouldBeChangedFromTable(SpendJson spend) {
    final String newDescription = "Обучение Niffler Next Generation";

    Selenide.open(CFG.frontUrl(), LoginPage.class)
        .login("duck", "12345")
        .editSpending(spend.description())
        .setNewSpendingDescription(newDescription)
        .save();

    mainPage.checkThatTableContainsSpending(newDescription);
  }
}
