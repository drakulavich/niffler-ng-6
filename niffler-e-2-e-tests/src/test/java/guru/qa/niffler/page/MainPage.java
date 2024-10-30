package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.component.Header;
import guru.qa.niffler.page.component.SpendingTable;
import lombok.Getter;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

@ParametersAreNonnullByDefault
public class MainPage {
  private final SelenideElement stat = $("#stat");
  @Getter
  private final Header header = new Header();
  @Getter
  private final SpendingTable spendingTable = new SpendingTable();

  @Nonnull
  public MainPage checkStatisticsVisible() {
    stat.shouldBe(visible);
    return this;
  }
}
