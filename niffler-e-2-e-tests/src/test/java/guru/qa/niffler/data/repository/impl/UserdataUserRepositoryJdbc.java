package guru.qa.niffler.data.repository.impl;

import guru.qa.niffler.data.dao.UdUserDao;
import guru.qa.niffler.data.dao.impl.FriendshipDaoJdbc;
import guru.qa.niffler.data.dao.impl.UdUserDaoJdbc;
import guru.qa.niffler.data.entity.userdata.FriendshipStatus;
import guru.qa.niffler.data.entity.userdata.UdUserEntity;
import guru.qa.niffler.data.repository.UserdataUserRepository;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Optional;
import java.util.UUID;

@ParametersAreNonnullByDefault
public class UserdataUserRepositoryJdbc implements UserdataUserRepository {

  private final UdUserDao udUserDao = new UdUserDaoJdbc();
  private final FriendshipDaoJdbc friendshipDao = new FriendshipDaoJdbc();

  @Nonnull
  @Override
  public UdUserEntity create(UdUserEntity user) {
    return udUserDao.create(user);
  }

  @Override
  public void sendInvitation(UdUserEntity requester, UdUserEntity addressee) {
    requester.addFriends(FriendshipStatus.PENDING, addressee);
    udUserDao.update(requester);
  }

  @Override
  public void addFriend(UdUserEntity requester, UdUserEntity addressee) {
    requester.addFriends(FriendshipStatus.ACCEPTED, addressee);
    addressee.addFriends(FriendshipStatus.ACCEPTED, requester);
    udUserDao.update(requester);
    udUserDao.update(addressee);
  }

  @Override
  public void remove(UdUserEntity user) {
    udUserDao.delete(user);
  }

  @Nonnull
  @Override
  public UdUserEntity update(UdUserEntity user) {
    return udUserDao.update(user);
  }

  @Nonnull
  @Override
  public Optional<UdUserEntity> findById(UUID id) {
    return udUserDao.findById(id);
  }

  @Nonnull
  @Override
  public Optional<UdUserEntity> findByUsername(String username) {
    return udUserDao.findByUsername(username);
  }

  @Override
  public void removeAll() {
    friendshipDao.deleteAll();
    udUserDao.deleteAll();
  }
}
