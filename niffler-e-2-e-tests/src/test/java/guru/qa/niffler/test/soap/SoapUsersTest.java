package guru.qa.niffler.test.soap;

import guru.qa.jaxb.userdata.AcceptInvitationRequest;
import guru.qa.jaxb.userdata.CurrentUserRequest;
import guru.qa.jaxb.userdata.DeclineInvitationRequest;
import guru.qa.jaxb.userdata.FriendsPageRequest;
import guru.qa.jaxb.userdata.PageInfo;
import guru.qa.jaxb.userdata.RemoveFriendRequest;
import guru.qa.jaxb.userdata.SendInvitationRequest;
import guru.qa.jaxb.userdata.UserResponse;
import guru.qa.jaxb.userdata.UsersResponse;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.annotation.meta.SoapTest;
import guru.qa.niffler.model.rest.UserJson;
import guru.qa.niffler.service.impl.UserdataSoapClient;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@SoapTest
public class SoapUsersTest {

  private final UserdataSoapClient userdataSoapClient = new UserdataSoapClient();

  @Test
  @User
  void currentUserTest(UserJson user) throws IOException {
    CurrentUserRequest currentUserRequest = new CurrentUserRequest();
    currentUserRequest.setUsername(user.username());

    UserResponse response = userdataSoapClient.getCurrent(currentUserRequest);
    assertThat(response.getUser().getUsername()).isEqualTo(user.username());
  }

  @User(friends = 2)
  @Test
  void getFriendsWithPagination(UserJson user) throws IOException {
    FriendsPageRequest request = new FriendsPageRequest();
    request.setUsername(user.username());

    setPageSize(10, request);

    UsersResponse response = userdataSoapClient.getFriends(request);
    assertThat(response.getTotalElements()).isEqualTo(2);
    assertThat(response.getTotalPages()).isEqualTo(1);
  }

  @User(friends = 3)
  @Test
  void getFriendsWithPaginationAndSearchQuery(UserJson user) throws IOException {
    final String friendName = user.testData().friends().get(1);

    FriendsPageRequest request = new FriendsPageRequest();
    request.setUsername(user.username());
    request.setSearchQuery(friendName);

    setPageSize(5, request);

    UsersResponse response = userdataSoapClient.getFriends(request);
    assertThat(response.getTotalElements()).isEqualTo(1);
    assertThat(response.getTotalPages()).isEqualTo(1);
  }

  @User(friends = 2)
  @Test
  void userCanRemoveFriend(UserJson user) throws IOException {
    final String friendToRemove = user.testData().friends().getFirst();

    RemoveFriendRequest removeRequest = new RemoveFriendRequest();
    removeRequest.setUsername(user.username());
    removeRequest.setFriendToBeRemoved(friendToRemove);

    userdataSoapClient.removeFriend(removeRequest);

    FriendsPageRequest getFriendsRequest = new FriendsPageRequest();
    getFriendsRequest.setUsername(user.username());

    setPageSize(5, getFriendsRequest);

    UsersResponse response = userdataSoapClient.getFriends(getFriendsRequest);
    assertThat(response.getTotalElements()).isEqualTo(1);
    assertThat(response.getUser().getFirst().getUsername()).isNotEqualTo(friendToRemove);
  }

  @User(friends = 1, income = 1)
  @Test
  void userCanAcceptFriendshipRequest(UserJson user) throws IOException {
    final String friendToAccept = user.testData().income().getFirst();

    AcceptInvitationRequest acceptRequest = new AcceptInvitationRequest();
    acceptRequest.setUsername(user.username());
    acceptRequest.setFriendToBeAdded(friendToAccept);

    UserResponse response = userdataSoapClient.acceptInvitation(acceptRequest);
    assertThat(response.getUser().getUsername()).isEqualTo(friendToAccept);

    FriendsPageRequest getFriendsRequest = new FriendsPageRequest();
    getFriendsRequest.setUsername(user.username());

    setPageSize(2, getFriendsRequest);

    UsersResponse friendsResponse = userdataSoapClient.getFriends(getFriendsRequest);
    assertThat(friendsResponse.getTotalElements()).isEqualTo(2);
  }

  @User(friends = 1, income = 1)
  @Test
  void userCanDeclineFriendshipRequest(UserJson user) throws IOException {
    final String friendToDecline = user.testData().income().getFirst();

    DeclineInvitationRequest declineRequest = new DeclineInvitationRequest();
    declineRequest.setUsername(user.username());
    declineRequest.setInvitationToBeDeclined(friendToDecline);

    UserResponse response = userdataSoapClient.declineInvitation(declineRequest);
    assertThat(response.getUser().getUsername()).isEqualTo(friendToDecline);

    FriendsPageRequest getFriendsRequest = new FriendsPageRequest();
    getFriendsRequest.setUsername(user.username());

    setPageSize(2, getFriendsRequest);

    UsersResponse friendsResponse = userdataSoapClient.getFriends(getFriendsRequest);
    assertThat(friendsResponse.getTotalElements()).isEqualTo(1);
  }

  @User(income = 3)
  @Test
  void userCanSendFriendshipRequest(UserJson user) throws IOException {
    final String friendToRequest = user.testData().income().getFirst();

    SendInvitationRequest requestFriendship = new SendInvitationRequest();
    requestFriendship.setUsername(user.username());
    requestFriendship.setFriendToBeRequested(friendToRequest);

    UserResponse responseFriendship = userdataSoapClient.sendInvitation(requestFriendship);
    assertThat(responseFriendship.getUser().getUsername()).isEqualTo(friendToRequest);

    FriendsPageRequest getFriendsRequest = new FriendsPageRequest();
    getFriendsRequest.setUsername(friendToRequest);

    setPageSize(5, getFriendsRequest);

    UsersResponse friendsResponse = userdataSoapClient.getFriends(getFriendsRequest);
    assertThat(friendsResponse.getTotalElements()).isEqualTo(1);
  }

  private static void setPageSize(int value, FriendsPageRequest request) {
    PageInfo pageInfo = new PageInfo();
    pageInfo.setSize(value);
    request.setPageInfo(pageInfo);
  }
}
