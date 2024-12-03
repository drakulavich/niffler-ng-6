package guru.qa.niffler.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import guru.qa.niffler.api.AuthApi;
import guru.qa.niffler.api.core.CodeInterceptor;
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

  public static final String RESPONSE_TYPE = "code";
  public static final String CLIENT_ID = "client";
  public static final String SCOPE = "openid";
  public static final String REDIRECT_URI = CFG.frontUrl() + "authorized";
  public static final String CODE_CHALLENGE_METHOD = "S256";
  public static final String GRANT_TYPE = "authorization_code";
  private final AuthApi authApi;

  public AuthApiClient() {
    super(CFG.authUrl(), true, new CodeInterceptor());
    this.authApi = create(AuthApi.class);
  }

  public String preRequest() {
    String codeVerifier = generateCodeVerifier();
    String codeChallenge = generateCodeChallenge(codeVerifier);
    final Response<Void> response;
    try {
      response = authApi.authorize(
        RESPONSE_TYPE,
        CLIENT_ID,
        SCOPE,
        REDIRECT_URI,
        codeChallenge,
        CODE_CHALLENGE_METHOD
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
        CLIENT_ID,
        REDIRECT_URI,
        GRANT_TYPE,
        code,
        codeVerifier
      ).execute();
    } catch (IOException e) {
      throw new AssertionError(e);
    }
    return Objects.requireNonNull(response.body()).get("id_token").asText();
  }

  public String getToken(@Nonnull String username, @Nonnull String password) {
    String codeVerifier = preRequest();
    String code = login(username, password);
    return token(code, codeVerifier);
  }
}
