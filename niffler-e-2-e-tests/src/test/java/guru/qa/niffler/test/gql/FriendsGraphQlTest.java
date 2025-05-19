package guru.qa.niffler.test.gql;

import com.apollographql.apollo.api.ApolloResponse;
import com.apollographql.apollo.api.Error;
import com.apollographql.java.client.ApolloCall;
import com.apollographql.java.rx2.Rx2Apollo;
import guru.qa.FriendsOfFriendsQuery;
import guru.qa.ValidFriendsOfFriendsQuery;
import guru.qa.niffler.jupiter.annotation.ApiLogin;
import guru.qa.niffler.jupiter.annotation.Token;
import guru.qa.niffler.jupiter.annotation.User;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class FriendsGraphQlTest extends BaseGraphQlTest {

  @User(friends = 2)
  @Test
  @ApiLogin
  void friendsOfFriendsDeeperTwoLevelsThrowsError(@Token String bearerToken) {
    final ApolloCall<FriendsOfFriendsQuery.Data> nestedFriends = apolloClient.query(
        new FriendsOfFriendsQuery(0, 10))
      .addHttpHeader("authorization", bearerToken);

    final ApolloResponse<FriendsOfFriendsQuery.Data> response = Rx2Apollo.single(nestedFriends).blockingGet();
    List<Error> errors = response.errors;

    assertThat(errors).hasSize(1);
    assertThat(errors.getFirst().getMessage()).isEqualTo("Can`t fetch over 2 friends sub-queries");
  }

  @User(friends = 2)
  @Test
  @ApiLogin
  void friendsOfFriendsCanBeRequested(@Token String bearerToken) {
    final ApolloCall<ValidFriendsOfFriendsQuery.Data> nestedFriends = apolloClient.query(
        new ValidFriendsOfFriendsQuery(0, 10))
      .addHttpHeader("authorization", bearerToken);

    final ApolloResponse<ValidFriendsOfFriendsQuery.Data> response = Rx2Apollo.single(nestedFriends).blockingGet();
    ValidFriendsOfFriendsQuery.Data data = response.dataOrThrow();

    String friendName = data.user.friends.edges.getFirst().node.friends.edges.getFirst().node.username;
    assertThat(friendName).isNotEmpty();
  }
}
