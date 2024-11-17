package guru.qa.niffler.service.impl;

import guru.qa.niffler.api.AuthApi;
import guru.qa.niffler.api.UserDataApi;
import guru.qa.niffler.api.core.RestClient.EmptyClient;
import guru.qa.niffler.api.core.ThreadSafeCookieStore;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.service.UsersClient;
import io.qameta.allure.Step;
import retrofit2.Response;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static guru.qa.niffler.utils.RandomDataUtils.randomUsername;
import static java.util.Objects.requireNonNull;
import static org.assertj.core.api.Assertions.assertThat;

public class UsersApiClient implements UsersClient {

  private static final Config CFG = Config.getInstance();
  private final static String DEFAULT_PASSWORD = "12345";

  private final UserDataApi userDataApi = new EmptyClient(CFG.userdataUrl()).create(UserDataApi.class);
  private final AuthApi authApi = new EmptyClient(CFG.authUrl()).create(AuthApi.class);

  @Step("[API] Create user with username: {username}")
  @Override
  public @Nonnull UserJson createUser(@Nonnull String username, @Nonnull String password) {
    try {
      authApi.requestRegisterForm().execute();
      authApi.register(
        username,
        password,
        password,
        ThreadSafeCookieStore.INSTANCE.cookieValue("XSRF-TOKEN")
      ).execute();
      // wait for user to be created
      Thread.sleep(50);
      return requireNonNull(userDataApi.currentUser(username).execute().body());
    } catch (IOException | InterruptedException e) {
      throw new RuntimeException(e);
    }
  }

  @Step("Create {count} income invitation(s) for {targetUser.username}")
  @Override
  public @Nonnull List<UserJson> addIncomeInvitation(@Nonnull UserJson targetUser, int count) {
    List<UserJson> result = new ArrayList<>();
    if (count > 0) {
      try {
        // check if user exists
        UserJson existingUser = userDataApi.currentUser(targetUser.username()).execute().body();
        if (existingUser == null || existingUser.id() == null) {
          throw new RuntimeException("User not found: " + targetUser.username());
        }
        for (int i = 0; i < count; i++) {
          final Response<UserJson> response;
          final UserJson newUser = createUser(randomUsername(), DEFAULT_PASSWORD);
          response = userDataApi.sendInvitation(newUser.username(), targetUser.username()).execute();
          assertThat(response.code()).isEqualTo(200);
          result.add(newUser);
        }
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
    return result;
  }

  @Step("Create {count} outcome invitation(s) for {targetUser.username}")
  @Override
  public @Nonnull List<UserJson> addOutcomeInvitation(@Nonnull UserJson targetUser, int count) {
    List<UserJson> result = new ArrayList<>();
    if (count > 0) {
      try {
        // check if user exists
        UserJson existingUser = userDataApi.currentUser(targetUser.username()).execute().body();
        if (existingUser == null || existingUser.id() == null) {
          throw new RuntimeException("User not found: " + targetUser.username());
        }
        for (int i = 0; i < count; i++) {
          final Response<UserJson> response;
          final UserJson newUser = createUser(randomUsername(), DEFAULT_PASSWORD);
          response = userDataApi.sendInvitation(targetUser.username(), newUser.username()).execute();
          assertThat(response.code()).isEqualTo(200);
          result.add(newUser);
        }
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
    return result;
  }

  @Step("Create {count} friend(s) for {targetUser.username}")
  @Override
  public @Nonnull List<UserJson> addFriend(@Nonnull UserJson targetUser, int count) {
    List<UserJson> result = new ArrayList<>();
    if (count > 0) {
      try {
        // check if user exists
        UserJson existingUser = userDataApi.currentUser(targetUser.username()).execute().body();
        if (existingUser == null || existingUser.id() == null) {
          throw new RuntimeException("User not found: " + targetUser.username());
        }
        for (int i = 0; i < count; i++) {
          final Response<UserJson> response;
          final UserJson newUser = createUser(randomUsername(), DEFAULT_PASSWORD);
          userDataApi.sendInvitation(newUser.username(), targetUser.username()).execute();
          response = userDataApi.acceptInvitation(targetUser.username(), newUser.username()).execute();
          assertThat(response.code()).isEqualTo(200);
          result.add(newUser);
        }
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
    return result;
  }

  @Step("Get all users")
  public @Nonnull List<UserJson> allUsers(String username, String searchQuery) {
    try {
      return requireNonNull(userDataApi.allUsers(username, searchQuery).execute().body());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
