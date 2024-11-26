package guru.qa.niffler.page;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideDriver;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.component.SpendingTable;
import guru.qa.niffler.page.component.StatComponent;
import io.qameta.allure.Step;
import lombok.Getter;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.Condition.visible;

@ParametersAreNonnullByDefault
public class MainPage extends BasePage<MainPage> {
  private final SelenideElement stat;

  @Getter
  private final SpendingTable spendingTable;
  @Getter
  private final StatComponent statComponent;

  public MainPage(SelenideDriver driver) {
    this.stat = driver.$("#stat");
    this.spendingTable = new SpendingTable(driver);
    this.statComponent = new StatComponent(driver);
  }

  public MainPage() {
    this.stat = Selenide.$("#stat");
    this.spendingTable = new SpendingTable();
    this.statComponent = new StatComponent();
  }

  @Nonnull
  @Step("Check that statistics is visible")
  public MainPage checkStatisticsVisible() {
    stat.shouldBe(visible);
    return this;
  }
}
