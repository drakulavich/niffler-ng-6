package guru.qa.niffler.jupiter.extension;

import io.qameta.allure.Allure;
import io.qameta.allure.AllureLifecycle;
import io.qameta.allure.model.TestResult;
import lombok.SneakyThrows;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.UUID;

public class AllureBackendLogsExtension implements SuiteExtension {

  public static final String caseName = "Niffler backend logs";

  @SneakyThrows
  @Override
  public void afterSuite() {
    final AllureLifecycle allureLifecycle = Allure.getLifecycle();
    final String caseId = UUID.randomUUID().toString();
    allureLifecycle.scheduleTestCase(new TestResult().setUuid(caseId).setName(caseName));
    allureLifecycle.startTestCase(caseId);
    Map<String, String> serviceLogs = Map.of(
      "niffler-auth", "./logs/niffler-auth/app.log",
      "niffler-userdata", "./logs/niffler-userdata/app.log",
      "niffler-currency", "./logs/niffler-currency/app.log",
      "niffler-spend", "./logs/niffler-spend/app.log",
      "niffler-gateway", "./logs/niffler-gateway/app.log"
    );
    for (String service : serviceLogs.keySet()) {
      allureLifecycle.addAttachment(
        service + " log",
        "text/html",
        ".log",
        Files.newInputStream(
          Path.of(serviceLogs.get(service))
        )
      );
    }
    allureLifecycle.stopTestCase(caseId);
    allureLifecycle.writeTestCase(caseId);
  }
}
