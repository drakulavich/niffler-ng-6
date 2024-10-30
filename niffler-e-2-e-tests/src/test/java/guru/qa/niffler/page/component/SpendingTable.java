package guru.qa.niffler.page.component;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.EditSpendingPage;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class SpendingTable {
  private final SelenideElement self = $("#spendings tbody");
  private final ElementsCollection rows = self.$$("tr");

  private final SearchField searchField = new SearchField();

  public SpendingTable selectPeriod(DataFilterValues period) {
    $("#period").selectOption(period.getValue());
    return this;
  }

  public EditSpendingPage editSpending(String description) {
    rows.find(text(description)).$$("td").get(5).click();
    return new EditSpendingPage();
  }

  public SpendingTable deleteSpending(String description) {
    rows.find(text(description)).$$("td").get(0).setSelected(true);
    $("#delete").click();
    return this;
  }

  public SpendingTable searchSpendingByDescription(String description) {
    searchField.search(description);
    return this;
  }

  public SpendingTable checkTableContains(String... expectedSpends) {
    for (String expectedSpend : expectedSpends) {
      searchField.clearIfNotEmpty();
      searchSpendingByDescription(expectedSpend);
      rows.find(text(expectedSpend)).shouldBe(visible);
    }
    return this;
  }

  public SpendingTable checkTableSize(int expectedSize) {
    rows.shouldHave(size(expectedSize));
    return this;
  }
}
