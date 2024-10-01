package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.CategoryDao;
import guru.qa.niffler.data.dao.SpendDao;
import guru.qa.niffler.data.dao.impl.CategoryDaoJdbc;
import guru.qa.niffler.data.dao.impl.SpendDaoJdbc;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.data.tpl.JdbcTransactionTemplate;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;

import java.util.Optional;

public class SpendDbClient {

  private static final Config CFG = Config.getInstance();

  private final CategoryDao categoryDao = new CategoryDaoJdbc();
  private final SpendDao spendDao = new SpendDaoJdbc();

  private final JdbcTransactionTemplate jdbcTxTemplate = new JdbcTransactionTemplate(
      CFG.spendJdbcUrl()
  );

  public SpendJson createSpend(SpendJson spend) {
    return jdbcTxTemplate.execute(() -> {
          SpendEntity spendEntity = SpendEntity.fromJson(spend);
          // Find existing category by username and category name
          Optional<CategoryEntity> categoryEntity = categoryDao.findCategoryByUsernameAndCategoryName(
            spendEntity.getUsername(),
            spendEntity.getCategory().getName()
          );
          if (categoryEntity.isPresent()) {
            spendEntity.setCategory(categoryEntity.get());
          }
          return SpendJson.fromEntity(
              spendDao.create(spendEntity)
          );
        }
    );
  }

  public void deleteCategory(CategoryJson category) {
    jdbcTxTemplate.execute(() -> {
        CategoryEntity categoryEntity = CategoryEntity.fromJson(category);
        categoryDao.deleteCategory(categoryEntity);
        return null;
      }
    );
  }

  public CategoryJson createCategory(CategoryJson category) {
    return jdbcTxTemplate.execute(() -> CategoryJson.fromEntity(
        categoryDao.create(CategoryEntity.fromJson(category))
    )
    );
  }
}
