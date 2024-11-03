package guru.qa.niffler.page.component;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.EditSpendingPage;
import guru.qa.niffler.page.FriendsPage;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.MainPage;
import guru.qa.niffler.page.PeoplePage;
import guru.qa.niffler.page.ProfilePage;
import io.qameta.allure.Step;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

@ParametersAreNonnullByDefault
public class Header extends BaseComponent<Header> {
  private final SelenideElement menuButton = self.$("button[aria-label='Menu']");
  private final ElementsCollection menuItems = $$("#account-menu li");

  public Header() {
    super($("#root header"));
  }

  @Nonnull
  @Step("Go to friends page")
  public FriendsPage toFriendsPage() {
    menuButton.click();
    menuItems.find(text("Friends")).click();
    return new FriendsPage();
  }

  @Nonnull
  @Step("Go to all peoples page")
  public PeoplePage toAllPeoplesPage() {
    menuButton.click();
    menuItems.find(text("All People")).click();
    return new PeoplePage();
  }

  @Nonnull
  @Step("Go to profile page")
  public ProfilePage toProfilePage() {
    menuButton.click();
    menuItems.find(text("Profile")).click();
    return new ProfilePage();
  }

  @Nonnull
  @Step("Sign out")
  public LoginPage signOut() {
    menuButton.click();
    menuItems.find(text("Sign out")).click();
    return new LoginPage();
  }

  @Nonnull
  @Step("Add new spending")
  public EditSpendingPage addSpendingPage() {
    self.find(byText("New spending")).click();
    return new EditSpendingPage();
  }

  @Nonnull
  @Step("Go to main page")
  public MainPage toMainPage() {
    self.find(byText("Niffler")).click();
    return new MainPage();
  }

}
