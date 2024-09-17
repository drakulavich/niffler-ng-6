package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.BrowserExtension;
import guru.qa.niffler.jupiter.Category;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.ProfilePage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(BrowserExtension.class)
public class ProfileWebTest {
    private static final Config CFG = Config.getInstance();

    @Category(
        username = "duck",
        archived = true
    )
    @Test
    void archivedCategoryShouldPresentInCategoriesList(CategoryJson category) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
            .login("duck", "12345")
            .openProfile();

        new ProfilePage()
            .showArchivedCategories(true)
            .unarchiveCategory(category.name())
            .showArchivedCategories(false)
            .checkCategoryPresent(category.name());
    }

    @Category(
        username = "duck",
        archived = false
    )
    @Test
    void activeCategoryShouldPresentInCategoriesList(CategoryJson category) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
            .login("duck", "12345")
            .openProfile();

        new ProfilePage()
            .checkCategoryPresent(category.name())
            .archiveCategory(category.name())
            .checkCategoryNotPresent(category.name());
    }
}
