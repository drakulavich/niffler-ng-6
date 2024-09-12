package guru.qa.niffler.api;

import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

public interface SpendApi {

  // Spends
  @POST("internal/spends/add")
  Call<SpendJson> addSpend(@Body SpendJson spend);

  @PATCH("internal/spends/edit")
  Call<SpendJson> editSpend(@Body SpendJson spend);

  @GET("internal/spends/{id}")
  Call<SpendJson> getSpend(@Path("id") int id,
                           @Query("username") String username);

  @GET("internal/spends/all")
  Call<SpendJson> getAllSpends(@Query("username") String username,
                               @Query("filterCurrency") CurrencyValues cur,
                               @Query("from") String from,
                               @Query("to") String to);

  @DELETE("internal/spends/remove")
  Call<SpendJson> removeSpend(@Query("username") String username, @Query("ids") List<String> ids);

  // Categories
  @POST("internal/categories/add")
  Call<CategoryJson> addCategory(@Body CategoryJson category);

  @PATCH("internal/categories/update")
  Call<CategoryJson> updateCategory(@Body CategoryJson category);

  @GET("internal/categories/all")
  Call<CategoryJson> getAllCategories(@Query("username") String username,
                                      @Query("excludeArchived") boolean excludeArchived);
}
