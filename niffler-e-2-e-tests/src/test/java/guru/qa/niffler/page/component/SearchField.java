package guru.qa.niffler.page.component;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import javax.annotation.Nonnull;

import static com.codeborne.selenide.Selenide.$;

public class SearchField {
  private final SelenideElement self = $("input[placeholder='Search']");
  private final SelenideElement clearSearchButton = $("button[id='input-clear']");

  @Nonnull
  @Step("Set search query {query}")
  public SearchField search(String query) {
    self.setValue(query).pressEnter();
    return this;
  }

  @Nonnull
  @Step("Clear search field")
  public SearchField clearIfNotEmpty() {
    if (!self.getValue().isEmpty()) {
      clearSearchButton.click();
    }
    return this;
  }
}
