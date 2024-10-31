package guru.qa.niffler.page.component;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import static com.codeborne.selenide.Selenide.$;

@ParametersAreNonnullByDefault
public class Calendar {
  private final SelenideElement self = $("input[name='date']");
  DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");

  @Nonnull
  @Step("Select date in calendar")
  public Calendar selectDateInCalendar(Date date) {
    self.setValue(formatter.format(date.toInstant()));
    return this;
  }
}
