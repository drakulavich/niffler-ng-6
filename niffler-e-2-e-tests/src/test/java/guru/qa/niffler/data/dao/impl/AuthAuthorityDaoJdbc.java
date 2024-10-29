package guru.qa.niffler.data.dao.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.AuthAuthorityDao;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import guru.qa.niffler.data.mapper.AuthorityEntityRowMapper;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static guru.qa.niffler.data.jdbc.Connections.holder;

@ParametersAreNonnullByDefault
public class AuthAuthorityDaoJdbc implements AuthAuthorityDao {

  private static final Config CFG = Config.getInstance();

  @SuppressWarnings("resource")
  @Nonnull
  @Override
  public void create(AuthorityEntity... authority) {
    try (PreparedStatement ps = holder(CFG.authJdbcUrl()).connection().prepareStatement(
        "INSERT INTO \"authority\" (user_id, authority) VALUES (?, ?)")) {
      for (AuthorityEntity a : authority) {
        ps.setObject(1, a.getUser().getId());
        ps.setString(2, a.getAuthority().name());
        ps.addBatch();
        ps.clearParameters();
      }
      ps.executeBatch();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @SuppressWarnings("resource")
  @Nonnull
  @Override
  public void update(AuthorityEntity authority) {
    try (PreparedStatement ps = holder(CFG.authJdbcUrl()).connection().prepareStatement(
        "UPDATE authority SET authority = ? WHERE id = ?"
    )) {
      ps.setString(1, authority.getAuthority().name());
      ps.setObject(2, authority.getId());
      ps.executeUpdate();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @SuppressWarnings("resource")
  @Override
  public void delete(AuthorityEntity authority) {
    try (PreparedStatement ps = holder(CFG.authJdbcUrl()).connection().prepareStatement(
        "DELETE FROM authority WHERE id = ?"
    )) {
      ps.setObject(1, authority.getId());
      ps.executeUpdate();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @SuppressWarnings("resource")
  @Nonnull
  @Override
  public List<AuthorityEntity> findAll() {
    try (PreparedStatement ps = holder(CFG.authJdbcUrl()).connection().prepareStatement(
        "SELECT * FROM authority"
    )) {
      ps.execute();

      try (ResultSet rs = ps.getResultSet()) {
        List<AuthorityEntity> authorities = new ArrayList<>();
        while (rs.next()) {
          AuthorityEntity authority = AuthorityEntityRowMapper.instance.mapRow(rs, rs.getRow());
          authorities.add(authority);
        }
        return authorities;
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @SuppressWarnings("resource")
  @Nonnull
  @Override
  public List<AuthorityEntity> findAllByUserId(UUID userId) {
    try (PreparedStatement ps = holder(CFG.authJdbcUrl()).connection().prepareStatement(
        "SELECT * FROM authority WHERE user_id = ?"
    )) {
      ps.setObject(1, userId);
      ps.execute();

      try (ResultSet rs = ps.getResultSet()) {
        List<AuthorityEntity> authorities = new ArrayList<>();
        while (rs.next()) {
          AuthorityEntity authority = AuthorityEntityRowMapper.instance.mapRow(rs, rs.getRow());
          authorities.add(authority);
        }
        return authorities;
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }
}
