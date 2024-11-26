package guru.qa.niffler.page.component;

import com.codeborne.selenide.SelenideDriver;
import com.codeborne.selenide.SelenideElement;

public class BaseComponent<T extends BaseComponent<?>> {
  protected final SelenideElement self;
  protected final SelenideDriver driver;

  public BaseComponent(SelenideElement self) {
    this.self = self;
    this.driver = null;
  }

  public BaseComponent(SelenideDriver driver, SelenideElement self) {
    this.driver = driver;
    this.self = self;
  }
}
