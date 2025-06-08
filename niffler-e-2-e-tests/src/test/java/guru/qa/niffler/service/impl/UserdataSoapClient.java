package guru.qa.niffler.service.impl;

import guru.qa.niffler.userdata.wsdl.AcceptInvitationRequest;
import guru.qa.niffler.userdata.wsdl.AllUsersRequest;
import guru.qa.niffler.userdata.wsdl.CurrentUserRequest;
import guru.qa.niffler.userdata.wsdl.DeclineInvitationRequest;
import guru.qa.niffler.userdata.wsdl.FriendsPageRequest;
import guru.qa.niffler.userdata.wsdl.RemoveFriendRequest;
import guru.qa.niffler.userdata.wsdl.SendInvitationRequest;
import guru.qa.niffler.userdata.wsdl.UserResponse;
import guru.qa.niffler.userdata.wsdl.UsersResponse;
import guru.qa.niffler.api.UserDataSoapApi;
import guru.qa.niffler.api.core.RestClient;
import guru.qa.niffler.api.core.converter.SoapConverterFactory;
import guru.qa.niffler.config.Config;
import io.qameta.allure.Step;
import okhttp3.logging.HttpLoggingInterceptor;

import javax.annotation.Nonnull;
import java.io.IOException;

public class UserdataSoapClient extends RestClient {

  private static final Config CFG = Config.getInstance();
  private final UserDataSoapApi userDataSoapApi;

  public UserdataSoapClient() {
    super(CFG.userdataUrl(), false, SoapConverterFactory.create("niffler-userdata"), HttpLoggingInterceptor.Level.BODY);
    this.userDataSoapApi = create(UserDataSoapApi.class);
  }

  @Step("[SOAP] Get current user info")
  public @Nonnull UserResponse getCurrent(CurrentUserRequest currentUserRequest) throws IOException {
    return userDataSoapApi.currentUser(currentUserRequest).execute().body();
  }

  @Step("[SOAP] Get all users")
  public @Nonnull UsersResponse getAllUsers(AllUsersRequest allUsersRequest) throws IOException {
    return userDataSoapApi.allUsers(allUsersRequest).execute().body();
  }

  @Step("[SOAP] Get friends")
  public @Nonnull UsersResponse getFriends(FriendsPageRequest request) throws IOException {
    return userDataSoapApi.getFriends(request).execute().body();
  }

  @Step("[SOAP] Remove friend")
  public void removeFriend(RemoveFriendRequest request) throws IOException {
    userDataSoapApi.removeFriend(request).execute();
  }

  @Step("[SOAP] Send invitation")
  public @Nonnull UserResponse sendInvitation(SendInvitationRequest request) throws IOException {
    return userDataSoapApi.sendInvitation(request).execute().body();
  }

  @Step("[SOAP] Accept invitation")
  public @Nonnull UserResponse acceptInvitation(AcceptInvitationRequest request) throws IOException {
    return userDataSoapApi.acceptInvitation(request).execute().body();
  }

  @Step("[SOAP] Decline invitation")
  public @Nonnull UserResponse declineInvitation(DeclineInvitationRequest request) throws IOException {
    return userDataSoapApi.declineInvitation(request).execute().body();
  }
}
