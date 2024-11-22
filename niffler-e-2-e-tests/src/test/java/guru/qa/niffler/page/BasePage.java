package guru.qa.niffler.page;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideDriver;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.component.Header;
import io.qameta.allure.Step;
import lombok.Getter;

import javax.annotation.Nonnull;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;

public abstract class BasePage<T extends BasePage<?>> {
  @Getter
  protected final Header header = new Header();
  private final SelenideElement alert;
  private final ElementsCollection formErrors;

  protected BasePage(SelenideDriver driver) {
    this.alert = driver.$("[role='alert']");
    this.formErrors = driver.$$(".form__error");
  }
  public BasePage() {
    this.alert = Selenide.$("[role='alert']");
    this.formErrors = Selenide.$$(".form__error");
  }

  @Step("Check that alert contains text {message}")
  @SuppressWarnings("unchecked")
  public T checkAlert(String message) {
    alert.shouldHave(text(message));
    return (T) this;
  }

  @Step("Check that form error message appears: {expectedText}")
  @SuppressWarnings("unchecked")
  @Nonnull
  public T checkFormErrorMessage(String... expectedText) {
    formErrors.should(CollectionCondition.textsInAnyOrder(expectedText));
    return (T) this;
  }
}
