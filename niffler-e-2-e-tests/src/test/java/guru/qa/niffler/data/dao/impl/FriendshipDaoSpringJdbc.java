package guru.qa.niffler.data.dao.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.FriendshipDao;
import guru.qa.niffler.data.entity.userdata.FriendshipEntity;
import guru.qa.niffler.data.jdbc.DataSources;
import org.jetbrains.annotations.NotNull;
import org.springframework.jdbc.core.JdbcTemplate;

public class FriendshipDaoSpringJdbc implements FriendshipDao {

  private static final Config CFG = Config.getInstance();

  @NotNull
  @Override
  public FriendshipEntity create(FriendshipEntity friendship) {
    JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.userdataJdbcUrl()));
    jdbcTemplate.update(
        "INSERT INTO friendship (requester_id, addressee_id, status, created_at) VALUES (?, ?, ?, ?)",
        friendship.getRequester().getId(),
        friendship.getAddressee().getId(),
        friendship.getStatus().name(),
        friendship.getCreatedDate()
    );
    return friendship;
  }

  @Override
  public void deleteAll() {
    JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.userdataJdbcUrl()));
    jdbcTemplate.update("DELETE FROM friendship");
  }
}
