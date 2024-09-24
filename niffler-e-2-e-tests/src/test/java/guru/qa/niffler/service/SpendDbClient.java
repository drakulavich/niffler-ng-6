package guru.qa.niffler.service;

import guru.qa.niffler.data.dao.CategoryDao;
import guru.qa.niffler.data.dao.SpendDao;
import guru.qa.niffler.data.dao.impl.CategoryDaoJdbc;
import guru.qa.niffler.data.dao.impl.SpendDaoJdbc;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;

import java.util.Optional;

public class SpendDbClient {

  private final SpendDao spendDao = new SpendDaoJdbc();
  private final CategoryDao categoryDao = new CategoryDaoJdbc();

  public SpendJson createSpend(SpendJson spend) {
    SpendEntity spendEntity = SpendEntity.fromJson(spend);
    Optional<CategoryEntity> categoryEntity = categoryDao.findCategoryByUsernameAndCategoryName(
        spendEntity.getUsername(),
        spendEntity.getCategory().getName()
    );
    if (categoryEntity.isPresent()) {
      spendEntity.setCategory(categoryEntity.get());
    } else {
      CategoryEntity createdCategory = categoryDao.create(spendEntity.getCategory());
      spendEntity.setCategory(createdCategory);
    }

    return SpendJson.fromEntity(
        spendDao.create(spendEntity)
    );
  }

  public CategoryJson createCategory(CategoryJson category) {
    CategoryEntity categoryEntity = CategoryEntity.fromJson(category);
    return CategoryJson.fromEntity(
      categoryDao.create(categoryEntity)
    );
  }

  public void deleteCategory(CategoryJson category) {
    categoryDao.deleteCategory(
      CategoryEntity.fromJson(category)
    );
  }
}
