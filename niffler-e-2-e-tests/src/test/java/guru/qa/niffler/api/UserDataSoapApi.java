package guru.qa.niffler.api;

import guru.qa.niffler.userdata.wsdl.AcceptInvitationRequest;
import guru.qa.niffler.userdata.wsdl.AllUsersRequest;
import guru.qa.niffler.userdata.wsdl.CurrentUserRequest;
import guru.qa.niffler.userdata.wsdl.DeclineInvitationRequest;
import guru.qa.niffler.userdata.wsdl.FriendsPageRequest;
import guru.qa.niffler.userdata.wsdl.RemoveFriendRequest;
import guru.qa.niffler.userdata.wsdl.SendInvitationRequest;
import guru.qa.niffler.userdata.wsdl.UserResponse;
import guru.qa.niffler.userdata.wsdl.UsersResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface UserDataSoapApi {

  @Headers({
    "Content-type: text/xml",
    "Accept-Charset: utf-8"
  })
  @POST("ws")
  Call<UserResponse> currentUser(@Body CurrentUserRequest currentUserRequest);

  @Headers({
    "Content-type: text/xml",
    "Accept-Charset: utf-8"
  })
  @POST("ws")
  Call<UsersResponse> allUsers(@Body AllUsersRequest allUsersRequest);

  @Headers(value = {
    "Content-type: text/xml",
    "Accept-Charset: utf-8"
  })
  @POST("ws")
  Call<UsersResponse> getFriends(@Body FriendsPageRequest request);

  @Headers(value = {
    "Content-type: text/xml",
    "Accept-Charset: utf-8"
  })
  @POST("ws")
  Call<Void> removeFriend(@Body RemoveFriendRequest request);

  @Headers(value = {
    "Content-type: text/xml",
    "Accept-Charset: utf-8"
  })
  @POST("ws")
  Call<UserResponse> sendInvitation(@Body SendInvitationRequest request);

  @Headers(value = {
    "Content-type: text/xml",
    "Accept-Charset: utf-8"
  })
  @POST("ws")
  Call<UserResponse> acceptInvitation(@Body AcceptInvitationRequest request);

  @Headers(value = {
    "Content-type: text/xml",
    "Accept-Charset: utf-8"
  })
  @POST("ws")
  Call<UserResponse> declineInvitation(@Body DeclineInvitationRequest request);
}
