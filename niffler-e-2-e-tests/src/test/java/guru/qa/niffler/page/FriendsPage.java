package guru.qa.niffler.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.component.PeopleTable;
import guru.qa.niffler.page.component.SearchField;
import io.qameta.allure.Step;
import lombok.Getter;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

import static com.codeborne.selenide.Selenide.$;

@ParametersAreNonnullByDefault
public class FriendsPage extends BasePage<FriendsPage> {

  public static final String URL = CFG.frontUrl() + "people/friends";

  private final SelenideElement peopleTab = $("a[href='/people/friends']");
  private final SelenideElement allTab = $("a[href='/people/all']");

  private final SearchField searchField = new SearchField();
  @Getter
  private final PeopleTable peopleTable = new PeopleTable();

  @Step("Check that the page is loaded")
  @Override
  @Nonnull
  public FriendsPage checkThatPageLoaded() {
    peopleTab.shouldBe(Condition.visible);
    allTab.shouldBe(Condition.visible);
    return this;
  }

  @Step("Check friends table contains {friendNames}")
  public void checkThatFriendsTableContainsFriends(List<String> friendNames) {
    for (String name : friendNames) {
      searchField.clearIfNotEmpty();
      searchField.search(name);
      peopleTable.checkFriend(name);
    }
  }

  @Step("Check friend requests table contains {friendNames}")
  public void checkThatFriendRequestsTableContainsFriends(List<String> friendNames) {
    for (String name : friendNames) {
      searchField.clearIfNotEmpty();
      searchField.search(name);
      peopleTable.checkIncomingRequest(name);
    }
  }
}
