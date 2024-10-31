package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.Selenide.$;

@ParametersAreNonnullByDefault
public class EditSpendingPage {
  private final SelenideElement descriptionInput = $("#description");
  private final SelenideElement amountInput = $("#amount");
  private final SelenideElement categoryInput = $("#category");
  private final SelenideElement saveBtn = $("#save");

  @Nonnull
  @Step("Set new spending description {description}")
  public EditSpendingPage setNewSpendingDescription(String description) {
    descriptionInput.clear();
    descriptionInput.setValue(description);
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
  public void save() {
    saveBtn.click();
  }
}
