package guru.qa.niffler.test.grpc;

import com.google.protobuf.Empty;
import guru.qa.niffler.grpc.Currency;
import guru.qa.niffler.grpc.CurrencyResponse;
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
}
