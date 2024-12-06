package guru.qa.niffler.condition;

import com.codeborne.selenide.CheckResult;
import com.codeborne.selenide.Driver;
import com.codeborne.selenide.WebElementsCondition;
import guru.qa.niffler.model.rest.SpendJson;
import org.apache.commons.lang3.ArrayUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import static com.codeborne.selenide.CheckResult.accepted;

@ParametersAreNonnullByDefault
public class SpendConditions {

  @Nonnull
  public static WebElementsCondition spends(@Nonnull SpendJson... expectedSpends) {
    return new WebElementsCondition() {

      final int categoryPos = 1;
      final int amountPos = 2;
      final int descPos = 3;
      final int datePos = 4;
      final DecimalFormat decimalFormat = new DecimalFormat("#.##");
      final SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.ENGLISH);

      private final List<String> expectedValues = Arrays.stream(expectedSpends)
        .map(spend -> String.format("Category: %s, Amount: %s ₽, Description: %s, Date: %s",
          spend.category().name(),
          decimalFormat.format(spend.amount()),
          spend.description(),
          dateFormat.format(spend.spendDate())
        ))
        .toList();

      @Nonnull
      @Override
      public CheckResult check(Driver driver, List<WebElement> elements) {
        if (ArrayUtils.isEmpty(expectedSpends)) {
          throw new IllegalArgumentException("No expected spends provided. The array cannot be empty.");
        }
        if (elements.size() != expectedSpends.length) {
          String message = String.format("List size mismatch: expected %d, but found %d element(s).",
            expectedSpends.length, elements.size());
          throw new IllegalArgumentException(message);
        }

        List<String> actualSpends = new ArrayList<>();
        for (WebElement row : elements) {
          List<WebElement> cells = row.findElements(By.cssSelector("td"));

          String actualRow = String.format("Category: %s, Amount: %s, Description: %s, Date: %s",
            cells.get(categoryPos).getText(),
            cells.get(amountPos).getText(),
            cells.get(descPos).getText(),
            cells.get(datePos).getText()
          );
          actualSpends.add(actualRow);
        }
        boolean matches = actualSpends.containsAll(expectedValues);
        if (!matches) {
          throw new AssertionError("Actual spends:\n" + actualSpends + "\n do not match expected:\n" + expectedValues);
        }
        return accepted();
      }

      @Override
      public String toString() {
        return expectedValues.toString();
      }
    };
  }
}
