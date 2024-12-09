package guru.qa.niffler.test.rest;

import guru.qa.niffler.jupiter.annotation.ApiLogin;
import guru.qa.niffler.jupiter.annotation.Token;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.annotation.meta.RestTest;
import guru.qa.niffler.jupiter.extension.ApiLoginExtension;
import guru.qa.niffler.model.rest.FriendJson;
import guru.qa.niffler.model.rest.FriendState;
import guru.qa.niffler.model.rest.UserJson;
import guru.qa.niffler.service.impl.GatewayApiClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RestTest
public class FriendsTest {

  @RegisterExtension
  private static final ApiLoginExtension apiLoginExtension = ApiLoginExtension.rest();

  private final GatewayApiClient gatewayApiClient = new GatewayApiClient();

  @User(friends = 2, income = 1)
  @ApiLogin
  @Test
  void allFriendsAndIncomeInvitationsShouldBeReturnedFroUser(UserJson user, @Token String token) {
    final List<String> expectedFriends = user.testData().friends();
    final List<String> expectedInvitations = user.testData().income();
    final List<UserJson> result = gatewayApiClient.allFriends(
      token,
      null
    );

    assertThat(result.size()).isEqualTo(3);

    final List<UserJson> friendsFromResponse = result.stream().filter(
      u -> u.friendState() == FriendState.FRIEND
    ).toList();
    final List<UserJson> invitationsFromResponse = result.stream().filter(
      u -> u.friendState() == FriendState.INVITE_RECEIVED
    ).toList();

    assertThat(friendsFromResponse.size()).isEqualTo(2);
    assertThat(invitationsFromResponse.size()).isEqualTo(1);
    assertThat(invitationsFromResponse.getFirst().username()).isEqualTo(expectedInvitations.getFirst());

    final UserJson firstUserFromRequest = friendsFromResponse.getFirst();
    final UserJson secondUserFromRequest = friendsFromResponse.getLast();

    assertThat(firstUserFromRequest.username()).isEqualTo(expectedFriends.getFirst());
    assertThat(secondUserFromRequest.username()).isEqualTo(expectedFriends.getLast());
  }

  @User(friends = 1, income = 2)
  @ApiLogin
  @Test
  void userCanSeeFriendsAndIncomeInvitations(@Token String token) {
    final List<UserJson> result = gatewayApiClient.allFriends(
      token,
      null
    );

    assertThat(result.size()).isEqualTo(3);

    final List<UserJson> friendsFromResponse = result.stream().filter(
      u -> u.friendState() == FriendState.FRIEND
    ).toList();
    final List<UserJson> invitationsFromResponse = result.stream().filter(
      u -> u.friendState() == FriendState.INVITE_RECEIVED
    ).toList();

    assertThat(friendsFromResponse.size()).isEqualTo(1);
    assertThat(invitationsFromResponse.size()).isEqualTo(2);
  }

  @User(friends = 1)
  @ApiLogin
  @Test
  void userCanRemoveFriend(UserJson user, @Token String token) {
    final String friendId = user.testData().friends().getFirst();
    gatewayApiClient.removeFriend(token, friendId);

    final List<UserJson> result = gatewayApiClient.allFriends(
      token,
      null
    );

    assertThat(result).isEmpty();
  }

  @User(friends = 1, income = 1)
  @ApiLogin
  @Test
  void userCanAcceptInvitation(UserJson user, @Token String token) {
    final String friendName = user.testData().income().getFirst();
    gatewayApiClient.acceptInvitation(token, new FriendJson(friendName));

    final List<UserJson> result = gatewayApiClient.allFriends(
      token,
      null
    );

    assertThat(result.size()).isEqualTo(2);
    assertThat(result.stream().anyMatch(u -> u.username().equals(friendName))).isTrue();
  }

  @User(friends = 1, income = 1)
  @ApiLogin
  @Test
  void userCanDeclineInvitation(UserJson user, @Token String token) {
    final String friendName = user.testData().income().getFirst();
    gatewayApiClient.declineInvitation(token, new FriendJson(friendName));

    final List<UserJson> result = gatewayApiClient.allFriends(
      token,
      null
    );

    assertThat(result.size()).isEqualTo(1);
    assertThat(result.stream().anyMatch(u -> u.username().equals(friendName))).isFalse();
  }

  @User(outcome = 1)
  @ApiLogin
  @Test
  void userCanSendOutcomeInvitation(UserJson user, @Token String token) {
    final String friendName = user.testData().outcome().getFirst();
    gatewayApiClient.sendInvitation(token, new FriendJson(friendName));

    final List<UserJson> outgoingInvitations = gatewayApiClient.allUsers(token, null).stream()
      .filter(u -> u.friendState() == FriendState.INVITE_SENT)
      .toList();

    assertThat(outgoingInvitations.size()).isEqualTo(1);
    assertThat(outgoingInvitations.getFirst().username()).isEqualTo(friendName);
  }
}
