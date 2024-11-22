package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

@ParametersAreNonnullByDefault
public class RegisterPage extends BasePage<RegisterPage> {

    private final SelenideElement usernameInput = $("input[name='username']");
    private final SelenideElement passwordInput = $("input[name='password']");
    private final SelenideElement passwordSubmitInput = $("input[name='passwordSubmit']");
    private final SelenideElement signUpButton = $("button[type='submit']");

    @Nonnull
    @Step("Set username {username}")
    public RegisterPage setUsername(String username) {
        usernameInput.setValue(username);
        return this;
    }

    @Nonnull
    @Step("Set password")
    public RegisterPage setPassword(String password) {
        passwordInput.setValue(password);
        return this;
    }

    @Nonnull
    @Step("Set submit password")
    public RegisterPage setPasswordSubmit(String password) {
        passwordSubmitInput.setValue(password);
        return this;
    }

    @Nonnull
    @Step("Submit registration")
    public LoginPage submitRegistration() {
        signUpButton.click();
        return new LoginPage();
    }

    @Nonnull
    @Step("Click sign up button")
    public RegisterPage clickSignUpButton() {
        signUpButton.click();
        return this;
    }

    @Nonnull
    @Step("Register with username {username} and password {password}")
    public LoginPage register(String username, String password) {
        setUsername(username);
        setPassword(password);
        setPasswordSubmit(password);
        signUpButton.click();

        return new LoginPage();
    }
}
