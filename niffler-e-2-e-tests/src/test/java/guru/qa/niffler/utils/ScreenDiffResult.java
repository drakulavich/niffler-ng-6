package guru.qa.niffler.utils;

import guru.qa.niffler.jupiter.extension.ScreenShotTestExtension;
import ru.yandex.qatools.ashot.comparison.DiffMarkupPolicy;
import ru.yandex.qatools.ashot.comparison.ImageDiff;
import ru.yandex.qatools.ashot.comparison.ImageDiffer;
import ru.yandex.qatools.ashot.comparison.PointsMarkupPolicy;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.function.BooleanSupplier;

public class ScreenDiffResult implements BooleanSupplier {

  public static final int DIFF_SIZE_TRIGGER = 2500;
  private final BufferedImage expected;
  private final BufferedImage actual;
  private final ImageDiff diff;
  private final boolean hasDif;

  public ScreenDiffResult(BufferedImage actual, BufferedImage expected) {
    ScreenShotTestExtension.setExpected(expected);
    ScreenShotTestExtension.setActual(actual);

    DiffMarkupPolicy myMarkup = new PointsMarkupPolicy();
    myMarkup
      .withDiffColor(Color.MAGENTA)
      .setDiffSizeTrigger(DIFF_SIZE_TRIGGER);

    this.actual = actual;
    this.expected = expected;
    this.diff = new ImageDiffer()
      .withDiffMarkupPolicy(myMarkup)
      .makeDiff(expected, actual);
    this.hasDif = diff.hasDiff();
  }

  @Override
  public boolean getAsBoolean() {
    if (hasDif) {
      ScreenShotTestExtension.setDiff(diff.getMarkedImage());
    }
    return hasDif;
  }
}
