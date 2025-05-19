package guru.qa.niffler.test.gql;

import com.apollographql.apollo.api.ApolloResponse;
import com.apollographql.apollo.api.Error;
import com.apollographql.java.client.ApolloCall;
import com.apollographql.java.rx2.Rx2Apollo;
import guru.qa.FriendsCategoriesQuery;
import guru.qa.niffler.jupiter.annotation.ApiLogin;
import guru.qa.niffler.jupiter.annotation.Token;
import guru.qa.niffler.jupiter.annotation.User;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class CategoriesGraphQlTest extends BaseGraphQlTest {

  @User(friends = 2)
  @Test
  @ApiLogin
  void categoriesFromAnotherUserThrowsError(@Token String bearerToken) {
    final ApolloCall<FriendsCategoriesQuery.Data> friendsCategoriesCall = apolloClient.query(
        new FriendsCategoriesQuery(0, 10))
      .addHttpHeader("authorization", bearerToken);

    final ApolloResponse<FriendsCategoriesQuery.Data> response = Rx2Apollo.single(friendsCategoriesCall).blockingGet();
    List<Error> errors = response.errors;

    assertThat(errors).hasSize(2);
    errors.forEach(
      e -> assertThat(e.getMessage()).isEqualTo("Can`t query categories for another user")
    );
  }
}
