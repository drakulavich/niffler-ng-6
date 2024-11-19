package guru.qa.niffler.page.component;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.condition.Color;
import guru.qa.niffler.utils.ScreenDiffResult;
import io.qameta.allure.Step;

import javax.annotation.Nonnull;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static guru.qa.niffler.condition.StatConditions.color;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class StatComponent extends BaseComponent<StatComponent> {
  private final ElementsCollection bubbles = self.$("#legend-container").$$("li");
  private final SelenideElement widget = self.$("canvas[role='img']");

  public StatComponent() {
    super($("#stat"));
  }

  @Nonnull
  @Step("Check bubbles contain {bubbleNames}")
  public StatComponent checkBubblesContain(String... bubbleNames) {
    for (String bubbleName : bubbleNames) {
      bubbles.find(text(bubbleName)).shouldBe(visible);
    }
    return this;
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

  @Step("Check that stat bubbles contains colors {expectedColors}")
  @Nonnull
  public StatComponent checkBubblesColors(Color... expectedColors) {
    bubbles.should(color(expectedColors));
    return this;
  }
}
