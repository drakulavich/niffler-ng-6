package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.BrowserExtension;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(BrowserExtension.class)
public class RegisterWebTest {
    private static final Config CFG = Config.getInstance();

    @Test
    void shouldRegisterNewUser() {
        String newUsername = "u" + System.currentTimeMillis();
        String newPassword = "pa$$w0rd";

        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .openRegisterPage()
                .setUsername(newUsername)
                .setPassword(newPassword)
                .setPasswordSubmit(newPassword)
                .submitRegistration()
                .clickSignInButton();

        new LoginPage()
                .login(newUsername, newPassword)
                .checkStatisticsVisible()
                .checkSpendingsVisible();
    }

    @Test
    void shouldNotRegisterUserWithExistingUsername() {
        String oldUsername = "duck";
        String newPassword = "pa$$w0rd";

        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .openRegisterPage()
                .setUsername(oldUsername)
                .setPassword(newPassword)
                .setPasswordSubmit(newPassword)
                .clickSignUpButton()
                .errorIsShown();
    }

    @Test
    void shouldShowErrorIfPasswordAndConfirmPasswordAreNotEqual() {
        String newUsername = "u" + System.currentTimeMillis();
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
