package guru.qa.niffler.page;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.page.component.Header;
import io.qameta.allure.Step;
import lombok.Getter;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public abstract class BasePage<T extends BasePage<?>> {

  protected static final Config CFG = Config.getInstance();
  @Getter
  protected final Header header = new Header();
  protected final SelenideElement alert = $("[role='alert']");
  private final SelenideElement errorMessage = $(".form__error");

  public abstract T checkThatPageLoaded();

  @Step("Check that alert contains text {message}")
  @SuppressWarnings("unchecked")
  public T checkAlert(String message) {
    alert.shouldHave(text(message));
    return (T) this;
  }

  @Step("Error is shown")
  public T errorIsShown() {
    errorMessage.shouldBe(visible);
    return (T) this;
  }

  @Step("Wait for component rendering")
  public T waitForComponentRendering() {
    Selenide.sleep(3000);
    return (T) this;
  }
}
