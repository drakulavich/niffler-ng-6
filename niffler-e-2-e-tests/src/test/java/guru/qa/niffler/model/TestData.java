package guru.qa.niffler.model;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public record TestData(
  String password,
  List<CategoryJson> categories,
  List<SpendJson> spendings,
  List<String> income,
  List<String> outcome,
  List<String> friends
) {

  public TestData(@Nonnull String password) {
    this(password, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
  }
}
