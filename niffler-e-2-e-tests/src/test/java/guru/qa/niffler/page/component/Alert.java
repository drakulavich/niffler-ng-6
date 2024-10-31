package guru.qa.niffler.page.component;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;

public class Alert {
  private final SelenideElement self = $("[role='alert']");

  @Step("Check that alert contains text {expectedText}")
  public void alertContains(String expectedText) {
    self.shouldHave(text(expectedText));
  }
}
