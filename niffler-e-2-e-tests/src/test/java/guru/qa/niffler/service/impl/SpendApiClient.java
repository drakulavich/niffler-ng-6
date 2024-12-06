package guru.qa.niffler.service.impl;

import guru.qa.niffler.api.SpendApi;
import guru.qa.niffler.api.core.RestClient.EmptyClient;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.model.rest.CategoryJson;
import guru.qa.niffler.model.rest.CurrencyValues;
import guru.qa.niffler.model.rest.SpendJson;
import guru.qa.niffler.service.SpendClient;
import retrofit2.Response;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.io.IOException;
import java.util.List;

import static java.util.Objects.requireNonNull;
import static org.assertj.core.api.Assertions.assertThat;

@ParametersAreNonnullByDefault
public class SpendApiClient implements SpendClient {

  private static final Config CFG = Config.getInstance();
  private final SpendApi spendApi = new EmptyClient(CFG.spendUrl()).create(SpendApi.class);

  @Override
  @Nonnull
  public SpendJson createSpend(SpendJson spend) {
    final Response<SpendJson> response;
    try {
      response = spendApi.addSpend(spend)
        .execute();
    } catch (IOException e) {
      throw new AssertionError(e);
    }
    assertThat(response.code()).isEqualTo(201);
    return requireNonNull(response.body());
  }

  @Override
  @Nonnull
  public CategoryJson createCategory(CategoryJson category) {
    final Response<CategoryJson> response;
    try {
      response = spendApi.addCategory(category)
        .execute();
    } catch (IOException e) {
      throw new AssertionError(e);
    }
    assertThat(response.code()).isEqualTo(200);
    CategoryJson result = requireNonNull(response.body());
    return category.archived()
      ? updateCategory(
      new CategoryJson(
        result.id(),
        result.name(),
        result.username(),
        true
      )
    ) : result;
  }

  @Override
  @Nonnull
  public CategoryJson updateCategory(CategoryJson category) {
    final Response<CategoryJson> response;
    try {
      response = spendApi.updateCategory(category)
        .execute();
    } catch (IOException e) {
      throw new AssertionError(e);
    }
    assertThat(response.code()).isEqualTo(200);
    return response.body();
  }

  @Override
  public void removeCategory(CategoryJson category) {
    throw new UnsupportedOperationException("Can`t remove category using API");
  }

  @Nonnull
  public List<CategoryJson> getCategories(String username, boolean excludeArchived) {
    final Response<List<CategoryJson>> response;
    try {
      response = spendApi.getAllCategories(username, excludeArchived).execute();
    } catch (IOException e) {
      throw new AssertionError(e);
    }
    assertThat(response.code()).isEqualTo(200);
    return requireNonNull(response.body());
  }

  @Nonnull
  public List<SpendJson> getSpends(String username, CurrencyValues currency, String from, String to) {
    final Response<List<SpendJson>> response;
    try {
      response = spendApi.getAllSpends(username, currency, from, to).execute();
    } catch (IOException e) {
      throw new AssertionError(e);
    }
    assertThat(response.code()).isEqualTo(200);
    return requireNonNull(response.body());
  }
}
