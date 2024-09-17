package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class RegisterPage {

    private final SelenideElement usernameInput = $("input[name='username']");
    private final SelenideElement passwordInput = $("input[name='password']");
    private final SelenideElement passwordSubmitInput = $("input[name='passwordSubmit']");
    private final SelenideElement signUpButton = $("button[type='submit']");

    private final SelenideElement errorMessage = $(".form__error");

    public RegisterPage setUsername(String username) {
        usernameInput.setValue(username);
        return this;
    }
    public RegisterPage setPassword(String password) {
        passwordInput.setValue(password);
        return this;
    }
    public RegisterPage setPasswordSubmit(String password) {
        passwordSubmitInput.setValue(password);
        return this;
    }

    public LoginPage submitRegistration() {
        signUpButton.click();
        return new LoginPage();
    }

    public RegisterPage clickSignUpButton() {
        signUpButton.click();
        return this;
    }

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
