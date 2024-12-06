package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.jupiter.annotation.ApiLogin;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.annotation.ScreenShotTest;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.rest.UserJson;
import guru.qa.niffler.page.ProfilePage;
import org.junit.jupiter.api.Test;

import java.awt.image.BufferedImage;

public class ProfileWebTest extends BaseWebTest {

  @User(
    categories = {@Category(
      archived = true
    )}
  )
  @ApiLogin
  @Test
  void archivedCategoryShouldPresentInCategoriesList(UserJson user) {
    String categoryName = user.testData().categories().getFirst().name();

    Selenide.open(ProfilePage.URL, ProfilePage.class)
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
  @ApiLogin
  @Test
  void activeCategoryShouldPresentInCategoriesList(UserJson user) {
    String categoryName = user.testData().categories().getFirst().name();

    Selenide.open(ProfilePage.URL, ProfilePage.class)
      .checkCategoryPresent(categoryName)
      .archiveCategory(categoryName)
      .checkCategoryNotPresent(categoryName);
  }

  @User
  @ApiLogin
  @ScreenShotTest(value = "img/expected-avatar.png")
  void shouldUpdateProfileWithAllFieldsSet(BufferedImage expected) throws Exception {
    final String newName = "John Snow";

    ProfilePage profilePage = Selenide.open(ProfilePage.URL, ProfilePage.class)
      .uploadPhotoFromClasspath("img/cat.jpeg")
      .updateName(newName)
      .checkAlert("Profile successfully updated");

    Selenide.refresh();

    profilePage.checkName(newName)
      .checkAvatarExist()
      .checkAvatarMatch(expected);
  }
}
