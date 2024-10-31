package guru.qa.niffler.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.component.Alert;
import guru.qa.niffler.page.component.Header;
import io.qameta.allure.Step;
import lombok.Getter;

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
    private final SelenideElement nameField = $("input[name='name']");
    private final SelenideElement saveButton = $("button[type='submit']");
    private final ElementsCollection categories = $$(".MuiChip-label");
    @Getter
    private final Alert alert = new Alert();
    @Getter
    private final Header header = new Header();

    @Nonnull
    @Step("Add category {categoryName}")
    public ProfilePage addCategory(String categoryName) {
        addCategoryInput.setValue(categoryName).pressEnter();
        return this;
    }

    @Nonnull
    @Step("Show archived categories")
    public ProfilePage showArchivedCategories() {
        // scroll to the top of the page to make the toggle visible
        // Selenide.executeJavaScript("window.scrollTo(0, 0);");
        showArchivedToggle.click();
        return this;
    }

    @Nonnull
    @Step("Check that category {categoryName} is present")
    public ProfilePage checkCategoryPresent(String categoryName) {
        categories.find(text(categoryName)).shouldBe(visible);
        return this;
    }

    @Nonnull
    @Step("Check that category {categoryName} is not present")
    public ProfilePage checkCategoryNotPresent(String categoryName) {
        categories.find(text(categoryName)).shouldNotBe(visible);
        return this;
    }

    @Nonnull
    @Step("Unarchive category {categoryName}")
    public ProfilePage unarchiveCategory(String categoryName) {
        categories.find(text(categoryName)).parent().parent().$("button[aria-label='Unarchive category']").click();

        $(byText("Unarchive")).shouldBe(Condition.visible).click();
        return this;
    }

    @Nonnull
    @Step("Archive category {categoryName}")
    public ProfilePage archiveCategory(String categoryName) {
        categories.find(text(categoryName)).parent().parent().$("button[aria-label='Archive category']").click();

        $(byText("Archive")).shouldBe(Condition.visible).click();
        return this;
    }

    @Nonnull
    @Step("Update profle name to {newName}")
    public ProfilePage updateName(String newName) {
        nameField.setValue(newName);
        saveButton.click();
        return this;
    }

    @Nonnull
    @Step("Check that name is {expectedName}")
    public ProfilePage checkName(String expectedName) {
        nameField.shouldHave(Condition.value(expectedName));
        return this;
    }
}
