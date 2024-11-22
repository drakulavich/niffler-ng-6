package guru.qa.niffler.test.web;

import com.codeborne.selenide.SelenideDriver;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.annotation.Spending;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.extension.BrowserExtension;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.utils.SelenideUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.util.List;

public class LoginWebTest extends BaseWebTest {

  @RegisterExtension
  private final BrowserExtension browserExtension = new BrowserExtension();
  private final SelenideDriver chrome = new SelenideDriver(SelenideUtils.chromeConfig);

  @User(
    categories = {
      @Category(name = "Food", archived = false),
      @Category(name = "Transport", archived = true)
    },
    spendings = {
      @Spending(category = "Entertainer", description = "Lunch", amount = 100),
    }
  )
  @Test
  void mainPageShouldBeDisplayedAfterSuccessLogin(UserJson user) {
    browserExtension.drivers().add(chrome);

    chrome.open(CFG.frontUrl(), LoginPage.class);

    new LoginPage(chrome)
      .login(user.username(), user.testData().password())
      .checkStatisticsVisible()
      .getSpendingTable().checkTableSize(1);
  }

  @User
  @Test
  void userShouldStayOnLoginPageAfterLoginWithBadCredentials(UserJson user) {
    SelenideDriver firefox = new SelenideDriver(SelenideUtils.firefoxConfig);
    browserExtension.drivers().addAll(List.of(chrome, firefox));

    chrome.open(CFG.frontUrl(), LoginPage.class);
    firefox.open(CFG.frontUrl(), LoginPage.class);

    new LoginPage(chrome)
      .setUsername(user.username())
      .setPassword(user.testData().password() + "_bad") // bad password
      .submit()
      .verifyIsLoaded();
  }
}
