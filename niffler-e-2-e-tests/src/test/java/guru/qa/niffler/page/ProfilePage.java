package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class ProfilePage {
    private final SelenideElement addCategoryInput = $("input[name='category']");
    private final SelenideElement showArchivedToggle = $("input[type='checkbox']");

    private final ElementsCollection categories = $$(".MuiChip-label");
    private final ElementsCollection archiveCategoryButtons = $$("button[aria-label='Archive category']");
    private final ElementsCollection editCategoryButtons = $$("button[aria-label='Edit category']");
    private final ElementsCollection unarchiveCategoryButtons = $$("button[aria-label='Unarchive category']");

    public ProfilePage addCategory(String categoryName) {
        addCategoryInput.setValue(categoryName).pressEnter();
        return this;
    }

    public ProfilePage showArchivedCategories() {
        // check if toggle active
        if (!showArchivedToggle.isSelected()) {
            showArchivedToggle.click();
        }
        return this;
    }

    public void checkCategoryPresent(String categoryName) {
        categories.find(text(categoryName)).shouldBe(visible);
    }
}
