package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.annotation.Spending;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.Test;

public class LoginWebTest extends BaseWebTest {
    private final LoginPage loginPage = new LoginPage();

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
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(user.username(), user.testData().password())
                .checkStatisticsVisible()
                .getSpendingTable().checkTableSize(1);
    }

    @User
    @Test
    void userShouldStayOnLoginPageAfterLoginWithBadCredentials(UserJson user) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .setUsername(user.username())
                .setPassword(user.testData().password() + "_bad") // bad password
                .submit()
                .verifyIsLoaded();
    }
}
