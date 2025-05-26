package guru.qa.niffler.test.grpc;

import com.google.protobuf.Empty;
import guru.qa.niffler.grpc.CalculateRequest;
import guru.qa.niffler.grpc.CalculateResponse;
import guru.qa.niffler.grpc.Currency;
import guru.qa.niffler.grpc.CurrencyResponse;
import guru.qa.niffler.grpc.CurrencyValues;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class CurrencyGrpcTest extends BaseGrpclTest {

  @Test
  void allCurrenciesShouldBeReturned() {
    final CurrencyResponse response = blockingStub.getAllCurrencies(Empty.getDefaultInstance());
    List<Currency> allCurrenciesList = response.getAllCurrenciesList();

    assertThat(allCurrenciesList.size()).isEqualTo(4);
  }

  @Test
  void calculateRateShouldReturnSameAmountForSameCurrency() {
    final CalculateRequest request = CalculateRequest.newBuilder()
      .setSpendCurrency(CurrencyValues.USD)
      .setDesiredCurrency(CurrencyValues.USD)
      .setAmount(100.0)
      .build();

    final CalculateResponse response = blockingStub.calculateRate(request);

    assertThat(response.getCalculatedAmount()).isEqualTo(100.0);
  }

  @Test
  void calculateRateShouldReturnCalculatedAmountForDifferentCurrencies() {
    final CalculateRequest request = CalculateRequest.newBuilder()
      .setSpendCurrency(CurrencyValues.USD)
      .setDesiredCurrency(CurrencyValues.EUR)
      .setAmount(100.0)
      .build();

    final CalculateResponse response = blockingStub.calculateRate(request);

    assertThat(response.getCalculatedAmount()).isEqualTo(92.59);
  }

  @Test
  void calculateRateShouldHandleZeroAmount() {
    final CalculateRequest request = CalculateRequest.newBuilder()
      .setSpendCurrency(CurrencyValues.USD)
      .setDesiredCurrency(CurrencyValues.EUR)
      .setAmount(0.0)
      .build();

    final CalculateResponse response = blockingStub.calculateRate(request);

    assertThat(response.getCalculatedAmount()).isEqualTo(0.0);
  }

  @Test
  void calculateRateShouldWorkForAllCurrencyPairs() {
    final CalculateRequest request = CalculateRequest.newBuilder()
      .setSpendCurrency(CurrencyValues.RUB)
      .setDesiredCurrency(CurrencyValues.KZT)
      .setAmount(50.0)
      .build();

    final CalculateResponse response = blockingStub.calculateRate(request);

    assertThat(response.getCalculatedAmount()).isEqualTo(357.14);
  }
}
