package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.service.SpendDbClient;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.junit.platform.commons.support.AnnotationSupport;

import static guru.qa.niffler.utils.RandomDataUtils.randomCategoryName;

public class CategoryExtension implements BeforeEachCallback, ParameterResolver, AfterTestExecutionCallback {
  public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(CategoryExtension.class);

  private final SpendDbClient spendDbClient = new SpendDbClient();

  @Override
  public void beforeEach(ExtensionContext context) throws Exception {
    AnnotationSupport.findAnnotation(context.getRequiredTestMethod(), User.class)
      .ifPresent(anno -> {
        if (anno.categories().length == 0) {
          return;
        }
        Category category = anno.categories()[0];
        String categoryTitle = "".equals(category.title())
          ? randomCategoryName()
          : category.title();
        CategoryJson categoryJson = new CategoryJson(
          null,
          categoryTitle,
          anno.username(),
          category.archived()
        );

        CategoryJson createdCategory = spendDbClient.createCategory(categoryJson);
        context.getStore(NAMESPACE).put(
          context.getUniqueId(),
          createdCategory
        );
      });
  }

  @Override
  public void afterTestExecution(ExtensionContext context) {
    // Delete category
    CategoryJson category = context.getStore(NAMESPACE).get(context.getUniqueId(), CategoryJson.class);
    if (category == null) {
      return;
    }

    spendDbClient.deleteCategory(category);
  }

  @Override
  public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
    return parameterContext.getParameter().getType().isAssignableFrom(CategoryJson.class);
  }

  @Override
  public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
    return extensionContext.getStore(NAMESPACE).get(extensionContext.getUniqueId(), CategoryJson.class);
  }
}
