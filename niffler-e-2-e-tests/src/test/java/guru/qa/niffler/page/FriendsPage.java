package guru.qa.niffler.page;

import guru.qa.niffler.page.component.Alert;
import guru.qa.niffler.page.component.PeopleTable;
import guru.qa.niffler.page.component.SearchField;
import io.qameta.allure.Step;
import lombok.Getter;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@ParametersAreNonnullByDefault
public class FriendsPage {
    private final SearchField searchField = new SearchField();
    @Getter
    private final PeopleTable peopleTable = new PeopleTable();
    @Getter
    private final Alert alert = new Alert();

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
