package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.impl.CategoryDaoJdbc;
import guru.qa.niffler.data.dao.impl.CategoryDaoSpringJdbc;
import guru.qa.niffler.data.dao.impl.SpendDaoJdbc;
import guru.qa.niffler.data.dao.impl.SpendDaoSpringJdbc;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;

import java.sql.Connection;
import java.util.Optional;
import java.util.function.Consumer;

import static guru.qa.niffler.data.Databases.dataSource;
import static guru.qa.niffler.data.Databases.transaction;

public class SpendDbClient {

  private static final Config CFG = Config.getInstance();

    public SpendJson createSpend(SpendJson spend) {
        return transaction(connection -> {
            SpendEntity spendEntity = SpendEntity.fromJson(spend);

            Optional<CategoryEntity> categoryEntity = new CategoryDaoJdbc(connection).findCategoryByUsernameAndCategoryName(
                    spendEntity.getUsername(),
                    spendEntity.getCategory().getName()
            );
            if (categoryEntity.isPresent()) {
                spendEntity.setCategory(categoryEntity.get());
            }
            return SpendJson.fromEntity(
                    new SpendDaoJdbc(connection).create(spendEntity)
            );
        },
        CFG.spendJdbcUrl()
        );
    }

  public CategoryJson createCategory(CategoryJson category) {
    return transaction(connection -> {
          CategoryEntity categoryEntity = CategoryEntity.fromJson(category);
          return CategoryJson.fromEntity(
              new CategoryDaoJdbc(connection).create(categoryEntity)
          );
        },
        CFG.spendJdbcUrl()
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
