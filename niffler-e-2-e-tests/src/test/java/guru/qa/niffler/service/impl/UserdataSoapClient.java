package guru.qa.niffler.service.impl;

import guru.qa.jaxb.userdata.CurrentUserRequest;
import guru.qa.jaxb.userdata.UserResponse;
import guru.qa.niffler.api.UserDataSoapApi;
import guru.qa.niffler.api.core.RestClient;
import guru.qa.niffler.api.core.converter.SoapConverterFactory;
import guru.qa.niffler.config.Config;
import io.qameta.allure.Step;
import okhttp3.logging.HttpLoggingInterceptor;

import javax.annotation.Nonnull;
import java.io.IOException;

public class UserdataSoapClient extends RestClient {

  private static final Config CFG = Config.getInstance();
  private final UserDataSoapApi userDataSoapApi;

  public UserdataSoapClient() {
    super(CFG.userdataUrl(), false, SoapConverterFactory.create("niffler-userdata"), HttpLoggingInterceptor.Level.BODY);
    this.userDataSoapApi = create(UserDataSoapApi.class);
  }

  @Step("[SOAP] Get current user info")
  public @Nonnull UserResponse getCurrent(CurrentUserRequest currentUserRequest) throws IOException {
    return userDataSoapApi.currentUser(currentUserRequest).execute().body();
  }
}
