package guru.qa.niffler.api;

import guru.qa.niffler.api.core.RestClient;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.service.SpendClient;
import retrofit2.Response;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ParametersAreNonnullByDefault
public class SpendApiClient extends RestClient implements SpendClient {

  private final SpendApi spendApi;

  public SpendApiClient() {
    super(CFG.spendUrl());
    this.spendApi = retrofit.create(SpendApi.class);
  }

  @Nonnull
  @Override
  public SpendJson createSpend(SpendJson spend) {
    final Response<SpendJson> response;
    try {
      response = spendApi.addSpend(spend).execute();
    } catch (IOException e) {
      throw new AssertionError(e);
    }
    assertEquals(201, response.code());
    return response.body();
  }

  @Nonnull
  public SpendJson updateSpend(SpendJson spend) {
    final Response<SpendJson> response;
    try {
      response = spendApi.editSpend(spend).execute();
    } catch (IOException e) {
      throw new AssertionError(e);
    }
    assertEquals(200, response.code());
    return response.body();
  }

  @Nonnull
  public SpendJson getSpend(int id, String username) {
    final Response<SpendJson> response;
    try {
      response = spendApi.getSpend(id, username).execute();
    } catch (IOException e) {
      throw new AssertionError(e);
    }
    assertEquals(200, response.code());
    return response.body();
  }

  public void deleteSpend(String username, List<String> ids) {
    final Response<SpendJson> response;
    try {
    response = spendApi.removeSpend(username, ids).execute();
    } catch (IOException e) {
    throw new AssertionError(e);
    }
    assertEquals(202, response.code());
  }

  @Nonnull
  public List<SpendJson> getAllSpends(String username,
                                      @Nullable CurrencyValues cur,
                                      @Nullable String from,
                                      @Nullable String to) {
    final Response<List<SpendJson>> response;
    try {
        response = spendApi.getAllSpends(username, cur, from, to).execute();
    } catch (IOException e) {
        throw new AssertionError(e);
    }
    assertEquals(200, response.code());
    return response.body() != null
      ? response.body()
      : Collections.emptyList();
  }

  @Nonnull
  @Override
  public CategoryJson createCategory(CategoryJson category) {
    final Response<CategoryJson> response;
    try {
      response = spendApi.addCategory(category).execute();
    } catch (IOException e) {
      throw new AssertionError(e);
    }
    assertEquals(200, response.code());
    return response.body();
  }

  @Nonnull
  public CategoryJson updateCategory(CategoryJson category) {
    final Response<CategoryJson> response;
    try {
    response = spendApi.updateCategory(category).execute();
    } catch (IOException e) {
    throw new AssertionError(e);
    }
    assertEquals(200, response.code());
    return response.body();
  }

  @Nonnull
  public List<CategoryJson> getAllCategories(String username, boolean excludeArchived) {
    final Response<List<CategoryJson>> response;
    try {
    response = spendApi.getAllCategories(username, excludeArchived).execute();
    } catch (IOException e) {
    throw new AssertionError(e);
    }
    assertEquals(200, response.code());
    return response.body() != null
      ? response.body()
      : Collections.emptyList();
  }

  @Override
  public void removeCategory(CategoryJson category) {
    throw new UnsupportedOperationException("This method is not implemented yet");
  }
}
