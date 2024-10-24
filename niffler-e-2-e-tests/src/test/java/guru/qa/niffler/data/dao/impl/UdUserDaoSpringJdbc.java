package guru.qa.niffler.data.dao.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.UdUserDao;
import guru.qa.niffler.data.entity.userdata.UdUserEntity;
import guru.qa.niffler.data.mapper.UdUserEntityRowMapper;
import guru.qa.niffler.data.tpl.DataSources;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import javax.annotation.Nonnull;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class UdUserDaoSpringJdbc implements UdUserDao {

  private static final Config CFG = Config.getInstance();

  @Override
  public UdUserEntity update(UdUserEntity user) {
    JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.userdataJdbcUrl()));
    jdbcTemplate.update(
        "UPDATE \"user\" SET username = ?, currency = ?, firstname = ?, surname = ?, photo = ?, photo_small = ?, full_name = ? WHERE id = ?",
        user.getUsername(),
        user.getCurrency().name(),
        user.getFirstname(),
        user.getSurname(),
        user.getPhoto(),
        user.getPhotoSmall(),
        user.getFullname(),
        user.getId()
    );

    jdbcTemplate.batchUpdate("""
            INSERT INTO friendship (requester_id, addressee_id, status)
            VALUES (?, ?, ?)
            ON CONFLICT (requester_id, addressee_id)
            DO UPDATE SET status = ?
            """,
      new BatchPreparedStatementSetter() {
        @Override
        public void setValues(@Nonnull PreparedStatement ps, int i) throws SQLException {
          ps.setObject(1, user.getId());
          ps.setObject(2, user.getFriendshipRequests().get(i).getAddressee().getId());
          ps.setString(3, user.getFriendshipRequests().get(i).getStatus().name());
          ps.setString(4, user.getFriendshipRequests().get(i).getStatus().name());
        }

        @Override
        public int getBatchSize() {
          return user.getFriendshipRequests().size();
        }
      });
    return user;
  }

  @Override
  public UdUserEntity create(UdUserEntity user) {
    JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.userdataJdbcUrl()));
    KeyHolder kh = new GeneratedKeyHolder();
    jdbcTemplate.update(con -> {
      PreparedStatement ps = con.prepareStatement(
          "INSERT INTO \"user\" (username, currency, firstname, surname, photo, photo_small, full_name) " +
              "VALUES (?,?,?,?,?,?,?)",
          Statement.RETURN_GENERATED_KEYS
      );
      ps.setString(1, user.getUsername());
      ps.setString(2, user.getCurrency().name());
      ps.setString(3, user.getFirstname());
      ps.setString(4, user.getSurname());
      ps.setBytes(5, user.getPhoto());
      ps.setBytes(6, user.getPhotoSmall());
      ps.setString(7, user.getFullname());
      return ps;
    }, kh);

    final UUID generatedKey = (UUID) kh.getKeys().get("id");
    user.setId(generatedKey);
    return user;
  }

  @Override
  public Optional<UdUserEntity> findById(UUID id) {
    JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.userdataJdbcUrl()));
    return Optional.ofNullable(
        jdbcTemplate.queryForObject(
            "SELECT * FROM \"user\" WHERE id = ?",
            UdUserEntityRowMapper.instance,
            id
        )
    );
  }

  @Override
  public Optional<UdUserEntity> findByUsername(String username) {
    JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.userdataJdbcUrl()));
    try {
      return Optional.ofNullable(
        jdbcTemplate.queryForObject(
          "SELECT * FROM \"user\" WHERE username = ?",
          UdUserEntityRowMapper.instance,
          username
        )
      );
    } catch (
      EmptyResultDataAccessException e) {
      return Optional.empty();
    }
  }

  @Override
  public void delete(UdUserEntity user) {
    JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.userdataJdbcUrl()));
    jdbcTemplate.update(
        "DELETE FROM \"user\" WHERE id = ?",
        user.getId()
    );
  }

  @Override
  public List<UdUserEntity> findAll() {
    JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.userdataJdbcUrl()));
    return jdbcTemplate.query(
        "SELECT * FROM \"user\"",
        UdUserEntityRowMapper.instance
    );
  }
}