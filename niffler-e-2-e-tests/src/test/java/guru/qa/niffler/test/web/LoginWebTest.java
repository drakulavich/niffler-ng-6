package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.jupiter.extension.UsersQueueExtension.StaticUser;
import guru.qa.niffler.jupiter.extension.UsersQueueExtension.UserType;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.Test;

import static guru.qa.niffler.jupiter.extension.UsersQueueExtension.UserType.Type.EMPTY;
import static guru.qa.niffler.jupiter.extension.UsersQueueExtension.UserType.Type.WITH_FRIEND;
import static guru.qa.niffler.jupiter.extension.UsersQueueExtension.UserType.Type.WITH_INCOME_REQUEST;

public class LoginWebTest extends BaseWebTest {
    private final LoginPage loginPage = new LoginPage();

    @Test
    void mainPageShouldBeDisplayedAfterSuccessLogin(@UserType(WITH_FRIEND) StaticUser user) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(user.username(), user.password())
                .checkSpendingsVisible()
                .checkStatisticsVisible();
    }

    @Test
    void userShouldStayOnLoginPageAfterLoginWithBadCredentials(
            @UserType(WITH_INCOME_REQUEST) StaticUser user,
            @UserType(EMPTY) StaticUser emptyUser
    ) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .setUsername(user.username())
                .setPassword(user.password() + "_bad") // bad password
                .submit()
                .verifyIsLoaded();

        loginPage
                .setUsername(emptyUser.username())
                .setPassword(emptyUser.password() + "_bad") // bad password
                .submit()
                .verifyIsLoaded();
    }
}
