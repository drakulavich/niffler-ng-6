package guru.qa.niffler.data.repository.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.entity.userdata.FriendshipEntity;
import guru.qa.niffler.data.entity.userdata.FriendshipStatus;
import guru.qa.niffler.data.entity.userdata.UdUserEntity;
import guru.qa.niffler.data.mapper.UdUserEntityRowMapper;
import guru.qa.niffler.data.repository.UserdataUserRepository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static guru.qa.niffler.data.tpl.Connections.holder;

public class UserdataUserRepositoryJdbc implements UserdataUserRepository {

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
    try (PreparedStatement ps = holder(CFG.userdataJdbcUrl()).connection().prepareStatement("SELECT * FROM \"user\" WHERE id = ? ");
         PreparedStatement friendPs = holder(CFG.userdataJdbcUrl()).connection().prepareStatement(
           "SELECT DISTINCT u.id, u.username, u.currency, u.firstname, u.surname, u.full_name, u.photo, u.photo_small, f.status " +
             "FROM \"user\" u JOIN friendship f ON u.id = f.requester_id OR u.id = f.addressee_id " +
             "WHERE (f.requester_id = ? OR f.addressee_id = ?) AND u.id <> ? " +
             "ORDER BY f.status DESC"
         )
    ) {
      ps.setObject(1, id);
      ps.execute();

      try (ResultSet rs = ps.getResultSet()) {
        if (rs.next()) {
          UdUserEntity user = UdUserEntityRowMapper.instance.mapRow(rs, 1);
          friendPs.setObject(1, id);
          friendPs.setObject(2, id);
          friendPs.setObject(3, id);
          friendPs.execute();
          try (ResultSet friendRs = friendPs.getResultSet()) {
            while (friendRs.next()) {
              UdUserEntity friend = UdUserEntityRowMapper.instance.mapRow(friendRs, 1);
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

  @Override
  public void sendInvitation(UdUserEntity requester, UdUserEntity addressee) {
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

  @Override
  public void remove(UdUserEntity user) {

  }

  @Override
  public Optional<UdUserEntity> findByUsername(String username) {
    return Optional.empty();
  }

  @Override
  public UdUserEntity update(UdUserEntity user) {
    return null;
  }
}
