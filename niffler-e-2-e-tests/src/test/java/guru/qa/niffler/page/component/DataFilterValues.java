package guru.qa.niffler.page.component;

import lombok.Getter;

@Getter
public enum DataFilterValues {
  ALL_TIME("All time"),
  LAST_MONTH("Last month"),
  LAST_WEEK("Last week"),
  TODAY("Today");

  private final String value;

  DataFilterValues(String value) {
    this.value = value;
  }
}
