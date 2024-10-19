package guru.qa.niffler.data.dao.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.AuthAuthorityDao;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import guru.qa.niffler.data.mapper.AuthorityEntityRowMapper;
import guru.qa.niffler.data.tpl.DataSources;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public class AuthAuthorityDaoSpringJdbc implements AuthAuthorityDao {

  private static final Config CFG = Config.getInstance();

  @Override
  public void create(AuthorityEntity... authority) {
    JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.authJdbcUrl()));
    jdbcTemplate.batchUpdate(
        "INSERT INTO authority (user_id, authority) VALUES (? , ?)",
        new BatchPreparedStatementSetter() {
          @Override
          public void setValues(PreparedStatement ps, int i) throws SQLException {
            ps.setObject(1, authority[i].getUser().getId());
            ps.setString(2, authority[i].getAuthority().name());
          }

          @Override
          public int getBatchSize() {
            return authority.length;
          }
        }
    );
  }

  @Override
  public void delete(AuthorityEntity authority) {
    JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.authJdbcUrl()));
    jdbcTemplate.update(
        "DELETE FROM authority WHERE id = ?",
        authority.getId()
    );
  }

  @Override
  public List<AuthorityEntity> findAll() {
    JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.authJdbcUrl()));
    return jdbcTemplate.query(
        "SELECT * FROM authority",
      AuthorityEntityRowMapper.instance
    );
  }

  @Override
  public void update(AuthorityEntity authority) {
    JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(Config.getInstance().authJdbcUrl()));
    jdbcTemplate.update(
      "UPDATE authority SET authority = ? WHERE id = ?",
      authority.getAuthority().name(),
      authority.getId()
    );
  }

  @Override
  public List<AuthorityEntity> findAllByUserId(UUID userId) {
    JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(Config.getInstance().authJdbcUrl()));
    return jdbcTemplate.query(
      "SELECT * FROM authority WHERE user_id = ?",
      AuthorityEntityRowMapper.instance,
      userId
    );
  }
}
