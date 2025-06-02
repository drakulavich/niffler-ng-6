package guru.qa.niffler.test.grpc;

import guru.qa.niffler.grpc.FriendshipRequest;
import guru.qa.niffler.grpc.UserPageRequest;
import guru.qa.niffler.grpc.UserPageResponse;
import guru.qa.niffler.grpc.UserResponse;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.rest.UserJson;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class UserdataGrpcTest extends BaseGrpclTest {

  @User(friends = 2)
  @Test
  void getFriendsWithPagination(UserJson user) {
    final UserPageRequest request = UserPageRequest.newBuilder()
      .setUsername(user.username())
      .setPage(0)
      .setSize(10)
      .build();

    final UserPageResponse response = userdataBlockingStub.getFriends(request);
    assertThat(response.getTotalElements()).isEqualTo(2);
    assertThat(response.getTotalPages()).isEqualTo(1);
    assertThat(response.getFirst()).isTrue();
    assertThat(response.getLast()).isTrue();
  }

  @User(friends = 3)
  @Test
  void getFriendsWithPaginationAndSearchQuery(UserJson user) {
    final String friendName = user.testData().friends().get(1);
    final UserPageRequest request = UserPageRequest.newBuilder()
      .setUsername(user.username())
      .setSize(5)
      .setSearchQuery(friendName)
      .build();

    final UserPageResponse response = userdataBlockingStub.getFriends(request);
    assertThat(response.getTotalElements()).isEqualTo(1);
    assertThat(response.getTotalPages()).isEqualTo(1);
  }

  @User(friends = 2)
  @Test
  void userCanRemoveFriend(UserJson user) {
    final String friendToRemove = user.testData().friends().getFirst();

    final FriendshipRequest removeRequest = FriendshipRequest.newBuilder()
      .setUsername(user.username())
      .setTargetUsername(friendToRemove)
      .build();

    userdataBlockingStub.removeFriend(removeRequest);

    final UserPageRequest getFriendsRequest = UserPageRequest.newBuilder()
      .setUsername(user.username())
      .setSize(5)
      .build();

    final UserPageResponse response = userdataBlockingStub.getFriends(getFriendsRequest);
    assertThat(response.getTotalElements()).isEqualTo(1);
    assertThat(response.getEdges(0).getUsername()).isNotEqualTo(friendToRemove);
  }

  @User(friends = 1, income = 1)
  @Test
  void userCanAcceptFriendshipRequest(UserJson user) {
    final String friendToAccept = user.testData().income().getFirst();

    final FriendshipRequest acceptRequest = FriendshipRequest.newBuilder()
      .setUsername(user.username())
      .setTargetUsername(friendToAccept)
      .build();
    final UserResponse response = userdataBlockingStub.acceptFriendshipRequest(acceptRequest);

    assertThat(response.getUsername()).isEqualTo(friendToAccept);

    final UserPageRequest getFriendsRequest = UserPageRequest.newBuilder()
      .setUsername(user.username())
      .setSize(2)
      .build();

    final UserPageResponse friendsResponse = userdataBlockingStub.getFriends(getFriendsRequest);
    assertThat(friendsResponse.getTotalElements()).isEqualTo(2);
  }

  @User(friends = 1, income = 1)
  @Test
  void userCanDeclineFriendshipRequest(UserJson user) {
    final String friendToDecline = user.testData().income().getFirst();

    final FriendshipRequest declineRequest = FriendshipRequest.newBuilder()
      .setUsername(user.username())
      .setTargetUsername(friendToDecline)
      .build();
    final UserResponse response = userdataBlockingStub.declineFriendshipRequest(declineRequest);
    assertThat(response.getUsername()).isEqualTo(friendToDecline);

    final UserPageRequest getFriendsRequest = UserPageRequest.newBuilder()
      .setUsername(user.username())
      .setSize(2)
      .build();
    final UserPageResponse friendsResponse = userdataBlockingStub.getFriends(getFriendsRequest);
    assertThat(friendsResponse.getTotalElements()).isEqualTo(1);
  }

  @User(income = 3)
  @Test
  void userCanSendFriendshipRequest(UserJson user) {
    final UserPageRequest request = UserPageRequest.newBuilder()
      .setUsername(user.username())
      .setSize(5)
      .build();

    final UserPageResponse response = userdataBlockingStub.getFriends(request);
    final String friendToRequest = response.getEdges(0).getUsername();

    final FriendshipRequest requestFriendship = FriendshipRequest.newBuilder()
      .setUsername(user.username())
      .setTargetUsername(friendToRequest)
      .build();
    final UserResponse responseFriendship = userdataBlockingStub.createFriendshipRequest(requestFriendship);
    assertThat(responseFriendship.getUsername()).isEqualTo(friendToRequest);

    final UserPageRequest getFriendsRequest = UserPageRequest.newBuilder()
      .setUsername(friendToRequest)
      .setSize(5)
      .build();
    final UserPageResponse friendsResponse = userdataBlockingStub.getFriends(getFriendsRequest);
    assertThat(friendsResponse.getTotalElements()).isEqualTo(1);
  }
}
