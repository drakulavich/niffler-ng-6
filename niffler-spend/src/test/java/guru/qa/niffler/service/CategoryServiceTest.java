package guru.qa.niffler.service;

import guru.qa.niffler.data.CategoryEntity;
import guru.qa.niffler.data.repository.CategoryRepository;
import guru.qa.niffler.ex.TooManyCategoriesException;
import guru.qa.niffler.model.CategoryJson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

  private CategoryService testedObject;

  private static final int MAX_CATEGORIES_SIZE = 7;

  private final UUID mainCategoryUuid = UUID.randomUUID();
  private final String mainCategoryName = "Wine";
  private CategoryEntity mainCategory;

  private final UUID secondCategoryUuid = UUID.randomUUID();
  private final String secondCategoryName = "Beer";
  private CategoryEntity secondCategory;

  private final UUID thirdCategoryUuid = UUID.randomUUID();
  private final String thirdCategoryName = "Entertainment";
  private CategoryEntity thirdCategory;

  private final String mainTestUserName = "testUser";

  @BeforeEach
  void init() {
    // Create test category data - some archived, some not
    mainCategory = new CategoryEntity();
    mainCategory.setId(mainCategoryUuid);
    mainCategory.setName(mainCategoryName);
    mainCategory.setUsername(mainTestUserName);
    mainCategory.setArchived(false);

    secondCategory = new CategoryEntity();
    secondCategory.setId(secondCategoryUuid);
    secondCategory.setName(secondCategoryName);
    secondCategory.setUsername(mainTestUserName);
    secondCategory.setArchived(false);

    thirdCategory = new CategoryEntity();
    thirdCategory.setId(thirdCategoryUuid);
    thirdCategory.setName(thirdCategoryName);
    thirdCategory.setUsername(mainTestUserName);
    thirdCategory.setArchived(true);
  }

  @Test
  void getAllCategoriesShouldFilterArchivedCategoriesWhenRequested(@Mock CategoryRepository categoryRepository) {
    List<CategoryEntity> allCategories = List.of(mainCategory, secondCategory, thirdCategory);
    when(categoryRepository.findAllByUsernameOrderByName(mainTestUserName)).thenReturn(allCategories);

    testedObject = new CategoryService(categoryRepository);

    // Test case 1: Get all categories including archived
    List<CategoryJson> resultWithArchived = testedObject.getAllCategories(mainTestUserName, false);
    assertEquals(3, resultWithArchived.size());

    // Test case 2: Get only active categories
    List<CategoryJson> resultWithoutArchived = testedObject.getAllCategories(mainTestUserName, true);
    assertEquals(2, resultWithoutArchived.size());
  }

  @Test
  void updateShouldThrowExceptionWhenUnarchivingWouldExceedMaxLimit(@Mock CategoryRepository categoryRepository) {
    when(categoryRepository.findByUsernameAndId(mainTestUserName, thirdCategoryUuid))
      .thenReturn(Optional.of(thirdCategory));
    when(categoryRepository.countByUsernameAndArchived(mainTestUserName, false))
      .thenReturn((long) MAX_CATEGORIES_SIZE + 1); // Above max limit

    CategoryJson categoryToUnarchive = new CategoryJson(
      thirdCategoryUuid,
      thirdCategoryName,
      mainTestUserName,
      false // Unarchiving
    );

    testedObject = new CategoryService(categoryRepository);
    TooManyCategoriesException ex = assertThrows(TooManyCategoriesException.class,
      () -> testedObject.update(categoryToUnarchive));

    assertEquals("Can`t unarchive category for user: '" + mainTestUserName + "'", ex.getMessage());
    verify(categoryRepository, never()).save(any(CategoryEntity.class));
  }

  @Test
  void saveShouldThrowExceptionWhenCategoriesExceedMaxLimit(@Mock CategoryRepository categoryRepository) {
    when(categoryRepository.countByUsernameAndArchived(mainTestUserName, false))
      .thenReturn((long) MAX_CATEGORIES_SIZE + 1); // Above max limit

    CategoryJson categoryToSave = new CategoryJson(
      null,
      "New category",
      mainTestUserName,
      false
    );

    testedObject = new CategoryService(categoryRepository);
    TooManyCategoriesException ex = assertThrows(TooManyCategoriesException.class,
      () -> testedObject.save(categoryToSave));

    assertEquals("Can`t add over than 8 categories for user: '" + mainTestUserName + "'", ex.getMessage());
    verify(categoryRepository, never()).save(any(CategoryEntity.class));
  }
}
