package guru.qa.niffler.test.grpc;

import com.google.protobuf.Empty;
import guru.qa.niffler.grpc.CalculateRequest;
import guru.qa.niffler.grpc.CalculateResponse;
import guru.qa.niffler.grpc.Currency;
import guru.qa.niffler.grpc.CurrencyResponse;
import guru.qa.niffler.grpc.CurrencyValues;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class CurrencyGrpcTest extends BaseGrpclTest {

  @Test
  void allCurrenciesShouldBeReturned() {
    final CurrencyResponse response = blockingStub.getAllCurrencies(Empty.getDefaultInstance());
    List<Currency> allCurrenciesList = response.getAllCurrenciesList();

    assertThat(allCurrenciesList.size()).isEqualTo(4);
  }

  @ParameterizedTest
  @MethodSource("calculateRateTestData")
  void calculateRateShouldReturnExpectedAmount(
    CurrencyValues fromCurrency,
    CurrencyValues toCurrency,
    double amount,
    double expectedAmount
  ) {
    final CalculateRequest request = CalculateRequest.newBuilder()
      .setSpendCurrency(fromCurrency)
      .setDesiredCurrency(toCurrency)
      .setAmount(amount)
      .build();

    final CalculateResponse response = blockingStub.calculateRate(request);

    assertThat(response.getCalculatedAmount()).isEqualTo(expectedAmount);
  }

  static Stream<Arguments> calculateRateTestData() {
    return Stream.of(
      Arguments.of(CurrencyValues.USD, CurrencyValues.USD, 100.0, 100.0),
      Arguments.of(CurrencyValues.USD, CurrencyValues.EUR, 100.0, 92.59),
      Arguments.of(CurrencyValues.USD, CurrencyValues.EUR, 0.0, 0.0),
      Arguments.of(CurrencyValues.RUB, CurrencyValues.KZT, 50.0, 357.14)
    );
  }
}
