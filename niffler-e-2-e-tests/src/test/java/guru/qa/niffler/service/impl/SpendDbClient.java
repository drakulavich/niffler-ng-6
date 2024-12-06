package guru.qa.niffler.service.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.data.repository.SpendRepository;
import guru.qa.niffler.data.repository.impl.SpendRepositoryHibernate;
import guru.qa.niffler.data.tpl.XaTransactionTemplate;
import guru.qa.niffler.model.rest.CategoryJson;
import guru.qa.niffler.model.rest.SpendJson;
import guru.qa.niffler.service.SpendClient;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Objects;

@ParametersAreNonnullByDefault
public class SpendDbClient implements SpendClient {

  private static final Config CFG = Config.getInstance();
  private final SpendRepository spendRepository = new SpendRepositoryHibernate();

  private final XaTransactionTemplate xaTransactionTemplate = new XaTransactionTemplate(
    CFG.spendJdbcUrl()
  );

  @Nonnull
  @Override
  public SpendJson createSpend(SpendJson spend) {
    return Objects.requireNonNull(xaTransactionTemplate.execute(() -> {
        SpendEntity spendEntity = SpendEntity.fromJson(spend);
        return SpendJson.fromEntity(spendRepository.create(spendEntity));
      }
    ));
  }

  @Override
  public void removeCategory(CategoryJson category) {
    xaTransactionTemplate.execute(() -> {
        CategoryEntity categoryEntity = CategoryEntity.fromJson(category);
        spendRepository.removeCategory(categoryEntity);
        return null;
      }
    );
  }

  @Nonnull
  @Override
  public CategoryJson createCategory(CategoryJson category) {
    return Objects.requireNonNull(xaTransactionTemplate.execute(() -> CategoryJson.fromEntity(
        spendRepository.createCategory(CategoryEntity.fromJson(category))
      )
    ));
  }

  @Nonnull
  @Override
  public CategoryJson updateCategory(CategoryJson category) {
    return Objects.requireNonNull(xaTransactionTemplate.execute(() -> {
        CategoryEntity categoryEntity = CategoryEntity.fromJson(category);
        return CategoryJson.fromEntity(spendRepository.updateCategory(categoryEntity));
      }
    ));
  }

  public void removeAllSpends() {
    xaTransactionTemplate.execute(() -> {
        spendRepository.removeAll();
        return null;
      }
    );
  }
}
