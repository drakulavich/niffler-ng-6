package guru.qa.niffler.page;

import guru.qa.niffler.page.component.PeopleTable;
import guru.qa.niffler.page.component.SearchField;

import java.util.List;

public class PeoplePage {
  private final SearchField searchField = new SearchField();
  private final PeopleTable peopleTable = new PeopleTable();

  public void checkOutcomeRequests(List<String> friendNames) {
    for (String name : friendNames) {
      searchField.clearIfNotEmpty();
      searchField.search(name);
      peopleTable.checkOutcomeRequest(name);
    }
  }
}
