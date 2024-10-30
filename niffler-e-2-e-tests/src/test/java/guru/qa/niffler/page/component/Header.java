package guru.qa.niffler.page.component;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.EditSpendingPage;
import guru.qa.niffler.page.FriendsPage;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.MainPage;
import guru.qa.niffler.page.PeoplePage;
import guru.qa.niffler.page.ProfilePage;

import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

@ParametersAreNonnullByDefault
public class Header {
  private final SelenideElement self = $("#root header");
  private final SelenideElement menuButton = self.$("button[aria-label='Menu']");
  private final ElementsCollection menuItems = $$("#account-menu li");

  public void checkHeaderText() {
    self.$("h1").shouldHave(text("Niffler"));
  }

  public FriendsPage toFriendsPage() {
    menuButton.click();
    menuItems.find(text("Friends")).click();
    return new FriendsPage();
  }

  public PeoplePage toAllPeoplesPage() {
    menuButton.click();
    menuItems.find(text("All People")).click();
    return new PeoplePage();
  }

  public ProfilePage toProfilePage() {
    menuButton.click();
    menuItems.find(text("Profile")).click();
    return new ProfilePage();
  }

  public LoginPage signOut() {
    menuButton.click();
    menuItems.find(text("Sign out")).click();
    return new LoginPage();
  }

  public EditSpendingPage addSpendingPage() {
    self.find(byText("New spending")).click();
    return new EditSpendingPage();
  }

  public MainPage toMainPage() {
    self.$("h1").find(byText("Niffler")).click();
    return new MainPage();
  }

}
