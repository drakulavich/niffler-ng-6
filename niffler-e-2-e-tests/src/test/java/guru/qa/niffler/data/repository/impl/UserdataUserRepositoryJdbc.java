package guru.qa.niffler.data.repository.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.entity.userdata.UdUserEntity;
import guru.qa.niffler.data.mapper.UdUserEntityRowMapper;
import guru.qa.niffler.data.repository.UserdataUserRepository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

import static guru.qa.niffler.data.tpl.Connections.holder;

public class UserdataUserRepositoryJdbc implements UserdataUserRepository {

  private static final Config CFG = Config.getInstance();

  @Override
  public UdUserEntity create(UdUserEntity user) {
    try (PreparedStatement userPs = holder(CFG.userdataJdbcUrl()).connection().prepareStatement(
      "INSERT INTO \"user\" (username, currency) VALUES (?, ?)",
      PreparedStatement.RETURN_GENERATED_KEYS
    )) {
      userPs.setString(1, user.getUsername());
      userPs.setString(2, user.getCurrency().name());
      userPs.executeUpdate();

      final UUID generatedKey;
      try (ResultSet rs = userPs.getGeneratedKeys()) {
        if (rs.next()) {
          generatedKey = rs.getObject("id", UUID.class);
        } else {
          throw new SQLException("Can`t find id in ResultSet");
        }
      }
      user.setId(generatedKey);
      return user;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public Optional<UdUserEntity> findById(UUID id) {
    try (PreparedStatement ps = holder(CFG.userdataJdbcUrl()).connection().prepareStatement("SELECT * FROM \"user\" WHERE id = ? ")) {
      ps.setObject(1, id);
      ps.execute();

      try (ResultSet rs = ps.getResultSet()) {
        if (rs.next()) {
          UdUserEntity ue = UdUserEntityRowMapper.instance.mapRow(rs, 1);
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
  public void addInvitation(UdUserEntity requester, UdUserEntity addressee) {
    try (PreparedStatement ps = holder(CFG.userdataJdbcUrl()).connection().prepareStatement(
      "INSERT INTO friendship (requester_id, addressee_id, status, created_date) VALUES (?, ?, 'PENDING', now())"
    )) {
      ps.setObject(1, requester.getId());
      ps.setObject(2, addressee.getId());
      ps.executeUpdate();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }

  }

  @Override
  public void addFriend(UdUserEntity requester, UdUserEntity addressee) {
    try (PreparedStatement ps = holder(CFG.userdataJdbcUrl()).connection().prepareStatement(
      "INSERT INTO friendship (requester_id, addressee_id, status, created_date) VALUES (?, ?, 'ACCEPTED', now())"
    )) {
      ps.setObject(1, requester.getId());
      ps.setObject(2, addressee.getId());
      ps.addBatch();
      ps.clearParameters();

      ps.setObject(1, addressee.getId());
      ps.setObject(2, requester.getId());
      ps.addBatch();
      ps.clearParameters();

      ps.executeBatch();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }
}
