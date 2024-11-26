package guru.qa.niffler.jupiter.converter;

import com.codeborne.selenide.SelenideDriver;
import guru.qa.niffler.jupiter.extension.BrowserExtension;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.params.converter.ArgumentConversionException;
import org.junit.jupiter.params.converter.ArgumentConverter;

public class BrowserConverter implements ArgumentConverter {

  @Override
  public SelenideDriver convert(Object source, ParameterContext context) {
    if (!(source instanceof Browser)) {
      throw new ArgumentConversionException(
          "Cannot convert source object to Browser: " + source);
    }
    SelenideDriver driver = new SelenideDriver(((Browser) source).getSelenideConfig());
    BrowserExtension.setDriver(driver);
    return driver;
  }
}
