package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import java.util.List;

import static com.codeborne.selenide.CollectionCondition.empty;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class FriendsPage {
    private final ElementsCollection friendsRows = $("#friends").$$("tr");
    private final ElementsCollection friendRequestsRows = $("#requests").$$("tr");
    private final ElementsCollection allPeopleRows = $("#all").$$("tr");

    private final SelenideElement friendsTab = $("a[href='/people/friends']");
    private final SelenideElement allPeopleTab = $("a[href='/people/all']");
    private final SelenideElement searchInput = $("input[placeholder='Search']");
    private final SelenideElement clearSearchButton = $("button[id='input-clear']");

    public void checkThatFriendsTableContainsFriends(List<String> friendName) {
        for (String name : friendName) {
            searchInput.setValue(name).pressEnter();
            friendsRows.find(text(name)).should(visible);
            clearSearchButton.click();
        }
    }

    public void checkThatFriendRequestsTableContainsFriends(List<String> friendName) {
        for (String name : friendName) {
            searchInput.setValue(name).pressEnter();
            friendRequestsRows.find(text(name)).should(visible);
            clearSearchButton.click();
        }
    }

    public void checkThatFriendsTableIsEmpty() {
        friendsRows.shouldBe(empty);
    }

    public void checkThatAllPeopleTableContainsOutcomeRequests(List<String> friendName) {
        for (String name : friendName) {
            searchInput.setValue(name).pressEnter();
            allPeopleRows.find(text(name)).$$("td").get(1).shouldHave(text("Waiting"));
            clearSearchButton.click();
        }
    }

    public FriendsPage openFriendsTab() {
        friendsTab.click();
        return this;
    }

    public FriendsPage openAllPeopleTab() {
        allPeopleTab.click();
        return this;
    }
}
