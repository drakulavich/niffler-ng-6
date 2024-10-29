package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

@ParametersAreNonnullByDefault
public class RegisterPage {

    private final SelenideElement usernameInput = $("input[name='username']");
    private final SelenideElement passwordInput = $("input[name='password']");
    private final SelenideElement passwordSubmitInput = $("input[name='passwordSubmit']");
    private final SelenideElement signUpButton = $("button[type='submit']");

    private final SelenideElement errorMessage = $(".form__error");

    @Nonnull
    public RegisterPage setUsername(String username) {
        usernameInput.setValue(username);
        return this;
    }

    @Nonnull
    public RegisterPage setPassword(String password) {
        passwordInput.setValue(password);
        return this;
    }

    @Nonnull
    public RegisterPage setPasswordSubmit(String password) {
        passwordSubmitInput.setValue(password);
        return this;
    }

    @Nonnull
    public LoginPage submitRegistration() {
        signUpButton.click();
        return new LoginPage();
    }

    @Nonnull
    public RegisterPage clickSignUpButton() {
        signUpButton.click();
        return this;
    }

    @Nonnull
    public LoginPage register(String username, String password) {
        setUsername(username);
        setPassword(password);
        setPasswordSubmit(password);
        signUpButton.click();

        return new LoginPage();
    }

    public void errorIsShown() {
        errorMessage.shouldBe(visible);
    }
}
