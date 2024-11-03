package guru.qa.niffler.page.component;

import io.qameta.allure.Step;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import static com.codeborne.selenide.Selenide.$;

@ParametersAreNonnullByDefault
public class Calendar extends BaseComponent<Calendar> {
  DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");

  public Calendar() {
    super($("input[name='date']"));
  }

  @Nonnull
  @Step("Select date in calendar")
  public Calendar selectDateInCalendar(Date date) {
    self.setValue(formatter.format(date.toInstant()));
    return this;
  }
}
