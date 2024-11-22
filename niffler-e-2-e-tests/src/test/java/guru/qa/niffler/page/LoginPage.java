package guru.qa.niffler.page;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideDriver;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

@ParametersAreNonnullByDefault
public class LoginPage extends BasePage<LoginPage> {
  private final SelenideElement usernameInput;
  private final SelenideElement passwordInput;
  private final SelenideElement submitButton;
  private final SelenideElement registerButton;
  private final SelenideElement signInButton;

  public LoginPage(SelenideDriver driver) {
    super(driver);
    this.usernameInput = driver.$("input[name='username']");
    this.passwordInput = driver.$("input[name='password']");
    this.submitButton = driver.$("button[type='submit']");
    this.registerButton = driver.$(".form__register");
    this.signInButton = driver.$(".form_sign-in");
  }
  public LoginPage() {
    this.usernameInput = Selenide.$("input[name='username']");
    this.passwordInput = Selenide.$("input[name='password']");
    this.submitButton = Selenide.$("button[type='submit']");
    this.registerButton = Selenide.$(".form__register");
    this.signInButton = Selenide.$(".form_sign-in");
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
