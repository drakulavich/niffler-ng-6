package guru.qa.niffler.page;

import guru.qa.niffler.page.component.PeopleTable;
import guru.qa.niffler.page.component.SearchField;
import io.qameta.allure.Step;

import java.util.List;

public class PeoplePage extends BasePage<PeoplePage> {
  private final SearchField searchField = new SearchField();
  private final PeopleTable peopleTable = new PeopleTable();

  @Step("Check outcome requests from friends {friendNames}")
  public void checkOutcomeRequests(List<String> friendNames) {
    for (String name : friendNames) {
      searchField.clearIfNotEmpty();
      searchField.search(name);
      peopleTable.checkOutcomeRequest(name);
    }
  }
}
