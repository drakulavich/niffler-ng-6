package guru.qa.niffler.api;

import guru.qa.niffler.model.SpendJson;
import retrofit2.Call;
import retrofit2.http.*;

public interface SpendApi {

  @POST("internal/spends/add")
  Call<SpendJson> addSpend(@Body SpendJson spend);

  @PATCH("internal/spends/edit")
  @GET("internal/spends/{id}")
  @GET("internal/spends/all")
  @DELETE("internal/spends/remove")
  @POST("internal/categories/add")
  @PATCH("internal/categories/update")
  @GET("internal/categories/all")
}
