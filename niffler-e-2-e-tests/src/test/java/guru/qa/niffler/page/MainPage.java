package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class MainPage {
  private final ElementsCollection tableRows = $("#spendings tbody").$$("tr");

  private final SelenideElement stat = $("#stat");
  private final SelenideElement spendingsHistory = $("#spendings");
  private final SelenideElement menuButton = $("button[aria-label='Menu']");

  private final SelenideElement profileMenuLink = $("a[href='/profile']");
  private final SelenideElement friendsMenuLink = $("a[href='/people/friends']");
  private final SelenideElement searchInput = $("input[placeholder='Search']");

  public EditSpendingPage editSpending(String spendingDescription) {
    tableRows.find(text(spendingDescription)).$$("td").get(5).click();
    return new EditSpendingPage();
  }

  public void checkThatTableContainsSpending(String spendingDescription) {
    searchInput.setValue(spendingDescription).pressEnter();
    tableRows.find(text(spendingDescription)).shouldBe(visible);
  }

  public MainPage checkStatisticsVisible() {
    stat.shouldBe(visible);
    return this;
  }

  public MainPage checkSpendingsVisible() {
    spendingsHistory.shouldBe(visible);
    return this;
  }

  public ProfilePage openProfile() {
    menuButton.click();
    profileMenuLink.click();
    return new ProfilePage();
  }

  public FriendsPage openFriends() {
    menuButton.click();
    friendsMenuLink.click();
    return new FriendsPage();
  }
}
