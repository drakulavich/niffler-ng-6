package guru.qa.niffler.condition;

import com.codeborne.selenide.CheckResult;
import com.codeborne.selenide.Driver;
import com.codeborne.selenide.WebElementCondition;
import com.codeborne.selenide.WebElementsCondition;
import org.apache.commons.lang3.ArrayUtils;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.WebElement;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.codeborne.selenide.CheckResult.accepted;
import static com.codeborne.selenide.CheckResult.rejected;

@ParametersAreNonnullByDefault
public class StatConditions {

  @Nonnull
  public static WebElementCondition color(Color expectedColor) {
    return new WebElementCondition("color " + expectedColor.rgb) {
      @NotNull
      @Override
      public CheckResult check(Driver driver, WebElement element) {
        final String rgba = element.getCssValue("background-color");
        return new CheckResult(
          expectedColor.rgb.equals(rgba),
          rgba
        );
      }
    };
  }

  @Nonnull
  public static WebElementsCondition statBubbles(@Nonnull Bubble... expectedBubbles) {
    return new WebElementsCondition() {

      private final String expectedValues = Arrays.stream(expectedBubbles)
        .map(b -> "Color: " + b.color().rgb + ", Text: " + b.text())
        .toList()
        .toString();

      @NotNull
      @Override
      public CheckResult check(Driver driver, List<WebElement> elements) {
        if (ArrayUtils.isEmpty(expectedBubbles)) {
          throw new IllegalArgumentException("No expected bubbles given");
        }
        if (expectedBubbles.length != elements.size()) {
          final String message = String.format("List size mismatch (expected: %s, actual: %s)", expectedBubbles.length, elements.size());
          return rejected(message, elements);
        }

        boolean passed = true;
        final List<String> actualBubbles = new ArrayList<>();
        for (int i = 0; i < elements.size(); i++) {
          final WebElement elementToCheck = elements.get(i);
          final Color colorToCheck = expectedBubbles[i].color();
          final String textToCheck = expectedBubbles[i].text();
          final String rgba = elementToCheck.getCssValue("background-color");
          final String text = elementToCheck.getText();
          actualBubbles.add("Color: " + rgba + ", Text: " + text);
          if (passed) {
            passed = colorToCheck.rgb.equals(rgba) && textToCheck.equals(text);
          }
        }
        if (!passed) {
          final String actualValues = actualBubbles.toString();
          final String message = String.format(
            "List bubbles mismatch (expected: %s, actual: %s)", expectedValues, actualValues
          );
          return rejected(message, actualValues);
        }
        return accepted();
      }

      @Override
      public String toString() {
        return expectedValues;
      }
    };
  }
}
