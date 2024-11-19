package guru.qa.niffler.condition;

import com.codeborne.selenide.CheckResult;
import com.codeborne.selenide.Driver;
import com.codeborne.selenide.WebElementCondition;
import com.codeborne.selenide.WebElementsCondition;
import org.apache.commons.lang3.ArrayUtils;
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
      @Nonnull
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
        .map(bubble -> String.format("Color: %s, Text: %s", bubble.color().rgb, bubble.text()))
        .toList()
        .toString();

      @Nonnull
      @Override
      public CheckResult check(Driver driver, List<WebElement> elements) {
        if (ArrayUtils.isEmpty(expectedBubbles)) {
          throw new IllegalArgumentException("No expected bubbles provided. The array cannot be empty.");
        }

        if (elements.size() != expectedBubbles.length) {
          String message = String.format("List size mismatch: expected %d, but found %d element(s).",
            expectedBubbles.length, elements.size());
          throw new IllegalArgumentException(message);
        }

        List<String> actualBubbles = new ArrayList<>();
        boolean allMatch = true;
        for (int i = 0; i < elements.size(); i++) {
          WebElement element = elements.get(i);
          Bubble expected = expectedBubbles[i];

          String actualColor = element.getCssValue("background-color");
          String actualText = element.getText();

          actualBubbles.add(String.format("Color: %s, Text: %s", actualColor, actualText));
          if (!bubbleMatches(expected, actualColor, actualText)) {
            allMatch = false;
          }
        }

        if (!allMatch) {
          String message = String.format("Bubble mismatch detected! Expected: %s, Actual: %s",
            expectedValues, actualBubbles);
          return rejected(message, actualBubbles);
        }

        return accepted();
      }

      private boolean bubbleMatches(Bubble expected, String actualColor, String actualText) {
        return expected.color().rgb.equals(actualColor) && expected.text().equals(actualText);
      }

      @Override
      public String toString() {
        return expectedValues;
      }
    };
  }

  @Nonnull
  public static WebElementsCondition statBubblesInAnyOrder(@Nonnull Bubble... expectedBubbles) {
    return new WebElementsCondition() {

      private final String expectedValues = Arrays.stream(expectedBubbles)
        .map(bubble -> String.format("Color: %s, Text: %s", bubble.color().rgb, bubble.text()))
        .toList()
        .toString();

      @Nonnull
      @Override
      public CheckResult check(Driver driver, List<WebElement> elements) {
        if (ArrayUtils.isEmpty(expectedBubbles)) {
          throw new IllegalArgumentException("No expected bubbles provided. The array cannot be empty.");
        }

        if (elements.size() != expectedBubbles.length) {
          String message = String.format("List size mismatch: expected %d, but found %d element(s).",
            expectedBubbles.length, elements.size());
          throw new IllegalArgumentException(message);
        }

        List<String> actualBubbles = elements.stream()
          .map(element -> String.format("Color: %s, Text: %s",
            element.getCssValue("background-color"),
            element.getText()))
          .toList();

        List<String> expectedBubblesAsStrings = Arrays.stream(expectedBubbles)
          .map(bubble -> String.format("Color: %s, Text: %s", bubble.color().rgb, bubble.text()))
          .toList();

        boolean matches = actualBubbles.size() == expectedBubblesAsStrings.size() &&
          actualBubbles.containsAll(expectedBubblesAsStrings);

        if (!matches) {
          String message = String.format(
            "Bubbles mismatch! Expected (any order): %s, Actual: %s", expectedValues, actualBubbles
          );
          return rejected(message, actualBubbles);
        }

        return accepted();
      }

      @Override
      public String toString() {
        return expectedValues;
      }
    };
  }

  @Nonnull
  public static WebElementsCondition statBubblesContain(@Nonnull Bubble... expectedBubbles) {
    return new WebElementsCondition() {

      private final String expectedValues = Arrays.stream(expectedBubbles)
        .map(bubble -> String.format("Color: %s, Text: %s", bubble.color().rgb, bubble.text()))
        .toList()
        .toString();

      @Nonnull
      @Override
      public CheckResult check(Driver driver, List<WebElement> elements) {
        if (ArrayUtils.isEmpty(expectedBubbles)) {
          throw new IllegalArgumentException("No expected bubbles provided. The array cannot be empty.");
        }

        if (elements.isEmpty()) {
          throw new IllegalArgumentException("No elements found on the page. Expected bubbles: " + expectedValues);
        }

        List<String> actualBubbles = elements.stream()
          .map(element -> String.format("Color: %s, Text: %s",
            element.getCssValue("background-color"),
            element.getText()))
          .toList();

        List<String> expectedBubblesAsStrings = Arrays.stream(expectedBubbles)
          .map(bubble -> String.format("Color: %s, Text: %s", bubble.color().rgb, bubble.text()))
          .toList();

        boolean containsAll = actualBubbles.containsAll(expectedBubblesAsStrings);
        if (!containsAll) {
          String message = String.format("Bubbles mismatch! Expected to contain: %s, Actual: %s",
            expectedValues, actualBubbles
          );
          return rejected(message, actualBubbles);
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
