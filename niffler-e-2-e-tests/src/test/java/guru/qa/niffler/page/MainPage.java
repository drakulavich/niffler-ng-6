package guru.qa.niffler.page;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.component.SpendingTable;
import guru.qa.niffler.page.component.StatComponent;
import io.qameta.allure.Step;
import lombok.Getter;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

@ParametersAreNonnullByDefault
public class MainPage extends BasePage<MainPage> {

  public static final String URL = CFG.frontUrl() + "main";

  private final SelenideElement stat = $("#stat");
  @Getter
  private final SpendingTable spendingTable = new SpendingTable();
  @Getter
  private final StatComponent statComponent = new StatComponent();

  @Step("Check that page is loaded")
  @Override
  @Nonnull
  public MainPage checkThatPageLoaded() {
    header.getSelf().should(visible).shouldHave(text("Niffler"));
    statComponent.getSelf().should(visible).shouldHave(text("Statistics"));
    spendingTable.getSelf().should(visible).shouldHave(text("History of Spendings"));
    return this;
  }

  @Nonnull
  @Step("Check that statistics is visible")
  public MainPage checkStatisticsVisible() {
    stat.shouldBe(visible);
    return this;
  }

  @Step("Wait for stats diagram rendering")
  public MainPage waitForStatsDiagramRendering() {
    Selenide.sleep(3000);
    return this;
  }
}
