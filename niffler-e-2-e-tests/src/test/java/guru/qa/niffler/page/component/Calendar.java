package guru.qa.niffler.page.component;

import com.codeborne.selenide.SelenideElement;

import javax.annotation.ParametersAreNonnullByDefault;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import static com.codeborne.selenide.Selenide.$;

@ParametersAreNonnullByDefault
public class Calendar {
  private final SelenideElement self = $("input[name='date']");
  DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");

  public Calendar selectDateInCalendar(Date date) {
    self.setValue(formatter.format(date.toInstant()));
    return this;
  }
}
