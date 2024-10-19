package guru.qa.niffler.data.dao;

import guru.qa.niffler.data.entity.auth.AuthorityEntity;

import java.util.List;
import java.util.UUID;

public interface AuthAuthorityDao {
  void create(AuthorityEntity... authority);
  void update(AuthorityEntity authority);
  void delete(AuthorityEntity authority);
  List<AuthorityEntity> findAll();
  List<AuthorityEntity> findAllByUserId(UUID userId);
}
