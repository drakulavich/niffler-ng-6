package guru.qa.niffler.page.component;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideDriver;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.page.EditSpendingPage;
import io.qameta.allure.Step;

import javax.annotation.Nonnull;

import static com.codeborne.selenide.ClickOptions.usingJavaScript;
import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static guru.qa.niffler.condition.SpendConditions.spends;

public class SpendingTable extends BaseComponent<SpendingTable> {
  private final ElementsCollection rows;
  private final SearchField searchField;
  private final SelenideElement popup;
  private final SelenideElement deleteBtn;

  public SpendingTable() {
    super($("#spendings"));
    this.rows = self.$("tbody").$$("tr");
    this.searchField = new SearchField();
    this.popup = $("div[role='dialog']");
    this.deleteBtn = self.$("#delete");
  }

  public SpendingTable(SelenideDriver driver) {
    super(driver, driver.$("#spendings"));
    this.rows = self.$("tbody").$$("tr");
    this.searchField = new SearchField(driver);
    this.popup = driver.$("div[role='dialog']");
    this.deleteBtn = self.$("#delete");
  }

  @Nonnull
  @Step("Select period {period}")
  public SpendingTable selectPeriod(DataFilterValues period) {
    $("#period").selectOption(period.getValue());
    return this;
  }

  @Nonnull
  @Step("Edit spending {description}")
  public EditSpendingPage editSpending(String description) {
    rows.find(text(description)).$$("td").get(5).click();
    return new EditSpendingPage();
  }

  @Nonnull
  @Step("Delete spending {description}")
  public SpendingTable deleteSpending(String description) {
    rows.find(text(description)).$$("td").get(0).click();
    deleteBtn.click();
    popup.$(byText("Delete")).click(usingJavaScript());
    return this;
  }

  @Nonnull
  @Step("Search spending by description {description}")
  public SpendingTable searchSpendingByDescription(String description) {
    searchField.search(description);
    return this;
  }

  @Nonnull
  @Step("Check table contains spendings {expectedSpends}")
  public SpendingTable checkTableContains(String... expectedSpends) {
    for (String expectedSpend : expectedSpends) {
      searchField.clearIfNotEmpty();
      searchSpendingByDescription(expectedSpend);
      rows.find(text(expectedSpend)).shouldBe(visible);
    }
    return this;
  }

  @Nonnull
  @Step("Check table size is {expectedSize}")
  public SpendingTable checkTableSize(int expectedSize) {
    rows.shouldHave(size(expectedSize));
    return this;
  }

  @Nonnull
  @Step("Check table spendings {expectedSpends}")
  public SpendingTable checkTableSpendings(SpendJson... expectedSpends) {
    rows.should(spends(expectedSpends));
    return this;
  }
}
