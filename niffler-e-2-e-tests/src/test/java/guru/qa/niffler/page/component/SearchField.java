package guru.qa.niffler.page.component;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideDriver;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import javax.annotation.Nonnull;

import static com.codeborne.selenide.Selenide.$;

public class SearchField extends BaseComponent<SearchField> {
  private final SelenideElement clearSearchButton;

  public SearchField() {
    super($("input[placeholder='Search']"));
    this.clearSearchButton = Selenide.$("button[id='input-clear']");
  }

  public SearchField(SelenideDriver driver) {
    super(driver, driver.$("input[placeholder='Search']"));
    this.clearSearchButton = driver.$("button[id='input-clear']");
  }

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
