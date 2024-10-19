package guru.qa.niffler.data.dao.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.UdUserDao;
import guru.qa.niffler.data.entity.userdata.FriendshipEntity;
import guru.qa.niffler.data.entity.userdata.FriendshipStatus;
import guru.qa.niffler.data.entity.userdata.UdUserEntity;
import guru.qa.niffler.data.mapper.UdUserEntityRowMapper;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static guru.qa.niffler.data.tpl.Connections.holder;

public class UdUserDaoJdbc implements UdUserDao {

  private static final Config CFG = Config.getInstance();

  @Override
  public UdUserEntity create(UdUserEntity user) {
    try (PreparedStatement userPs = holder(CFG.userdataJdbcUrl()).connection().prepareStatement(
      "INSERT INTO \"user\" (username, currency, firstname, surname, photo, photo_small, full_name) VALUES (?, ?, ?, ?, ?, ?, ?)",
      PreparedStatement.RETURN_GENERATED_KEYS);
         PreparedStatement friendshipPs = holder(CFG.userdataJdbcUrl()).connection().prepareStatement(
           "INSERT INTO friendship (requester_id, addressee_id, status, created_date) VALUES (?, ?, ?, ?)")
    ) {
      userPs.setString(1, user.getUsername());
      userPs.setString(2, user.getCurrency().name());
      userPs.setString(3, user.getFirstname());
      userPs.setString(4, user.getSurname());
      userPs.setBytes(5, user.getPhoto());
      userPs.setBytes(6, user.getPhotoSmall());
      userPs.setString(7, user.getFullname());
      userPs.executeUpdate();

      final UUID generatedKey;
      try (ResultSet rs = userPs.getGeneratedKeys()) {
        if (rs.next()) {
          generatedKey = rs.getObject("id", UUID.class);
        } else {
          throw new SQLException("Can`t find id in ResultSet");
        }
      }

      List<FriendshipEntity> allFriendships = new ArrayList<>();
      allFriendships.addAll(user.getFriendshipRequests());
      allFriendships.addAll(user.getFriendshipAddressees());
      for (FriendshipEntity f : allFriendships) {
        friendshipPs.setObject(1, f.getRequester().getId());
        friendshipPs.setObject(2, f.getAddressee().getId());
        friendshipPs.setString(3, f.getStatus().name());
        friendshipPs.setObject(4, f.getCreatedDate());
        friendshipPs.addBatch();
        friendshipPs.clearParameters();
      }
      friendshipPs.executeBatch();

      user.setId(generatedKey);
      return user;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public Optional<UdUserEntity> findById(UUID id) {
    return findUser("SELECT * FROM \"user\" WHERE id = ?", id);
  }

  @Override
  public Optional<UdUserEntity> findByUsername(String username) {
    return findUser("SELECT * FROM \"user\" WHERE username = ?", username);
  }

  @Override
  public void delete(UdUserEntity user) {
    try (PreparedStatement ps = holder(CFG.userdataJdbcUrl()).connection().prepareStatement(
            "DELETE FROM \"user\" WHERE id = ?")) {
      ps.setObject(1, user.getId());
      ps.execute();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public List<UdUserEntity> findAll() {
    try (PreparedStatement ps = holder(CFG.userdataJdbcUrl()).connection().prepareStatement(
      "SELECT * FROM \"user\"")) {
      ps.execute();

      try (ResultSet rs = ps.getResultSet()) {
        List<UdUserEntity> users = new ArrayList<>();
        while (rs.next()) {
          UdUserEntity ue = UdUserEntityRowMapper.instance.mapRow(rs, rs.getRow());
          users.add(ue);
        }
        return users;
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public UdUserEntity update(UdUserEntity user) {
    try (PreparedStatement ps = holder(CFG.userdataJdbcUrl()).connection().prepareStatement(
            "UPDATE \"user\" SET username = ?, currency = ?, firstname = ?, surname = ?, photo = ?, photo_small = ?, full_name = ? WHERE id = ?");
         PreparedStatement friendPs = holder(CFG.userdataJdbcUrl()).connection().prepareStatement(
           "INSERT INTO friendship (requester_id, addressee_id, status) VALUES (?, ?, ?) " +
               "ON CONFLICT (requester_id, addressee_id) DO UPDATE SET status = ?")
    ) {
      ps.setString(1, user.getUsername());
      ps.setString(2, user.getCurrency().name());
      ps.setString(3, user.getFirstname());
      ps.setString(4, user.getSurname());
      ps.setBytes(5, user.getPhoto());
      ps.setBytes(6, user.getPhotoSmall());
      ps.setString(7, user.getFullname());
      ps.setObject(8, user.getId());
      ps.executeUpdate();

      for (FriendshipEntity fe : user.getFriendshipRequests()) {
        friendPs.setObject(1, user.getId());
        friendPs.setObject(2, fe.getAddressee().getId());
        friendPs.setString(3, fe.getStatus().name());
        friendPs.setString(4, fe.getStatus().name());
        friendPs.addBatch();
        friendPs.clearParameters();
      }
      friendPs.executeBatch();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
    return user;
  }

  private Optional<UdUserEntity> findUser(String query, Object param) {
    try (PreparedStatement ps = holder(CFG.userdataJdbcUrl()).connection().prepareStatement(query);
         PreparedStatement friendPs = holder(CFG.userdataJdbcUrl()).connection().prepareStatement(
           "SELECT DISTINCT u.id, u.username, u.currency, u.firstname, u.surname, u.full_name, u.photo, u.photo_small, f.status " +
             "FROM \"user\" u JOIN friendship f ON u.id = f.requester_id OR u.id = f.addressee_id " +
             "WHERE (f.requester_id = ? OR f.addressee_id = ?) AND u.id <> ? " + "ORDER BY f.status DESC")
    ) {
      if (param instanceof UUID) {
        ps.setObject(1, param);
      } else if (param instanceof String) {
        ps.setString(1, (String) param);
      }
      ps.execute();

      try (ResultSet rs = ps.getResultSet()) {
        if (rs.next()) {
          UdUserEntity user = UdUserEntityRowMapper.instance.mapRow(rs, rs.getRow());
          friendPs.setObject(1, user.getId());
          friendPs.setObject(2, user.getId());
          friendPs.setObject(3, user.getId());
          friendPs.execute();
          try (ResultSet friendRs = friendPs.getResultSet()) {
            while (friendRs.next()) {
              UdUserEntity friend = UdUserEntityRowMapper.instance.mapRow(friendRs, friendRs.getRow());
              user.addFriends(FriendshipStatus.valueOf(friendRs.getString("status")), friend);
            }
          }
          return Optional.of(user);
        } else {
          return Optional.empty();
        }
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }
}
