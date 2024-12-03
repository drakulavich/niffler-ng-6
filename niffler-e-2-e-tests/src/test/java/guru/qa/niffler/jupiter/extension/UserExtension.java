package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.TestData;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.service.UsersClient;
import guru.qa.niffler.service.impl.UsersDbClient;
import guru.qa.niffler.utils.RandomDataUtils;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.junit.platform.commons.support.AnnotationSupport;

import java.util.ArrayList;
import java.util.List;

public class UserExtension implements BeforeEachCallback, ParameterResolver {

  public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(UserExtension.class);
  private static final String defaultPassword = "12345";

  private final UsersClient usersClient = new UsersDbClient();

  @Override
  public void beforeEach(ExtensionContext context) throws Exception {
    AnnotationSupport.findAnnotation(context.getRequiredTestMethod(), User.class)
        .ifPresent(anno -> {
          final String username = "".equals(anno.username())
            ? RandomDataUtils.randomUsername()
            : anno.username();
          UserJson testUser = usersClient.createUser(username, defaultPassword);
          List<UserJson> incomeInvitations = usersClient.addIncomeInvitation(testUser, anno.income());
          List<UserJson> outcomeInvitations = usersClient.addOutcomeInvitation(testUser, anno.outcome());
          List<UserJson> friends = usersClient.addFriend(testUser, anno.friends());

          testUser = testUser.addTestData(
            new TestData(
              defaultPassword,
              new ArrayList<>(),
              new ArrayList<>(),
              incomeInvitations.stream().map(UserJson::username).toList(),
              outcomeInvitations.stream().map(UserJson::username).toList(),
              friends.stream().map(UserJson::username).toList()
            )
          );
          setUser(testUser);
        });
  }

  @Override
  public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
    return parameterContext.getParameter().getType().isAssignableFrom(UserJson.class);
  }

  @Override
  public UserJson resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
    return getUserJson();
  }

  public static void setUser(UserJson testUser) {
    final ExtensionContext context = TestMethodContextExtension.context();
    context.getStore(NAMESPACE).put(
      context.getUniqueId(),
      testUser
    );
  }
  public static UserJson getUserJson() {
    final ExtensionContext context = TestMethodContextExtension.context();
    return context.getStore(NAMESPACE).get(context.getUniqueId(), UserJson.class);
  }
}
