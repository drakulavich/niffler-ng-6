package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.CategoryDao;
import guru.qa.niffler.data.dao.SpendDao;
import guru.qa.niffler.data.dao.impl.CategoryDaoJdbc;
import guru.qa.niffler.data.dao.impl.CategoryDaoSpringJdbc;
import guru.qa.niffler.data.dao.impl.SpendDaoJdbc;
import guru.qa.niffler.data.dao.impl.SpendDaoSpringJdbc;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;

import static guru.qa.niffler.data.Databases.transaction;

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
          if (spendEntity.getCategory().getId() == null) {
            CategoryEntity categoryEntity = categoryDao.create(spendEntity.getCategory());
            spendEntity.setCategory(categoryEntity);
          }
          return SpendJson.fromEntity(
              spendDao.create(spendEntity)
          );
        }
    );
  }

  public void deleteCategory(CategoryJson category) {
    transaction((Consumer<Connection>) connection -> new CategoryDaoJdbc(connection)
        .deleteCategory(
            CategoryEntity.fromJson(category)
        ),
        CFG.spendJdbcUrl()
    );
  }

  public SpendJson createSpendSpringJdbc(SpendJson spend) {
    SpendEntity spendEntityCreated = new SpendDaoSpringJdbc(
        dataSource(CFG.spendJdbcUrl())
    ).create(SpendEntity.fromJson(spend));

    return SpendJson.fromEntity(spendEntityCreated);
  }

  public CategoryJson createCategorySpringJdbc(CategoryJson category) {
    CategoryEntity categoryEntityCreated = new CategoryDaoSpringJdbc(
        dataSource(CFG.spendJdbcUrl())
    ).create(CategoryEntity.fromJson(category));

    return CategoryJson.fromEntity(categoryEntityCreated);
  }
}
