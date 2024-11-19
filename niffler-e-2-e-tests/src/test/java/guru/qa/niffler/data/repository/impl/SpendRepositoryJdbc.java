package guru.qa.niffler.data.repository.impl;

import guru.qa.niffler.data.dao.CategoryDao;
import guru.qa.niffler.data.dao.SpendDao;
import guru.qa.niffler.data.dao.impl.CategoryDaoJdbc;
import guru.qa.niffler.data.dao.impl.SpendDaoJdbc;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.data.repository.SpendRepository;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Optional;
import java.util.UUID;

@ParametersAreNonnullByDefault
public class SpendRepositoryJdbc implements SpendRepository {

  private final SpendDao spendDao = new SpendDaoJdbc();
  private final CategoryDao categoryDao = new CategoryDaoJdbc();

  @Nonnull
  @Override
  public SpendEntity create(SpendEntity spend) {
    final UUID categoryId = spend.getCategory().getId();
    if (categoryId == null || categoryDao.findById(categoryId).isEmpty()) {
      spend.setCategory(
        categoryDao.create(spend.getCategory())
      );
    }
    return spendDao.create(spend);
  }

  @Nonnull
  @Override
  public SpendEntity update(SpendEntity spend) {
    spendDao.update(spend);
    categoryDao.update(spend.getCategory());

    return spend;
  }

  @Nonnull
  @Override
  public CategoryEntity createCategory(CategoryEntity category) {
    return categoryDao.create(category);
  }

  @Nonnull
  @Override
  public Optional<CategoryEntity> findCategoryById(UUID id) {
    return categoryDao.findById(id);
  }

  @Nonnull
  @Override
  public Optional<CategoryEntity> findCategoryByUsernameAndSpendName(String username, String name) {
    return categoryDao.findCategoryByUsernameAndCategoryName(username, name);
  }

  @Nonnull
  @Override
  public Optional<SpendEntity> findById(UUID id) {
    return spendDao.findById(id);
  }

  @Nonnull
  @Override
  public Optional<SpendEntity> findByUsernameAndSpendDescription(String username, String description) {
    return spendDao.findByUsernameAndSpendDescription(username, description);
  }

  @Override
  public void remove(SpendEntity spend) {
    spendDao.deleteSpend(spend);
  }

  @Override
  public void removeCategory(CategoryEntity category) {
    categoryDao.deleteCategory(category);
  }

  @Nonnull
  @Override
  public CategoryEntity updateCategory(CategoryEntity category) {
    return categoryDao.update(category);
  }

  @Override
  public void removeAll() {
    spendDao.deleteAll();
    categoryDao.deleteAll();
  }
}
