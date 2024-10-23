package guru.qa.niffler.data.repository.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.AuthAuthorityDao;
import guru.qa.niffler.data.dao.AuthUserDao;
import guru.qa.niffler.data.dao.impl.AuthAuthorityDaoSpringJdbc;
import guru.qa.niffler.data.dao.impl.AuthUserDaoSpringJdbc;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import guru.qa.niffler.data.extractor.AuthUserEntityRowExtractor;
import guru.qa.niffler.data.repository.AuthUserRepository;
import guru.qa.niffler.data.tpl.DataSources;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Optional;
import java.util.UUID;

public class AuthUserRepositorySpringJdbc implements AuthUserRepository {

  private static final Config CFG = Config.getInstance();
  private final AuthUserDao authUserDao = new AuthUserDaoSpringJdbc();
  private final AuthAuthorityDao authAuthorityDao = new AuthAuthorityDaoSpringJdbc();

  @Override
  public AuthUserEntity update(AuthUserEntity user) {
    user.getAuthorities().forEach(authAuthorityDao::update);
    return authUserDao.update(user);
  }

  @Override
  public void remove(AuthUserEntity user) {
    user.getAuthorities().forEach(authAuthorityDao::delete);
    authUserDao.delete(user);
  }

  @Override
  public AuthUserEntity create(AuthUserEntity user) {
    authUserDao.create(user);
    authAuthorityDao.create(user.getAuthorities().toArray(new AuthorityEntity[0]));
    return user;
  }

  @Override
  public Optional<AuthUserEntity> findById(UUID id) {
    JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.authJdbcUrl()));
    return Optional.ofNullable(
      jdbcTemplate.query(
        "SELECT a.id as authority_id, " +
          "authority, " +
          "user_id as id, " +
          "u.username, " +
          "u.password, " +
          "u.enabled, " +
          "u.account_non_expired, " +
          "u.account_non_locked, " +
          "u.credentials_non_expired " +
          "FROM \"user\" u join authority a on u.id = a.user_id WHERE u.id = ?",
        AuthUserEntityRowExtractor.instance,
        id
    ));
  }

  @Override
  public Optional<AuthUserEntity> findByUsername(String username) {
    JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.authJdbcUrl()));
    return Optional.ofNullable(
      jdbcTemplate.query(
        "SELECT a.id as authority_id, " +
          "authority, " +
          "user_id as id, " +
          "u.username, " +
          "u.password, " +
          "u.enabled, " +
          "u.account_non_expired, " +
          "u.account_non_locked, " +
          "u.credentials_non_expired " +
          "FROM \"user\" u join authority a on u.id = a.user_id WHERE u.username = ?",
        AuthUserEntityRowExtractor.instance,
        username
      ));
  }
}
