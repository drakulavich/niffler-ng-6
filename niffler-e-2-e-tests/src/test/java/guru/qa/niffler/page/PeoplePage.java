package guru.qa.niffler.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.component.PeopleTable;
import guru.qa.niffler.page.component.SearchField;
import io.qameta.allure.Step;

import javax.annotation.Nonnull;
import java.util.List;

import static com.codeborne.selenide.Selenide.$;

public class PeoplePage extends BasePage<PeoplePage> {

  public static final String URL = CFG.frontUrl() + "people/all";

  private final SelenideElement peopleTab = $("a[href='/people/friends']");
  private final SelenideElement allTab = $("a[href='/people/all']");

  private final SearchField searchField = new SearchField();
  private final PeopleTable peopleTable = new PeopleTable();

  @Step("Check that the page is loaded")
  @Override
  @Nonnull
  public PeoplePage checkThatPageLoaded() {
    peopleTab.shouldBe(Condition.visible);
    allTab.shouldBe(Condition.visible);
    return this;
  }

  @Step("Check outcome requests from friends {friendNames}")
  public void checkOutcomeRequests(List<String> friendNames) {
    for (String name : friendNames) {
      searchField.clearIfNotEmpty();
      searchField.search(name);
      peopleTable.checkOutcomeRequest(name);
    }
  }
}
