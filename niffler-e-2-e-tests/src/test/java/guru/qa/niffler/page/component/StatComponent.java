package guru.qa.niffler.page.component;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.condition.Bubble;
import guru.qa.niffler.condition.StatConditions;
import guru.qa.niffler.utils.ScreenDiffResult;
import io.qameta.allure.Step;
import lombok.NonNull;

import javax.annotation.Nonnull;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

import static com.codeborne.selenide.Selenide.$;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class StatComponent extends BaseComponent<StatComponent> {
  private final ElementsCollection bubbles = self.$("#legend-container").$$("li");
  private final SelenideElement widget = self.$("canvas[role='img']");

  public StatComponent() {
    super($("#stat"));
  }

  @Nonnull
  @Step("Check widget image")
  public StatComponent checkWidgetImage(BufferedImage expected) throws IOException {
    BufferedImage actual = ImageIO.read(widget.screenshot());
    assertFalse(new ScreenDiffResult(
      actual,
      expected
    ));
    return this;
  }

  @Step("Check bubbles list")
  @Nonnull
  public StatComponent checkBubbles(Bubble... expectedBubbles) {
    bubbles.should(StatConditions.statBubbles(expectedBubbles));
    return this;
  }

  @Step("Check bubbles list in any order")
  @NonNull
  public StatComponent checkBubblesAnyOrder(Bubble... expectedBubbles) {
    bubbles.should(StatConditions.statBubblesInAnyOrder(expectedBubbles));
    return this;
  }

  @Step("Check bubbles contain")
  @Nonnull
  public StatComponent checkBubblesContain(Bubble... expectedBubbles) {
    bubbles.should(StatConditions.statBubblesContain(expectedBubbles));
    return this;
  }
}
