package guru.qa.niffler.api;

import guru.qa.niffler.api.core.RestClient;
import guru.qa.niffler.model.UserJson;
import retrofit2.Response;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserDataApiClient extends RestClient {

  private final UserDataApi userDataApi;

  public UserDataApiClient() {
    super(CFG.userdataUrl());
    this.userDataApi = retrofit.create(UserDataApi.class);
  }

  public UserJson currentUser(String username) {
    final Response<UserJson> response;
    try {
      response = userDataApi.currentUser(username).execute();
    } catch (IOException e) {
      throw new AssertionError(e);
    }
    assertEquals(200, response.code());
    return response.body();
  }

  public List<UserJson> allUsers(String username, String searchQuery) {
    final Response<List<UserJson>> response;
    try {
      response = userDataApi.allUsers(username, searchQuery).execute();
    } catch (IOException e) {
      throw new AssertionError(e);
    }
    assertEquals(200, response.code());
    return response.body();
  }

  public UserJson updateUser(UserJson user) {
    final Response<UserJson> response;
    try {
      response = userDataApi.updateUserInfo(user).execute();
    } catch (IOException e) {
      throw new AssertionError(e);
    }
    assertEquals(200, response.code());
    return response.body();
  }

  public UserJson sendInvitation(String username, String targetUsername) {
    final Response<UserJson> response;
    try {
      response = userDataApi.sendInvitation(username, targetUsername).execute();
    } catch (IOException e) {
      throw new AssertionError(e);
    }
    assertEquals(200, response.code());
    return response.body();
  }

  public UserJson acceptInvitation(String username, String targetUsername) {
    final Response<UserJson> response;
    try {
      response = userDataApi.acceptInvitation(username, targetUsername).execute();
    } catch (IOException e) {
      throw new AssertionError(e);
    }
    assertEquals(200, response.code());
    return response.body();
  }

  public UserJson declineInvitation(String username, String targetUsername) {
    final Response<UserJson> response;
    try {
      response = userDataApi.declineInvitation(username, targetUsername).execute();
    } catch (IOException e) {
      throw new AssertionError(e);
    }
    assertEquals(200, response.code());
    return response.body();
  }

  public List<UserJson> getFriends(String username, String searchQuery) {
    final Response<List<UserJson>> response;
    try {
      response = userDataApi.friends(username, searchQuery).execute();
    } catch (IOException e) {
      throw new AssertionError(e);
    }
    assertEquals(200, response.code());
    return response.body();
  }

  public void removeFriend(String username, String targetUsername) {
    final Response<Void> response;
    try {
      response = userDataApi.removeFriend(username, targetUsername).execute();
    } catch (IOException e) {
      throw new AssertionError(e);
    }
    assertEquals(200, response.code());
  }
}
