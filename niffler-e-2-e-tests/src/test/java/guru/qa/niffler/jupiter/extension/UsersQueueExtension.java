package guru.qa.niffler.jupiter.extension;

import io.qameta.allure.Allure;
import org.apache.commons.lang3.time.StopWatch;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.BeforeTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.junit.platform.commons.support.AnnotationSupport;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;

public class UsersQueueExtension implements
    BeforeTestExecutionCallback,
    AfterTestExecutionCallback,
    ParameterResolver {

  public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(UsersQueueExtension.class);

  public record StaticUser(
    String username,
    String password,
    String friend,
    String income,
    String outcome) {
  }

  private static final Queue<StaticUser> EMPTY_USERS = new ConcurrentLinkedQueue<>();
  private static final Queue<StaticUser> WITH_FRIEND_USERS = new ConcurrentLinkedQueue<>();
  private static final Queue<StaticUser> WITH_INCOME_REQUEST_USERS = new ConcurrentLinkedQueue<>();
  private static final Queue<StaticUser> WITH_OUTCOME_REQUEST_USERS = new ConcurrentLinkedQueue<>();

  static {
    EMPTY_USERS.add(new StaticUser("bee", "12345", null, null, null));
    WITH_FRIEND_USERS.add(new StaticUser("duck", "12345", "dima", null, null));
    WITH_INCOME_REQUEST_USERS.add(new StaticUser("dima", "12345", null, "barsik", null));
    WITH_OUTCOME_REQUEST_USERS.add(new StaticUser("barsik", "12345", null, null, "dima"));
  }

  @Target(ElementType.PARAMETER)
  @Retention(RetentionPolicy.RUNTIME)
  public @interface UserType {
    Type value() default Type.EMPTY;
    enum Type {
        EMPTY, WITH_FRIEND, WITH_INCOME_REQUEST, WITH_OUTCOME_REQUEST
    }
  }

  private static Queue<StaticUser> getQueueByUserType(UserType.Type type) {
    switch (type) {
      case EMPTY -> { return EMPTY_USERS; }
      case WITH_FRIEND -> { return WITH_FRIEND_USERS; }
      case WITH_INCOME_REQUEST -> { return WITH_INCOME_REQUEST_USERS; }
      case WITH_OUTCOME_REQUEST -> { return WITH_OUTCOME_REQUEST_USERS; }
      default -> { throw new IllegalArgumentException("Unknown user type: " + type); }
    }
  }

  @SuppressWarnings("unchecked")
  @Override
  public void beforeTestExecution(ExtensionContext context) {
    Arrays.stream(context.getRequiredTestMethod().getParameters())
        .filter(p -> AnnotationSupport.isAnnotated(p, UserType.class) && p.getType().equals(StaticUser.class))
        .map(p -> p.getAnnotation(UserType.class))
        .forEach (ut -> {
          Optional<StaticUser> user = Optional.empty();
          StopWatch sw = StopWatch.createStarted();
          while (user.isEmpty() && sw.getTime(TimeUnit.SECONDS) < 30) {
              var queue = getQueueByUserType(ut.value());
              user = Optional.ofNullable(queue.poll());
          }
          Allure.getLifecycle().updateTestCase(testCase ->
              testCase.setStart(new Date().getTime())
          );
          user.ifPresentOrElse(
              u ->
                ((Map<UserType, StaticUser>) context.getStore(NAMESPACE)
                  .getOrComputeIfAbsent(
                      context.getUniqueId(),
                      val -> new HashMap<>()
                  )).put(ut, u),
              () -> {
                throw new IllegalStateException("Can`t obtain user after 30s.");
              }
          );
        });
  }

  @SuppressWarnings("unchecked")
  @Override
  public void afterTestExecution(ExtensionContext context) {
    Map<UserType, StaticUser> map = context.getStore(NAMESPACE).get(
        context.getUniqueId(),
        Map.class
    );
    if (map == null) {
      return;
    }

    for (Map.Entry<UserType, StaticUser> e : map.entrySet()) {
        StaticUser user = e.getValue();
        UserType userType = e.getKey();
        System.out.println("Returning back to queue: " + user);
        var queue = getQueueByUserType(userType.value());
        queue.add(user);
    }
  }

  @Override
  public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
    return parameterContext.getParameter().getType().isAssignableFrom(StaticUser.class)
        && AnnotationSupport.isAnnotated(parameterContext.getParameter(), UserType.class);
  }

  @SuppressWarnings("unchecked")
  @Override
  public StaticUser resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
      Map<UserType, StaticUser> map = extensionContext.getStore(NAMESPACE).get(extensionContext.getUniqueId(), Map.class);
      return map.get(parameterContext.getParameter().getAnnotation(UserType.class));
  }
}
