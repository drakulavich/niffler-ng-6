package guru.qa.niffler.data.dao;

import guru.qa.niffler.data.entity.userdata.FriendshipEntity;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public interface FriendshipDao {
  @Nonnull
  FriendshipEntity create(FriendshipEntity friendship);
  void deleteAll();
}
