package edu.mit.compilers.le02;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Util {

  public static <T> List<T> makeList(T... args) {
    ArrayList<T> list = new ArrayList<T>();
    for (T t : args) {
      list.add(t);
    }
    return list;
  }

  public static <T> List<T> emptyList(Class<T> T) {
    List<T> list = Collections.emptyList();
    return list;
  }

  /**
   * We need to replace the characters '\t', '\n', '\"', '\'', '\\' with their
   * escaped alternatives.
   */
  public static String escape(String s) {
    return s.replace(
      "\\", "\\\\").replace(
        "\t", "\\t").replace(
          "\n", "\\n").replace(
            "\"", "\\\"").replace(
              "\'", "\\\'");
  }

  /**
   * We need to replace the unescaped sequences
   * "\\t", "\\n", "\\\"", "\\'", "\\\\" with their unescaped alternatives.
   */
  public static String unescape(String s) {
    return s.replace(
      "\\t", "\t").replace(
        "\\n", "\n").replace(
          "\\\"", "\"").replace(
            "\\\'", "\'").replace(
              "\\\\", "\\");
  }
}
