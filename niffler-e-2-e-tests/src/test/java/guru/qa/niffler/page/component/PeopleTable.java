package guru.qa.niffler.page.component;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.FriendsPage;
import io.qameta.allure.Step;

import javax.annotation.Nonnull;

import static com.codeborne.selenide.CollectionCondition.empty;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class PeopleTable {
  private final SelenideElement self = $("tbody");
  private final ElementsCollection allPeopleRows = $("#all").$$("tr");
  private final ElementsCollection friendRows = $("#friends").$$("tr");
  private final ElementsCollection friendRequestsRows = $("#requests").$$("tr");

  @Step("Check outcome request to {name}")
  public void checkOutcomeRequest(String name) {
    allPeopleRows.find(text(name)).$$("td").get(1).shouldHave(text("Waiting"));
  }

  @Step("Check friend {name} in the table")
  public void checkFriend(String name) {
    friendRows.find(text(name)).should(visible);
  }

  @Step("Check incoming request from {name}")
  public void checkIncomingRequest(String name) {
    friendRequestsRows.find(text(name)).should(visible);
  }

  @Step("Check that table is empty")
  public void checkEmpty() {
    allPeopleRows.shouldBe(empty);
  }

  @Nonnull
  @Step("Accept friend request from {name}")
  public FriendsPage acceptFriendRequest(String name) {
    friendRequestsRows.find(text(name)).$$("td").get(1)
      .$$("button").filterBy(text("Accept")).first().click();
    return new FriendsPage();
  }

  @Nonnull
  @Step("Decline friend request from {name}")
  public FriendsPage declineFriendRequest(String name) {
    friendRequestsRows.find(text(name)).$$("td").get(1)
      .$$("button").filterBy(text("Decline")).first().click();

    $("[role='dialog']").$$("button").filterBy(text("Decline")).first().click();
    return new FriendsPage();
  }
}
