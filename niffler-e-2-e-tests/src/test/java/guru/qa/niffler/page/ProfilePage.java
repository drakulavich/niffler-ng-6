package guru.qa.niffler.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.utils.ScreenDiffResult;
import io.qameta.allure.Step;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

import static com.codeborne.selenide.Condition.attributeMatching;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static org.junit.jupiter.api.Assertions.assertFalse;

@ParametersAreNonnullByDefault
public class ProfilePage extends BasePage<ProfilePage> {
    private final SelenideElement addCategoryInput = $("input[name='category']");
    private final SelenideElement showArchivedToggle = $("input[type='checkbox']");
    private final SelenideElement nameField = $("input[name='name']");
    private final SelenideElement saveButton = $("button[type='submit']");
    private final ElementsCollection categories = $$(".MuiChip-label");

    private final SelenideElement photoInput = $("input[type='file']");
    private final SelenideElement avatar = $("#image__input").parent().$("img");

    @Step("Upload avatar from classpath")
    @Nonnull
    public ProfilePage uploadPhotoFromClasspath(String path) {
      photoInput.uploadFromClasspath(path);
      return this;
    }

    @Step("Check avatar exist")
    @Nonnull
    public ProfilePage checkAvatarExist() {
      avatar.should(attributeMatching("src", "data:image.*"));
      return this;
    }

    @Step("Check avatar match")
    @Nonnull
    public ProfilePage checkAvatarMatch(BufferedImage expected) throws Exception {
      BufferedImage actual = ImageIO.read(avatar.screenshot());
      assertFalse(new ScreenDiffResult(actual, expected));
      return this;
    }

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
