package guru.qa.niffler.data.dao.impl;

import guru.qa.niffler.data.dao.UserdataUserDao;
import guru.qa.niffler.data.entity.userdata.UdUserEntity;
import guru.qa.niffler.model.CurrencyValues;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;
import java.util.UUID;

public class UdUserDaoJdbc implements UserdataUserDao {
  private final Connection connection;

  public UdUserDaoJdbc(Connection connection) {
      this.connection = connection;
  }

  @Override
  public UdUserEntity createUser(UdUserEntity user) {
    try (PreparedStatement ps = connection.prepareStatement(
            "INSERT INTO \"user\" (username, currency, firstname, surname, full_name, photo, photo_small) " +
                 "VALUES (?, ?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS)) {
      ps.setString(1, user.getUsername());
      ps.setString(2, user.getCurrency().name());
      ps.setString(3, user.getFirstname());
      ps.setString(4, user.getSurname());
      ps.setString(5, user.getFullname());
      ps.setBytes(6, user.getPhoto());
      ps.setBytes(7, user.getPhotoSmall());

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
  public Optional<UdUserEntity> findById(UUID id) {
    try (PreparedStatement ps = connection.prepareStatement(
            "SELECT * FROM users WHERE id = ?")) {
      ps.setObject(1, id);
      ps.execute();

      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          UdUserEntity ue = new UdUserEntity();
          ue.setId(rs.getObject("id", UUID.class));
          ue.setUsername(rs.getString("username"));
          ue.setCurrency(CurrencyValues.valueOf(rs.getString("currency")));
          ue.setFirstname(rs.getString("firstname"));
          ue.setSurname(rs.getString("surname"));
          ue.setFullname(rs.getString("fullname"));
          ue.setPhoto(rs.getBytes("photo"));
          ue.setPhotoSmall(rs.getBytes("photo_small"));

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
  public Optional<UdUserEntity> findByUsername(String username) {
    try (PreparedStatement ps = connection.prepareStatement(
            "SELECT * FROM users WHERE username = ?")) {
      ps.setString(1, username);
      ps.execute();

      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          UdUserEntity ue = new UdUserEntity();
          ue.setId(rs.getObject("id", UUID.class));
          ue.setUsername(rs.getString("username"));
          ue.setCurrency(CurrencyValues.valueOf(rs.getString("currency")));
          ue.setFirstname(rs.getString("firstname"));
          ue.setSurname(rs.getString("surname"));
          ue.setFullname(rs.getString("fullname"));
          ue.setPhoto(rs.getBytes("photo"));
          ue.setPhotoSmall(rs.getBytes("photo_small"));

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
  public void delete(UdUserEntity user) {
    try (PreparedStatement ps = connection.prepareStatement(
            "DELETE FROM users WHERE id = ?")) {
      ps.setObject(1, user.getId());
      ps.execute();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }
}
