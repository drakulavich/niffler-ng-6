package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.UdUserDao;
import guru.qa.niffler.data.dao.impl.UdUserDaoSpringJdbc;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.auth.Authority;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import guru.qa.niffler.data.entity.userdata.UdUserEntity;
import guru.qa.niffler.data.repository.AuthUserRepository;
import guru.qa.niffler.data.repository.UserdataUserRepository;
import guru.qa.niffler.data.repository.impl.AuthUserRepositoryJdbc;
import guru.qa.niffler.data.repository.impl.UserdataUserRepositoryJdbc;
import guru.qa.niffler.data.tpl.DataSources;
import guru.qa.niffler.data.tpl.XaTransactionTemplate;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.UserJson;
import org.springframework.data.transaction.ChainedTransactionManager;
import org.springframework.jdbc.support.JdbcTransactionManager;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Arrays;

public class UsersDbClient {

  private static final Config CFG = Config.getInstance();
  private static final PasswordEncoder pe = PasswordEncoderFactories.createDelegatingPasswordEncoder();

  private final AuthUserRepository authUserRepository = new AuthUserRepositoryJdbc();
  private final UdUserDao udUserDao = new UdUserDaoSpringJdbc();
  private final UserdataUserRepository userdataUserRepository = new UserdataUserRepositoryJdbc();

  private final UdUserDao udUserDaoJdbc = new UdUserDaoSpringJdbc();

  private final XaTransactionTemplate xaTransactionTemplate = new XaTransactionTemplate(
      CFG.authJdbcUrl(),
      CFG.userdataJdbcUrl()
  );

  private final TransactionTemplate txTemplate = new TransactionTemplate(
    new ChainedTransactionManager(
      new JdbcTransactionManager(DataSources.dataSource(CFG.authJdbcUrl())),
      new JdbcTransactionManager(DataSources.dataSource(CFG.userdataJdbcUrl()))
    )
  );

  public UserJson createUser(UserJson user) {
    return createUserTemplate(user, authUserRepository, udUserDao);
  }

  public UserJson createUser(String username, String password) {
    UserJson user = new UserJson(
        null,
        username,
        null,
        null,
        null,
        CurrencyValues.RUB,
        null,
        null,
        null
    );
    return txTemplate.execute(status -> {
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
          ).toList());
        authUserRepository.create(authUser);

        return UserJson.fromEntity(
            userdataUserRepository.create(UdUserEntity.fromJson(user)),
            null
        );
      }
    );
  }

  public void addInvitation(UserJson requester, UserJson addressee) {
    txTemplate.execute(status -> {
        UdUserEntity requesterEntity = userdataUserRepository.findById(requester.id()).orElseThrow();
        UdUserEntity addresseeEntity = userdataUserRepository.findById(addressee.id()).orElseThrow();

        userdataUserRepository.addInvitation(requesterEntity, addresseeEntity);
        return null;
      }
    );
  }

  public void addFriends(UserJson requester, UserJson addressee) {
    txTemplate.execute(status -> {
        UdUserEntity requesterEntity = userdataUserRepository.findById(requester.id()).orElseThrow();
        UdUserEntity addresseeEntity = userdataUserRepository.findById(addressee.id()).orElseThrow();

        userdataUserRepository.addFriend(requesterEntity, addresseeEntity);
        return null;
      }
    );
  }

  public UserJson createUserJdbc(UserJson user) {
    return createUserTemplate(user, authUserRepository, udUserDaoJdbc);
  }

  public UserJson createUserWithoutTx(UserJson user) {
    return createUserWithoutTxTemplate(user, authUserRepository, udUserDao);
  }

  public UserJson createUserWithoutTxJdbc(UserJson user) {
    return createUserWithoutTxTemplate(user, authUserRepository, udUserDaoJdbc);
  }

  private UserJson createUserTemplate(UserJson user, AuthUserRepository authUserRepository, UdUserDao udUserDao) {
    return txTemplate.execute(status -> {
        AuthUserEntity authUser = new AuthUserEntity();
        authUser.setUsername(user.username());
        authUser.setPassword(pe.encode("12345"));
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
        ).toList());
        authUserRepository.create(authUser);

        return UserJson.fromEntity(
          udUserDao.create(UdUserEntity.fromJson(user)),
          null
        );
      }
    );
  }

  private UserJson createUserWithoutTxTemplate(UserJson user, AuthUserRepository authUserRepository, UdUserDao udUserDao) {
    AuthUserEntity authUser = new AuthUserEntity();
    authUser.setUsername(user.username());
    authUser.setPassword(pe.encode("12345"));
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
      ).toList());
    authUserRepository.create(authUser);

    return UserJson.fromEntity(
      udUserDao.create(UdUserEntity.fromJson(user)),
      null
    );
  }
}
