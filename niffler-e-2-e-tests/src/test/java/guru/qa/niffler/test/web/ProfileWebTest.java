package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.annotation.ScreenShotTest;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.ProfilePage;
import org.junit.jupiter.api.Test;

import java.awt.image.BufferedImage;

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
  @ScreenShotTest(value = "img/expected-avatar.png")
  void shouldUpdateProfileWithAllFieldsSet(UserJson user, BufferedImage expected) throws Exception {
    final String newName = "John Snow";

    Selenide.open(CFG.frontUrl(), LoginPage.class)
      .login(user.username(), user.testData().password())
      .getHeader().toProfilePage()
      .uploadPhotoFromClasspath("img/cat.jpeg")
      .updateName(newName)
      .checkAlert("Profile successfully updated");

    Selenide.refresh();

    profilePage.checkName(newName)
      .checkAvatarExist()
      .checkAvatarMatch(expected);
  }
}
