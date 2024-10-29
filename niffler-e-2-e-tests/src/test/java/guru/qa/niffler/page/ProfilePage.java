package guru.qa.niffler.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

@ParametersAreNonnullByDefault
public class ProfilePage {
    private final SelenideElement addCategoryInput = $("input[name='category']");
    private final SelenideElement showArchivedToggle = $("input[type='checkbox']");

    private final ElementsCollection categories = $$(".MuiChip-label");

    @Nonnull
    public ProfilePage addCategory(String categoryName) {
        addCategoryInput.setValue(categoryName).pressEnter();
        return this;
    }

    @Nonnull
    public ProfilePage showArchivedCategories() {
        // scroll to the top of the page to make the toggle visible
        Selenide.executeJavaScript("window.scrollTo(0, 0);");
        showArchivedToggle.click();

        return this;
    }

    @Nonnull
    public ProfilePage checkCategoryPresent(String categoryName) {
        categories.find(text(categoryName)).shouldBe(visible);
        return this;
    }

    @Nonnull
    public ProfilePage checkCategoryNotPresent(String categoryName) {
        categories.find(text(categoryName)).shouldNotBe(visible);
        return this;
    }

    @Nonnull
    public ProfilePage unarchiveCategory(String categoryName) {
        categories.find(text(categoryName)).parent().parent().$("button[aria-label='Unarchive category']").click();

        $(byText("Unarchive")).shouldBe(Condition.visible).click();
        return this;
    }

    @Nonnull
    public ProfilePage archiveCategory(String categoryName) {
        categories.find(text(categoryName)).parent().parent().$("button[aria-label='Archive category']").click();

        $(byText("Archive")).shouldBe(Condition.visible).click();
        return this;
    }
}
