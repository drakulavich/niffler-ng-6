package guru.qa.niffler.jupiter.converter;

import com.codeborne.selenide.SelenideConfig;

public enum Browser {
  CHROME("chrome"),
  FIREFOX("firefox"),
  SAFARI("safari");

  private final String browserName;

  Browser(String browserName) {
    this.browserName = browserName;
  }

  public SelenideConfig getSelenideConfig() {
    return new SelenideConfig()
      .browser(browserName)
      .pageLoadStrategy("eager")
      .timeout(5000L);
  }
}
