package guru.qa.niffler.data.dao;

import guru.qa.niffler.data.entity.spend.SpendEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SpendDao {
  SpendEntity create(SpendEntity spend);
  Optional<SpendEntity> findById(UUID id);
  SpendEntity update(SpendEntity spend);
  Optional<SpendEntity> findByUsernameAndSpendDescription(String username, String description);
  List<SpendEntity> findAllByUsername(String username);
  void deleteSpend(SpendEntity spend);
  List<SpendEntity> findAll();
}
