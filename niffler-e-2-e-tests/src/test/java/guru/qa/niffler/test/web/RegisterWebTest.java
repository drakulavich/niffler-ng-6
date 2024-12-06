package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.rest.UserJson;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.Test;

import static guru.qa.niffler.utils.RandomDataUtils.randomUsername;

public class RegisterWebTest extends BaseWebTest {

  @Test
  void shouldRegisterNewUser() {
    String newUsername = randomUsername();
    String newPassword = "pa$$w0rd";

    Selenide.open(CFG.frontUrl(), LoginPage.class)
      .openRegisterPage()
      .register(newUsername, newPassword)
      .clickSignInButton()
      .login(newUsername, newPassword)
      .checkStatisticsVisible()
      .getSpendingTable().checkTableSize(0);
  }

  @User
  @Test
  void shouldNotRegisterUserWithExistingUsername(UserJson user) {
    String oldUsername = user.username();
    String newPassword = "pa$$w0rd";

    Selenide.open(CFG.frontUrl(), LoginPage.class)
      .openRegisterPage()
      .register(oldUsername, newPassword)
      .errorIsShown();
  }

  @Test
  void shouldShowErrorIfPasswordAndConfirmPasswordAreNotEqual() {
    String newUsername = randomUsername();
    String newPassword = "pa$$w0rd";

    Selenide.open(CFG.frontUrl(), LoginPage.class)
      .openRegisterPage()
      .setUsername(newUsername)
      .setPassword(newPassword)
      .setPasswordSubmit(newPassword + "!!")
      .clickSignUpButton()
      .errorIsShown();
  }
}
