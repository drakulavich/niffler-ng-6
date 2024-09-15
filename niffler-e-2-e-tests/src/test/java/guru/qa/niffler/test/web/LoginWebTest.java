package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.extension.BrowserExtension;
import guru.qa.niffler.jupiter.extension.UsersQueueExtension;
import guru.qa.niffler.jupiter.extension.UsersQueueExtension.StaticUser;
import guru.qa.niffler.jupiter.extension.UsersQueueExtension.UserType;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static guru.qa.niffler.jupiter.extension.UsersQueueExtension.UserType.Type.EMPTY;
import static guru.qa.niffler.jupiter.extension.UsersQueueExtension.UserType.Type.WITH_FRIEND;
import static guru.qa.niffler.jupiter.extension.UsersQueueExtension.UserType.Type.WITH_INCOME_REQUEST;

@ExtendWith({BrowserExtension.class, UsersQueueExtension.class})
public class LoginWebTest {
    private static final Config CFG = Config.getInstance();

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
        Selenide.sleep(1000);
        new LoginPage()
                .setUsername(emptyUser.username())
                .setPassword(emptyUser.password() + "_bad") // bad password
                .submit()
                .verifyIsLoaded();
    }
}
