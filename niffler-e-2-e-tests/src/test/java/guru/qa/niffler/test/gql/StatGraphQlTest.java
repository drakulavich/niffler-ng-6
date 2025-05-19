package guru.qa.niffler.test.gql;

import com.apollographql.apollo.api.ApolloResponse;
import com.apollographql.java.client.ApolloCall;
import com.apollographql.java.rx2.Rx2Apollo;
import guru.qa.StatQuery;
import guru.qa.niffler.jupiter.annotation.ApiLogin;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.annotation.Spending;
import guru.qa.niffler.jupiter.annotation.Token;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.rest.CurrencyValues;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class StatGraphQlTest extends BaseGraphQlTest {

  @User
  @Test
  @ApiLogin
  void statTest(@Token String bearerToken) {
    final ApolloCall<StatQuery.Data> currenciesCall = apolloClient.query(StatQuery.builder()
            .filterCurrency(null)
            .statCurrency(null)
            .filterPeriod(null)
            .build())
        .addHttpHeader("authorization", bearerToken);

    final ApolloResponse<StatQuery.Data> response = Rx2Apollo.single(currenciesCall).blockingGet();
    final StatQuery.Data data = response.dataOrThrow();
    StatQuery.Stat result = data.stat;
    Assertions.assertEquals(
        0.0,
        result.total
    );
  }

  @User(
    categories = {
      @Category(
        name = "Leisure",
        archived = true
      ),
      @Category(
        name = "Dine Out"
      )
    },
    spendings = {
      @Spending(
        category = "Leisure",
        description = "Travel",
        currency = CurrencyValues.EUR,
        amount = 3000),
      @Spending(
        category = "Dine Out",
        description = "Cafe",
        currency = CurrencyValues.USD,
        amount = 45),
      @Spending(
        category = "Dine Out",
        description = "Restaurant",
        currency = CurrencyValues.KZT,
        amount = 50_000)
    }
  )
  @Test
  @ApiLogin
  void statWithFilterAndCurrencyConversion(@Token String bearerToken) {
    final ApolloCall<StatQuery.Data> statCall = apolloClient.query(StatQuery.builder()
            .filterCurrency(guru.qa.type.CurrencyValues.USD)
            .statCurrency(guru.qa.type.CurrencyValues.RUB)
            .filterPeriod(null)
            .build())
        .addHttpHeader("authorization", bearerToken);

    final ApolloResponse<StatQuery.Data> response = Rx2Apollo.single(statCall).blockingGet();
    final StatQuery.Data data = response.dataOrThrow();
    StatQuery.Stat result = data.stat;

    assertThat(result.total).isEqualTo(3000.0);
    assertThat(result.currency.rawValue).isEqualTo(CurrencyValues.RUB.name());
  }

  @User(
    categories = {
      @Category(
        name = "Leisure",
        archived = true
      ),
      @Category(
        name = "Dine Out"
      )
    },
    spendings = {
      @Spending(
        category = "Leisure",
        description = "Travel",
        currency = CurrencyValues.EUR,
        amount = 3000),
      @Spending(
        category = "Leisure",
        description = "Cafe",
        currency = CurrencyValues.USD,
        amount = 45)
    }
  )
  @Test
  @ApiLogin
  void statWithArchivedCategoryShouldBeShown(@Token String bearerToken) {
    final ApolloCall<StatQuery.Data> statCall = apolloClient.query(StatQuery.builder()
        .filterCurrency(guru.qa.type.CurrencyValues.EUR)
        .statCurrency(guru.qa.type.CurrencyValues.EUR)
        .filterPeriod(null)
        .build())
      .addHttpHeader("authorization", bearerToken);

    final ApolloResponse<StatQuery.Data> response = Rx2Apollo.single(statCall).blockingGet();
    final StatQuery.Data data = response.dataOrThrow();
    StatQuery.Stat result = data.stat;

    assertThat(result.total).isEqualTo(3000.0);
    assertThat(result.currency.rawValue).isEqualTo(CurrencyValues.EUR.name());
  }
}
