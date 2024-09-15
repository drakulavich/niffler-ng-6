package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

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

    public void checkThatFriendsTableContainsFriend(String friendName) {
        friendsRows.find(text(friendName)).should(visible);
    }

    public void checkThatFriendRequestsTableContainsFriend(String friendName) {
        friendRequestsRows.find(text(friendName)).should(visible);
    }

    public void checkThatFriendsTableIsEmpty() {
        friendsRows.shouldBe(empty);
    }

    public void checkThatAllPeopleTableContainsOutcomeRequest(String friendName) {
        allPeopleRows.find(text(friendName)).$$("td").get(1).shouldHave(text("Waiting"));
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
