package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.data.jdbc.Connections;
import guru.qa.niffler.data.jpa.EntityManagers;
import guru.qa.niffler.service.impl.SpendDbClient;
import guru.qa.niffler.service.impl.UsersDbClient;
import io.qameta.allure.Step;
import org.junit.jupiter.api.extension.ExtensionContext;

public class DatabasesExtension implements SuiteExtension {

  private final SpendDbClient spendDbClient = new SpendDbClient();
  private final UsersDbClient usersDbClient = new UsersDbClient();

  @Override
  public void afterSuite() {
    Connections.closeAllConnections();
    EntityManagers.closeAllEmfs();
  }

  @Step("Remove all spends and users before tests execution")
  @Override
  public void beforeSuite(ExtensionContext context) {
    spendDbClient.removeAllSpends();
    usersDbClient.removeAllUsers();
  }
}
