package slimeknights.tconstruct.library.utils;

public class ColorHelper {
  public static int getA(int color) {
    return color >> 24 & 0xFF;
  }

  public static int getR(int color) {
    return color >> 0 & 0xFF;
  }

  public static int getG(int color) {
    return color >> 8 & 0xFF;
  }

  public static int getB(int color) {
    return color >> 16 & 0xFF;
  }

  public static int combine(int a, int b, int g, int r) {
    return (a & 0xFF) << 24 | (b & 0xFF) << 16 | (g & 0xFF) << 8 | (r & 0xFF);
  }
}
