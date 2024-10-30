package guru.qa.niffler.page.component;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.CollectionCondition.empty;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class PeopleTable {
  private final SelenideElement self = $("tbody");
  private final ElementsCollection allPeopleRows = $("#all").$$("tr");
  private final ElementsCollection friendRows = $("#friends").$$("tr");
  private final ElementsCollection friendRequestsRows = $("#requests").$$("tr");

  public void checkOutcomeRequest(String name) {
    allPeopleRows.find(text(name)).$$("td").get(1).shouldHave(text("Waiting"));
  }

  public void checkFriend(String name) {
    friendRows.find(text(name)).should(visible);
  }

  public void checkIncomingRequest(String name) {
    friendRequestsRows.find(text(name)).should(visible);
  }

  public void checkEmpty() {
    allPeopleRows.shouldBe(empty);
  }
}
