package guru.qa.niffler.data.dao.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.AuthUserDao;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.mapper.AuthUserEntityRowMapper;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static guru.qa.niffler.data.jdbc.Connections.holder;

@ParametersAreNonnullByDefault
public class AuthUserDaoJdbc implements AuthUserDao {

  private static final Config CFG = Config.getInstance();

  @SuppressWarnings("resource")
  @Nonnull
  @Override
  public AuthUserEntity create(AuthUserEntity user) {
    try (PreparedStatement ps = holder(CFG.authJdbcUrl()).connection().prepareStatement(
        "INSERT INTO \"user\" (username, password, enabled, account_non_expired, account_non_locked, credentials_non_expired) " +
            "VALUES (?, ?, ?, ?, ?, ?)", PreparedStatement.RETURN_GENERATED_KEYS)) {
      ps.setString(1, user.getUsername());
      ps.setString(2, user.getPassword());
      ps.setBoolean(3, user.getEnabled());
      ps.setBoolean(4, user.getAccountNonExpired());
      ps.setBoolean(5, user.getAccountNonLocked());
      ps.setBoolean(6, user.getCredentialsNonExpired());

      ps.executeUpdate();

      final UUID generatedKey;
      try (ResultSet rs = ps.getGeneratedKeys()) {
        if (rs.next()) {
          generatedKey = rs.getObject("id", UUID.class);
        } else {
          throw new SQLException("Can`t find id in ResultSet");
        }
      }
      user.setId(generatedKey);
      return user;
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @SuppressWarnings("resource")
  @Nonnull
  @Override
  public Optional<AuthUserEntity> findById(UUID id) {
    try (PreparedStatement ps = holder(CFG.authJdbcUrl()).connection().prepareStatement("SELECT * FROM \"user\" WHERE id = ?")) {
      ps.setObject(1, id);

      ps.execute();

      try (ResultSet rs = ps.getResultSet()) {
        if (rs.next()) {
          return Optional.ofNullable(AuthUserEntityRowMapper.instance.mapRow(rs, rs.getRow()));
        } else {
          return Optional.empty();
        }
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @SuppressWarnings("resource")
  @Nonnull
  @Override
  public Optional<AuthUserEntity> findByUsername(String username) {
    try (PreparedStatement ps = holder(CFG.authJdbcUrl()).connection().prepareStatement(
      "SELECT * FROM \"user\" WHERE username = ?")) {
      ps.setString(1, username);
      ps.execute();

      try (ResultSet rs = ps.getResultSet()) {
        if (rs.next()) {
          return Optional.ofNullable(AuthUserEntityRowMapper.instance.mapRow(rs, rs.getRow()));
        } else {
          return Optional.empty();
        }
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @SuppressWarnings("resource")
  @Override
  public void delete(AuthUserEntity user) {
    try (PreparedStatement ps = holder(CFG.authJdbcUrl()).connection().prepareStatement(
      "DELETE FROM \"user\" WHERE id = ?"
    )) {
      ps.setObject(1, user.getId());
      ps.execute();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @SuppressWarnings("resource")
  @Nonnull
  @Override
  public List<AuthUserEntity> findAll() {
    try (PreparedStatement ps = holder(CFG.authJdbcUrl()).connection().prepareStatement(
      "SELECT * FROM \"user\"")) {
      ps.execute();

      try (ResultSet rs = ps.getResultSet()) {
        List<AuthUserEntity> users = new ArrayList<>();
        while (rs.next()) {
          AuthUserEntity ue = AuthUserEntityRowMapper.instance.mapRow(rs, rs.getRow());
          users.add(ue);
        }
        return users;
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @SuppressWarnings("resource")
  @Nonnull
  @Override
  public AuthUserEntity update(AuthUserEntity user) {
    try (PreparedStatement userPs = holder(CFG.authJdbcUrl()).connection().prepareStatement(
      "UPDATE \"user\" SET username = ?, password = ?, enabled = ?, account_non_expired = ?, account_non_locked = ?, credentials_non_expired = ? WHERE id = ?")) {
      userPs.setString(1, user.getUsername());
      userPs.setString(2, user.getPassword());
      userPs.setBoolean(3, user.getEnabled());
      userPs.setBoolean(4, user.getAccountNonExpired());
      userPs.setBoolean(5, user.getAccountNonLocked());
      userPs.setBoolean(6, user.getCredentialsNonExpired());
      userPs.setObject(7, user.getId());
      userPs.executeUpdate();
      return user;
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void deleteAll() {
    try (PreparedStatement ps = holder(CFG.authJdbcUrl()).connection().prepareStatement(
      "DELETE FROM \"user\""
    )) {
      ps.execute();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }
}
