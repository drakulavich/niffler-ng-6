package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.ProfilePage;
import org.junit.jupiter.api.Test;

public class ProfileWebTest extends BaseWebTest {
  private final ProfilePage profilePage = new ProfilePage();

  @User(
    categories = {@Category(
      archived = true
    )}
  )
  @Test
  void archivedCategoryShouldPresentInCategoriesList(UserJson user) {
    Selenide.open(CFG.frontUrl(), LoginPage.class)
      .login(user.username(), user.testData().password())
      .getHeader().toProfilePage();

    String categoryName = user.testData().categories().getFirst().name();
    profilePage
      .showArchivedCategories()
      .unarchiveCategory(categoryName)
      .showArchivedCategories()
      .checkCategoryPresent(categoryName);
  }

  @User(
    categories = {@Category(
      archived = false
    )}
  )
  @Test
  void activeCategoryShouldPresentInCategoriesList(UserJson user) {
    Selenide.open(CFG.frontUrl(), LoginPage.class)
      .login(user.username(), user.testData().password())
      .getHeader().toProfilePage();

    String categoryName = user.testData().categories().getFirst().name();
    profilePage
      .checkCategoryPresent(categoryName)
      .archiveCategory(categoryName)
      .checkCategoryNotPresent(categoryName);
  }

  @User
  @Test
  void userCanUpdateProfile(UserJson user) {
    Selenide.open(CFG.frontUrl(), LoginPage.class)
      .login(user.username(), user.testData().password())
      .getHeader().toProfilePage()
      .updateName("John Snow")
      .checkAlert("Profile successfully updated")
      .getHeader().toMainPage()
      .getHeader().toProfilePage().checkName("John Snow");
  }
}
