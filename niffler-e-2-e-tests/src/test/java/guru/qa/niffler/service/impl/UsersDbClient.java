package guru.qa.niffler.service.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.auth.Authority;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import guru.qa.niffler.data.entity.userdata.UdUserEntity;
import guru.qa.niffler.data.repository.AuthUserRepository;
import guru.qa.niffler.data.repository.UserdataUserRepository;
import guru.qa.niffler.data.repository.impl.AuthUserRepositorySpringJdbc;
import guru.qa.niffler.data.repository.impl.UserdataUserRepositorySpringJdbc;
import guru.qa.niffler.data.tpl.XaTransactionTemplate;
import guru.qa.niffler.model.rest.CurrencyValues;
import guru.qa.niffler.model.rest.UserJson;
import guru.qa.niffler.service.UsersClient;
import io.qameta.allure.Step;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static guru.qa.niffler.utils.RandomDataUtils.randomUsername;

@ParametersAreNonnullByDefault
public class UsersDbClient implements UsersClient {

  private static final Config CFG = Config.getInstance();
  private static final PasswordEncoder pe = PasswordEncoderFactories.createDelegatingPasswordEncoder();

  private final AuthUserRepository authUserRepository = new AuthUserRepositorySpringJdbc();
  private final UserdataUserRepository userdataUserRepository = new UserdataUserRepositorySpringJdbc();

  private final XaTransactionTemplate xaTransactionTemplate = new XaTransactionTemplate(
      CFG.authJdbcUrl(),
      CFG.userdataJdbcUrl()
  );

  @Nonnull
  @Step("Crete user using SQL")
  public UserJson createUser(String username, String password) {
    return Objects.requireNonNull(xaTransactionTemplate.execute(() -> UserJson.fromEntity(
      createNewUser(username, password),
      null
    )));
  }

  public void addInvitation(UserJson requester, UserJson addressee) {
    xaTransactionTemplate.execute(() -> {
        UdUserEntity requesterEntity = userdataUserRepository.findById(requester.id()).orElseThrow();
        UdUserEntity addresseeEntity = userdataUserRepository.findById(addressee.id()).orElseThrow();

        userdataUserRepository.sendInvitation(requesterEntity, addresseeEntity);
        return null;
      }
    );
  }

  public void addFriends(UserJson requester, UserJson addressee) {
    xaTransactionTemplate.execute(() -> {
        UdUserEntity requesterEntity = userdataUserRepository.findById(requester.id()).orElseThrow();
        UdUserEntity addresseeEntity = userdataUserRepository.findById(addressee.id()).orElseThrow();

        userdataUserRepository.addFriend(requesterEntity, addresseeEntity);
        return null;
      }
    );
  }

  @Nonnull
  @Override
  public List<UserJson> addIncomeInvitation(UserJson targetUser, int count) {
    List<UserJson> result = new ArrayList<>();
    if (count > 0) {
      UdUserEntity targetEntity = userdataUserRepository.findById(
        targetUser.id()
      ).orElseThrow();
      for (int i = 0; i < count; i++) {
        xaTransactionTemplate.execute(() -> {
            String username = randomUsername();
            UdUserEntity adressee = createNewUser(username, "12345");
            result.add(UserJson.fromEntity(adressee, null));
            userdataUserRepository.sendInvitation(adressee, targetEntity);
            return null;
          }
        );
      }
    }
    return result;
  }

  @Nonnull
  @Override
  public List<UserJson> addOutcomeInvitation(UserJson targetUser, int count) {
    List<UserJson> result = new ArrayList<>();
    if (count > 0) {
      UdUserEntity targetEntity = userdataUserRepository.findById(
        targetUser.id()
      ).orElseThrow();
      for (int i = 0; i < count; i++) {
        xaTransactionTemplate.execute(() -> {
            String username = randomUsername();
            UdUserEntity adressee = createNewUser(username, "12345");
            result.add(UserJson.fromEntity(adressee, null));
            userdataUserRepository.sendInvitation(targetEntity, adressee);
            return null;
          }
        );
      }
    }
    return result;
  }

  @Nonnull
  @Override
  public List<UserJson> addFriend(UserJson targetUser, int count) {
    List<UserJson> result = new ArrayList<>();
    if (count > 0) {
      UdUserEntity targetEntity = userdataUserRepository.findById(
        targetUser.id()
      ).orElseThrow();

      for (int i = 0; i < count; i++) {
        xaTransactionTemplate.execute(() -> {
            String username = randomUsername();
            UdUserEntity friend = createNewUser(username, "12345");
            result.add(UserJson.fromEntity(friend, null));
            userdataUserRepository.addFriend(targetEntity, friend);
            return null;
          }
        );
      }
    }
    return result;
  }

  @Nonnull
  private UdUserEntity userEntity(String username) {
    UdUserEntity ue = new UdUserEntity();
    ue.setUsername(username);
    ue.setCurrency(CurrencyValues.RUB);
    return ue;
  }

  @Nonnull
  private AuthUserEntity authUserEntity(String username, String password) {
    AuthUserEntity authUser = new AuthUserEntity();
    authUser.setUsername(username);
    authUser.setPassword(pe.encode(password));
    authUser.setEnabled(true);
    authUser.setAccountNonExpired(true);
    authUser.setAccountNonLocked(true);
    authUser.setCredentialsNonExpired(true);
    authUser.setAuthorities(
      Arrays.stream(Authority.values()).map(
        e -> {
          AuthorityEntity ae = new AuthorityEntity();
          ae.setUser(authUser);
          ae.setAuthority(e);
          return ae;
        }
      ).toList()
    );
    return authUser;
  }

  @Nonnull
  private UdUserEntity createNewUser(String username, String password) {
    AuthUserEntity authUser = authUserEntity(username, password);
    authUserRepository.create(authUser);
    return userdataUserRepository.create(userEntity(username));
  }

  public void removeAllUsers() {
    xaTransactionTemplate.execute(() -> {
      userdataUserRepository.removeAll();
      authUserRepository.removeAll();
      return null;
    });
  }
}
