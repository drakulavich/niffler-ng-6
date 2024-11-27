package guru.qa.niffler.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import guru.qa.niffler.api.AuthApi;
import guru.qa.niffler.api.core.RestClient;
import guru.qa.niffler.api.core.ThreadSafeCookieStore;
import retrofit2.Response;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.io.IOException;
import java.util.Objects;

import static guru.qa.niffler.utils.OauthUtils.generateCodeChallenge;
import static guru.qa.niffler.utils.OauthUtils.generateCodeVerifier;

@ParametersAreNonnullByDefault
public class AuthApiClient extends RestClient {

  private final AuthApi authApi;

  public AuthApiClient() {
    super(CFG.authUrl(), true);
    this.authApi = create(AuthApi.class);
  }

  public String preRequest() {
    String codeVerifier = generateCodeVerifier();
    String codeChallenge = generateCodeChallenge(codeVerifier);
    final Response<Void> response;
    try {
      response = authApi.authorize(
        "code",
        "client",
        "openid",
        CFG.frontUrl() + "authorized",
        codeChallenge,
        "S256"
      ).execute();
    } catch (IOException e) {
      throw new AssertionError(e);
    }
    return codeVerifier;
  }

  public String login(@Nonnull String username, @Nonnull String password) {
    final Response<Void> response;
    try {
      response = authApi.login(
        username,
        password,
        ThreadSafeCookieStore.INSTANCE.cookieValue("XSRF-TOKEN")
      ).execute();
    } catch (IOException e) {
      throw new AssertionError(e);
    }
    return Objects.requireNonNull(response.raw().request().url().queryParameter("code"));
  }

  public String token(String code, String codeVerifier) {
    Response<JsonNode> response;
    try {
      response = authApi.token(
        "client",
        CFG.frontUrl() + "authorized",
        "authorization_code",
        code,
        codeVerifier
      ).execute();
    } catch (IOException e) {
      throw new AssertionError(e);
    }
    return Objects.requireNonNull(response.body()).get("id_token").asText();
  }
}
