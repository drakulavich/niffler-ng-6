package guru.qa.niffler.data.dao;

import guru.qa.niffler.data.entity.userdata.UdUserEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UdUserDao {
  UdUserEntity create(UdUserEntity user);
  UdUserEntity update(UdUserEntity user);
  Optional<UdUserEntity> findById(UUID id);
  Optional<UdUserEntity> findByUsername(String username);
  void delete(UdUserEntity user);
  List<UdUserEntity> findAll();
}
