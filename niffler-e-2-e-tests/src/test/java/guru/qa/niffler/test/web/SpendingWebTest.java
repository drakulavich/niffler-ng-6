package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.condition.Bubble;
import guru.qa.niffler.condition.Color;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.annotation.ScreenShotTest;
import guru.qa.niffler.jupiter.annotation.Spending;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.MainPage;
import org.junit.jupiter.api.Test;

import java.awt.image.BufferedImage;
import java.io.IOException;

@WebTest
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

  @User(
    spendings = @Spending(
      category = "Обучение",
      description = "Обучение Advanced 2.0",
      amount = 79990
    )
  )
  @ScreenShotTest("img/expected-stat.png")
  void checkStatComponentTest(UserJson user, BufferedImage expected) throws IOException {
    Selenide.open(CFG.frontUrl(), LoginPage.class)
      .login(user.username(), user.testData().password());

    waitForStatComponent();
    mainPage.getStatComponent()
      .checkBubbles(Bubble.of(Color.yellow, "Обучение 79990 ₽"))
      .checkWidgetImage(expected);
  }

  @User(
    spendings = {
      @Spending(
        category = "Отдых",
        description = "Лыжи",
        amount = 6000),
      @Spending(
        category = "Еда",
        description = "Ресторан",
        amount = 3333)
    }
  )
  @ScreenShotTest(value = "img/expected-stat-removed.png")
  void userCanRemoveSpending(UserJson user, BufferedImage expected) throws IOException {
    Selenide.open(CFG.frontUrl(), LoginPage.class)
        .login(user.username(), user.testData().password())
        .getSpendingTable().deleteSpending("Лыжи");

    waitForStatComponent();
    mainPage.getStatComponent()
      .checkBubblesAnyOrder(
        Bubble.of(Color.yellow, "Еда 3333 ₽")
      )
      .checkWidgetImage(expected);
  }

  @User(
    spendings = {
      @Spending(
        category = "Отдых",
        description = "Лыжи",
        amount = 6000),
      @Spending(
        category = "Еда",
        description = "Ресторан",
        amount = 3333)
    }
  )
  @ScreenShotTest(value = "img/expected-stat-edit.png")
  void userCanEditSpendingAmount(UserJson user, BufferedImage expected) throws IOException {
    Selenide.open(CFG.frontUrl(), LoginPage.class)
        .login(user.username(), user.testData().password())
        .getSpendingTable().editSpending("Ресторан")
        .setNewSpendingAmount(4000)
        .save();

    waitForStatComponent();
    mainPage.getStatComponent()
      .checkBubblesAnyOrder(
        Bubble.of(Color.green, "Еда 4000 ₽"),
        Bubble.of(Color.yellow, "Отдых 6000 ₽")
      )
      .checkWidgetImage(expected);
  }

  @User(
    spendings = {
      @Spending(
        category = "Отдых",
        description = "Лыжи",
        amount = 6000),
      @Spending(
        category = "Еда",
        description = "Ресторан",
        amount = 3333)
    },
    categories = {
      @Category(
        name = "Отдых",
        archived = true
      )
    }
  )
  @ScreenShotTest(value = "img/expected-stat-archived.png")
  void userCanSeeArchivedCategoriesInPieChart(UserJson user, BufferedImage expected) throws IOException {
    Selenide.open(CFG.frontUrl(), LoginPage.class)
        .login(user.username(), user.testData().password());

    waitForStatComponent();
    mainPage.getStatComponent()
      .checkBubblesContain(
        Bubble.of(Color.yellow, "Еда 3333 ₽"),
        Bubble.of(Color.green, "Archived 6000 ₽")
      )
      .checkWidgetImage(expected);
  }

  @User(
    spendings = {
      @Spending(
        category = "Отдых",
        description = "Кино",
        amount = 100),
      @Spending(
        category = "Еда",
        description = "Продукты",
        amount = 200)
    }
  )
  @Test
  void userCanSeeAvailableSpendings(UserJson user) {
    Selenide.open(CFG.frontUrl(), LoginPage.class)
      .login(user.username(), user.testData().password());

    mainPage.getSpendingTable()
      .checkTableSpendings(user.testData().spendings().toArray(new SpendJson[0]));
  }

  private void waitForStatComponent() {
    Selenide.sleep(3000);
  }
}
