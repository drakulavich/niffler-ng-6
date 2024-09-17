package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.jupiter.Category;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.ProfilePage;
import org.junit.jupiter.api.Test;

public class ProfileWebTest extends BaseWebTest {
    private static final String USERNAME = "bee";
    private static final String PASSWORD = "12345";

    private final ProfilePage profilePage = new ProfilePage();

    @Category(
        username = USERNAME,
        archived = true
    )
    @Test
    void archivedCategoryShouldPresentInCategoriesList(CategoryJson category) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
            .login(USERNAME, PASSWORD)
            .openProfile();

        profilePage
            .showArchivedCategories()
            .unarchiveCategory(category.name())
            .showArchivedCategories()
            .checkCategoryPresent(category.name());
    }

    @Category(
        username = USERNAME,
        archived = false
    )
    @Test
    void activeCategoryShouldPresentInCategoriesList(CategoryJson category) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
            .login(USERNAME, PASSWORD)
            .openProfile();

        profilePage
            .checkCategoryPresent(category.name())
            .archiveCategory(category.name())
            .checkCategoryNotPresent(category.name());
    }
}
