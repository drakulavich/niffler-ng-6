package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class LoginPage {
  private final SelenideElement usernameInput = $("input[name='username']");
  private final SelenideElement passwordInput = $("input[name='password']");
  private final SelenideElement submitButton = $("button[type='submit']");
  private final SelenideElement registerButton = $(".form__register");
  private final SelenideElement signInButton = $(".form_sign-in");

  public MainPage login(String username, String password) {
    usernameInput.setValue(username);
    passwordInput.setValue(password);
    submitButton.click();
    return new MainPage();
  }

  public LoginPage setUsername(String username) {
    usernameInput.setValue(username);
    return this;
  }

  public LoginPage setPassword(String password) {
    passwordInput.setValue(password);
    return this;
  }

  public LoginPage submit() {
    submitButton.click();
    return this;
  }

  public void inputsAreVisible() {
    usernameInput.shouldBe(visible);
    passwordInput.shouldBe(visible);
    submitButton.shouldBe(visible);
  }

  public LoginPage clickSignInButton() {
    signInButton.click();
    return this;
  }

  public RegisterPage openRegisterPage() {
    registerButton.click();
    return new RegisterPage();
  }
}
