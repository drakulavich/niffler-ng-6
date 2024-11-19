package guru.qa.niffler.condition;

public record Bubble(
  Color color,
  String text
) {

  public static Bubble of(Color color, String text) {
    return new Bubble(color, text);
  }
}
