package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

@ParametersAreNonnullByDefault
public class LoginPage extends BasePage<LoginPage> {

  public static final String URL = CFG.authUrl() + "login";

  private final SelenideElement usernameInput = $("input[name='username']");
  private final SelenideElement passwordInput = $("input[name='password']");
  private final SelenideElement submitButton = $("button[type='submit']");
  private final SelenideElement registerButton = $(".form__register");
  private final SelenideElement signInButton = $(".form_sign-in");

  @Step("Check that page is loaded")
  @Override
  @Nonnull
  public LoginPage checkThatPageLoaded() {
    usernameInput.should(visible);
    passwordInput.should(visible);
    return this;
  }

  @Nonnull
  @Step("Login with username {username} and password {password}")
  public MainPage login(String username, String password) {
    usernameInput.setValue(username);
    passwordInput.setValue(password);
    submitButton.click();
    return new MainPage();
  }

  @Nonnull
  @Step("Set username {username}")
  public LoginPage setUsername(String username) {
    usernameInput.setValue(username);
    return this;
  }

  @Nonnull
  @Step("Set password")
  public LoginPage setPassword(String password) {
    passwordInput.setValue(password);
    return this;
  }

  @Nonnull
  @Step("Submit login form")
  public LoginPage submit() {
    submitButton.click();
    return this;
  }

  @Step("Verify that login page is loaded")
  public void verifyIsLoaded() {
    usernameInput.shouldBe(visible);
    passwordInput.shouldBe(visible);
    submitButton.shouldBe(visible);
  }

  @Nonnull
  @Step("Click sign in button")
  public LoginPage clickSignInButton() {
    signInButton.click();
    return this;
  }

  @Nonnull
  @Step("Open register page")
  public RegisterPage openRegisterPage() {
    registerButton.click();
    return new RegisterPage();
  }
}
