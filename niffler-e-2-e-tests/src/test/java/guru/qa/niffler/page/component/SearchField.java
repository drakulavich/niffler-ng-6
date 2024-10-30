package guru.qa.niffler.page.component;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$;

public class SearchField {
  private final SelenideElement self = $("input[placeholder='Search']");
  private final SelenideElement clearSearchButton = $("button[id='input-clear']");

  public SearchField search(String query) {
    self.setValue(query).pressEnter();
    return this;
  }
  public SearchField clearIfNotEmpty() {
    if (!self.getValue().isEmpty()) {
      clearSearchButton.click();
    }
    return this;
  }
}
