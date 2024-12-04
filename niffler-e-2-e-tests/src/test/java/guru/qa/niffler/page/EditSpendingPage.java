package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

@ParametersAreNonnullByDefault
public class EditSpendingPage extends BasePage<EditSpendingPage> {

  public static final String URL = CFG.frontUrl() + "spending";

  private final SelenideElement descriptionInput = $("#description");
  private final SelenideElement amountInput = $("#amount");
  private final SelenideElement categoryInput = $("#category");
  private final SelenideElement saveBtn = $("#save");

  @Override
  @Nonnull
  public EditSpendingPage checkThatPageLoaded() {
    amountInput.should(visible);
    return this;
  }

  @Nonnull
  @Step("Set new spending description {description}")
  public EditSpendingPage setNewSpendingDescription(String description) {
    descriptionInput.clear();
    descriptionInput.setValue(description);
    return this;
  }

  @Nonnull
  @Step("Set new spending amount {amount}")
  public EditSpendingPage setNewSpendingAmount(int amount) {
    amountInput.clear();
    amountInput.setValue(String.valueOf(amount));
    return this;
  }

  @Nonnull
  @Step("Create new spending")
  public MainPage createSpend(String description, String category, int amount) {
    descriptionInput.setValue(description);
    categoryInput.setValue(category);
    amountInput.setValue(String.valueOf(amount));
    save();
    return new MainPage();
  }

  @Step("Save spending")
  public MainPage save() {
    saveBtn.click();
    return new MainPage();
  }
}
