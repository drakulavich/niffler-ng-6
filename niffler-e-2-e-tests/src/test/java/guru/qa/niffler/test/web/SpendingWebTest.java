package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.condition.Bubble;
import guru.qa.niffler.condition.Color;
import guru.qa.niffler.jupiter.annotation.ApiLogin;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.annotation.ScreenShotTest;
import guru.qa.niffler.jupiter.annotation.Spending;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.page.MainPage;
import org.junit.jupiter.api.Test;

import java.awt.image.BufferedImage;
import java.io.IOException;

public class SpendingWebTest extends BaseWebTest {

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
  @ApiLogin
  @Test
  void categoryDescriptionShouldBeChangedFromTable(UserJson user) {
    final String newDescription = "Обучение Niffler Next Generation";
    final String oldDescription = user.testData().spendings().getFirst().description();

    Selenide.open(MainPage.URL, MainPage.class)
      .getSpendingTable().editSpending(oldDescription)
      .setNewSpendingDescription(newDescription)
      .save()
      .getSpendingTable().checkTableContains(newDescription);
  }

  @User
  @ApiLogin
  @Test
  void userCanCreateSpending(UserJson user) {
    Selenide.open(MainPage.URL, MainPage.class)
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
  @ApiLogin
  @ScreenShotTest("img/expected-stat.png")
  void checkStatComponentTest(UserJson user, BufferedImage expected) throws IOException {
    Selenide.open(MainPage.URL, MainPage.class)
      .waitForComponentRendering()
      .getStatComponent()
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
  @ApiLogin
  @ScreenShotTest(value = "img/expected-stat-removed.png")
  void userCanRemoveSpending(UserJson user, BufferedImage expected) throws IOException {
    MainPage mainPage = Selenide.open(MainPage.URL, MainPage.class);
    mainPage.getSpendingTable().deleteSpending("Лыжи");

    mainPage
      .waitForComponentRendering()
      .getStatComponent()
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
  @ApiLogin
  @ScreenShotTest(value = "img/expected-stat-edit.png")
  void userCanEditSpendingAmount(UserJson user, BufferedImage expected) throws IOException {
    Selenide.open(MainPage.URL, MainPage.class)
      .getSpendingTable().editSpending("Ресторан")
      .setNewSpendingAmount(4000)
      .save()
      .waitForComponentRendering()
      .getStatComponent()
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
  @ApiLogin
  @ScreenShotTest(value = "img/expected-stat-archived.png")
  void userCanSeeArchivedCategoriesInPieChart(UserJson user, BufferedImage expected) throws IOException {
    Selenide.open(MainPage.URL, MainPage.class)
      .waitForComponentRendering()
      .getStatComponent()
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
  @ApiLogin
  @Test
  void userCanSeeAvailableSpendings(UserJson user) {
    Selenide.open(MainPage.URL, MainPage.class)
      .getSpendingTable()
      .checkTableSpendings(user.testData().spendings().toArray(new SpendJson[0]));
  }
}
