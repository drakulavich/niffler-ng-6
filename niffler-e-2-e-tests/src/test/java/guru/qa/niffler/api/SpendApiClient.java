package guru.qa.niffler.api;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SpendApiClient {

  private final Retrofit retrofit = new Retrofit.Builder()
      .baseUrl(Config.getInstance().spendUrl())
      .addConverterFactory(JacksonConverterFactory.create())
      .build();

  private final SpendApi spendApi = retrofit.create(SpendApi.class);

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

  public List<SpendJson> getAllSpends(String username, CurrencyValues cur, String from, String to) {
    final Response<SpendJson> response;
    try {
        response = spendApi.getAllSpends(username, cur, from, to).execute();
    } catch (IOException e) {
        throw new AssertionError(e);
    }
    assertEquals(200, response.code());
    return Collections.singletonList(response.body());
  }

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

  public List<CategoryJson> getAllCategories(String username, boolean excludeArchived) {
    final Response<CategoryJson> response;
    try {
    response = spendApi.getAllCategories(username, excludeArchived).execute();
    } catch (IOException e) {
    throw new AssertionError(e);
    }
    assertEquals(200, response.code());
    return Collections.singletonList(response.body());
  }
}