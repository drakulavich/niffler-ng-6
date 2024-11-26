package guru.qa.niffler.test.web;

import com.codeborne.selenide.SelenideDriver;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.annotation.Spending;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.jupiter.converter.Browser;
import guru.qa.niffler.jupiter.converter.BrowserConverter;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.MainPage;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.provider.EnumSource;

@WebTest
public class LoginWebTest extends BaseWebTest {

  @User(
    categories = {
      @Category(name = "Food", archived = false),
      @Category(name = "Transport", archived = true)
    },
    spendings = {
      @Spending(category = "Entertainer", description = "Lunch", amount = 100),
    }
  )
  @ParameterizedTest
  @EnumSource(Browser.class)
  void mainPageShouldBeDisplayedAfterSuccessLogin(@ConvertWith(BrowserConverter.class) SelenideDriver driver, UserJson user) {
    driver.open(CFG.frontUrl(), LoginPage.class);

    new LoginPage(driver)
      .login(user.username(), user.testData().password());

    new MainPage(driver)
      .checkStatisticsVisible()
      .getSpendingTable().checkTableSize(1);
  }

  @User
  @ParameterizedTest
  @EnumSource(Browser.class)
  void userShouldStayOnLoginPageAfterLoginWithBadCredentials(@ConvertWith(BrowserConverter.class) SelenideDriver driver, UserJson user) {
    driver.open(CFG.frontUrl(), LoginPage.class);

    new LoginPage(driver)
      .setUsername(user.username())
      .setPassword(user.testData().password() + "_bad") // bad password
      .submit()
      .verifyIsLoaded();
  }
}
