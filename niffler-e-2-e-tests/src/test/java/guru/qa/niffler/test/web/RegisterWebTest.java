package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.RegisterPage;
import org.junit.jupiter.api.Test;

public class RegisterWebTest extends BaseWebTest {
    private final LoginPage loginPage = new LoginPage();
    private final RegisterPage registerPage = new RegisterPage();

    @Test
    void shouldRegisterNewUser() {
        String newUsername = "u" + System.currentTimeMillis();
        String newPassword = "pa$$w0rd";

        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .openRegisterPage()
                .register(newUsername, newPassword)
                .clickSignInButton();

        loginPage
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
                .register(oldUsername, newPassword);

        registerPage.errorIsShown();
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
