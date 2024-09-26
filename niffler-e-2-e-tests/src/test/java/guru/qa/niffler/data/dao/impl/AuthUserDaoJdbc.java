package guru.qa.niffler.data.dao.impl;

import guru.qa.niffler.data.dao.AuthUserDao;
import guru.qa.niffler.data.entity.auth.UserEntity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;
import java.util.UUID;

public class AuthUserDaoJdbc implements AuthUserDao {
  private final Connection connection;
  private static final PasswordEncoder pe = PasswordEncoderFactories.createDelegatingPasswordEncoder();

  public AuthUserDaoJdbc(Connection connection) {
    this.connection = connection;
  }

  @Override
  public UserEntity createUser(UserEntity user) {
    try (PreparedStatement ps = connection.prepareStatement(
      "INSERT INTO \"user\" (id, username, password, enabled, account_non_expired, account_non_locked, credentials_non_expired) " +
        "VALUES (?, ?, ?, ?, ?, ?, ?)",
      Statement.RETURN_GENERATED_KEYS
    )) {
      ps.setObject(1, user.getId());
      ps.setString(2, user.getUsername());
      String encodedPassword = pe.encode(user.getPassword());
      ps.setString(3, encodedPassword);
      ps.setBoolean(4, user.getEnabled());
      ps.setBoolean(5, user.getAccountNonExpired());
      ps.setBoolean(6, user.getAccountNonLocked());
      ps.setBoolean(7, user.getCredentialsNonExpired());
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

  @Override
  public Optional<UserEntity> findById(UUID id) {
    try (PreparedStatement ps = connection.prepareStatement(
      "SELECT * FROM \"user\" WHERE id = ?")) {
      ps.setObject(1, id);
      ps.execute();

      try (ResultSet rs = ps.getResultSet()) {
        if (rs.next()) {
          UserEntity ue = new UserEntity();
          ue.setId(rs.getObject("id", UUID.class));
          ue.setUsername(rs.getString("username"));
          ue.setPassword(rs.getString("password"));
          ue.setEnabled(rs.getBoolean("enabled"));
          ue.setAccountNonExpired(rs.getBoolean("account_non_expired"));
          ue.setAccountNonLocked(rs.getBoolean("account_non_locked"));
          ue.setCredentialsNonExpired(rs.getBoolean("credentials_non_expired"));
          return Optional.of(ue);
        } else {
          return Optional.empty();
        }
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public Optional<UserEntity> findByUsername(String username) {
    try (PreparedStatement ps = connection.prepareStatement(
      "SELECT * FROM \"user\" WHERE username = ?")) {
      ps.setString(1, username);
      ps.execute();

      try (ResultSet rs = ps.getResultSet()) {
        if (rs.next()) {
          UserEntity ue = new UserEntity();
          ue.setId(rs.getObject("id", UUID.class));
          ue.setUsername(rs.getString("username"));
          ue.setPassword(rs.getString("password"));
          ue.setEnabled(rs.getBoolean("enabled"));
          ue.setAccountNonExpired(rs.getBoolean("account_non_expired"));
          ue.setAccountNonLocked(rs.getBoolean("account_non_locked"));
          ue.setCredentialsNonExpired(rs.getBoolean("credentials_non_expired"));
          return Optional.of(ue);
        } else {
          return Optional.empty();
        }
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void delete(UserEntity user) {
    try (PreparedStatement ps = connection.prepareStatement(
      "DELETE FROM \"user\" WHERE id = ?"
    )) {
      ps.setObject(1, user.getId());
      ps.execute();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }
}
