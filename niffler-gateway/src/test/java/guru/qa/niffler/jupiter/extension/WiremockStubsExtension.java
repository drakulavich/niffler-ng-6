package guru.qa.niffler.jupiter.extension;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.common.Json;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.stubbing.StubMapping;
import guru.qa.niffler.jupiter.annotation.WiremockStubs;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.BeforeTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.platform.commons.support.AnnotationSupport;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class WiremockStubsExtension implements
  BeforeAllCallback,
  BeforeTestExecutionCallback,
  AfterEachCallback,
  AfterAllCallback {

  private final String basePath = "src/test/resources/wiremock/stubs/";
  private final WireMockServer wiremock = new WireMockServer(
    new WireMockConfiguration()
      .port(8093)
      .globalTemplating(true)
  );

  @Override
  public void beforeTestExecution(ExtensionContext extensionContext) {
    AnnotationSupport.findAnnotation(extensionContext.getRequiredTestMethod(), WiremockStubs.class)
      .ifPresent(wiremockStubs -> {
        for (String path : wiremockStubs.paths()) {
          try {
            String jsonContent = Files.readString(Paths.get(basePath + path));
            System.out.println(jsonContent);
            StubMapping stubMapping = Json.read(jsonContent, StubMapping.class);
            wiremock.addStubMapping(stubMapping);
          } catch (IOException e) {
            throw new RuntimeException(e);
          }
        }
      });
  }

  @Override
  public void afterEach(ExtensionContext extensionContext) {
    wiremock.resetMappings();
  }

  @Override
  public void beforeAll(ExtensionContext extensionContext) throws Exception {
    wiremock.start();
  }

  @Override
  public void afterAll(ExtensionContext extensionContext) throws Exception {
    wiremock.shutdown();
  }
}
