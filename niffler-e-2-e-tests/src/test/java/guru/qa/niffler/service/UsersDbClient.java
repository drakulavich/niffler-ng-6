package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.auth.Authority;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import guru.qa.niffler.data.entity.userdata.UdUserEntity;
import guru.qa.niffler.data.repository.AuthUserRepository;
import guru.qa.niffler.data.repository.UserdataUserRepository;
import guru.qa.niffler.data.repository.impl.AuthUserRepositoryHibernate;
import guru.qa.niffler.data.repository.impl.UserdataUserRepositoryHibernate;
import guru.qa.niffler.data.tpl.XaTransactionTemplate;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.UserJson;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static guru.qa.niffler.utils.RandomDataUtils.randomUsername;

public class UsersDbClient implements UsersClient {

  private static final Config CFG = Config.getInstance();
  private static final PasswordEncoder pe = PasswordEncoderFactories.createDelegatingPasswordEncoder();

  private final AuthUserRepository authUserRepository = new AuthUserRepositoryHibernate();
  private final UserdataUserRepository userdataUserRepository = new UserdataUserRepositoryHibernate();

  private final XaTransactionTemplate xaTransactionTemplate = new XaTransactionTemplate(
      CFG.authJdbcUrl(),
      CFG.userdataJdbcUrl()
  );

  public UserJson createUser(String username, String password) {
    return xaTransactionTemplate.execute(() -> UserJson.fromEntity(
      createNewUser(username, password),
      null
    ));
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

  private UdUserEntity userEntity(String username) {
    UdUserEntity ue = new UdUserEntity();
    ue.setUsername(username);
    ue.setCurrency(CurrencyValues.USD);
    return ue;
  }

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

  private UdUserEntity createNewUser(String username, String password) {
    AuthUserEntity authUser = authUserEntity(username, password);
    authUserRepository.create(authUser);
    return userdataUserRepository.create(userEntity(username));
  }
}
