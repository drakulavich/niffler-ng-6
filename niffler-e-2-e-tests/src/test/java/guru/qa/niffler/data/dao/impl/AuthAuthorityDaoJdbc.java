package guru.qa.niffler.data.dao.impl;

import guru.qa.niffler.data.dao.AuthAuthorityDao;
import guru.qa.niffler.data.entity.auth.Authority;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AuthAuthorityDaoJdbc implements AuthAuthorityDao {
  private final Connection connection;

  public AuthAuthorityDaoJdbc(Connection connection) {
    this.connection = connection;
  }

  @Override
  public void create(AuthorityEntity... authority) {
    try (PreparedStatement ps = connection.prepareStatement(
        "INSERT INTO authority (user_id, authority) VALUES (?, ?)"
    )) {
      for (AuthorityEntity auth : authority) {
        ps.setObject(1, auth.getUserId());
        ps.setString(2, auth.getAuthority().name());
        ps.addBatch();
        ps.clearParameters();
      }

      ps.executeBatch();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void delete(AuthorityEntity authority) {
    try (PreparedStatement ps = connection.prepareStatement(
        "DELETE FROM authority WHERE id = ?"
    )) {
      ps.setObject(1, authority.getId());
      ps.executeUpdate();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public List<AuthorityEntity> findAll() {
    try (PreparedStatement ps = connection.prepareStatement(
        "SELECT * FROM authority"
    )) {
      ps.execute();

      try (ResultSet rs = ps.getResultSet()) {
        List<AuthorityEntity> authorities = new ArrayList<>();
        while (rs.next()) {
          AuthorityEntity authority = new AuthorityEntity();
          authority.setId(rs.getObject("id", java.util.UUID.class));
          authority.setUserId(rs.getObject("user_id", java.util.UUID.class));
          authority.setAuthority(Authority.valueOf(rs.getString("authority")));
          authorities.add(authority);
        }
        return authorities;
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }
}
