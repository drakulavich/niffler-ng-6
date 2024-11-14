package guru.qa.niffler.page.component;

import com.codeborne.selenide.ElementsCollection;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class StatComponent extends BaseComponent<StatComponent> {
  public StatComponent() {
    super($("#stat"));
  }
  private final ElementsCollection bubbles = self.$("#legend-container").$$("li");

  public void checkBubblesContain(String... bubbleNames) {
    for (String bubbleName : bubbleNames) {
      bubbles.find(text(bubbleName)).shouldBe(visible);
    }
  }
}
