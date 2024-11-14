package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.component.SpendingTable;
import guru.qa.niffler.page.component.StatComponent;
import io.qameta.allure.Step;
import lombok.Getter;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

@ParametersAreNonnullByDefault
public class MainPage extends BasePage<MainPage> {
  private final SelenideElement stat = $("#stat");
  @Getter
  private final SpendingTable spendingTable = new SpendingTable();
  @Getter
  private final StatComponent statComponent = new StatComponent();

  @Nonnull
  @Step("Check that statistics is visible")
  public MainPage checkStatisticsVisible() {
    stat.shouldBe(visible);
    return this;
  }
}
