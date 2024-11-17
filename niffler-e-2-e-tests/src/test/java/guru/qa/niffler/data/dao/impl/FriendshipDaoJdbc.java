package guru.qa.niffler.data.dao.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.FriendshipDao;
import guru.qa.niffler.data.entity.userdata.FriendshipEntity;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.sql.PreparedStatement;

import static guru.qa.niffler.data.jdbc.Connections.holder;

@ParametersAreNonnullByDefault
public class FriendshipDaoJdbc implements FriendshipDao {

  private static final Config CFG = Config.getInstance();

  @SuppressWarnings("resource")
  @Nonnull
  @Override
  public FriendshipEntity create(FriendshipEntity friendship) {
    try (PreparedStatement ps = holder(CFG.userdataJdbcUrl()).connection().prepareStatement(
        "INSERT INTO friendship (requester_id, addressee_id, status, created_at) VALUES (?, ?, ?, ?)"
    )) {
      ps.setObject(1, friendship.getRequester().getId());
      ps.setObject(2, friendship.getAddressee().getId());
      ps.setString(3, friendship.getStatus().name());
      ps.setObject(4, friendship.getCreatedDate());
      ps.executeUpdate();
      return friendship;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void deleteAll() {
    try (PreparedStatement ps = holder(CFG.userdataJdbcUrl()).connection().prepareStatement(
        "DELETE FROM friendship"
    )) {
      ps.execute();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
