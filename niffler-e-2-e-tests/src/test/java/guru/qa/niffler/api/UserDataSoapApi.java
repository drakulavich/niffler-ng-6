package guru.qa.niffler.api;

import guru.qa.jaxb.userdata.AcceptInvitationRequest;
import guru.qa.jaxb.userdata.AllUsersRequest;
import guru.qa.jaxb.userdata.CurrentUserRequest;
import guru.qa.jaxb.userdata.DeclineInvitationRequest;
import guru.qa.jaxb.userdata.FriendsPageRequest;
import guru.qa.jaxb.userdata.RemoveFriendRequest;
import guru.qa.jaxb.userdata.SendInvitationRequest;
import guru.qa.jaxb.userdata.UserResponse;
import guru.qa.jaxb.userdata.UsersResponse;
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
