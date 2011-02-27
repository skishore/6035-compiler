package edu.mit.compilers.le02;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Util {

  /**
   * Constructs a list of out of a vararg list of elements.
   * Allows easy construction of lists for ASTNode.getChildren()
   * and unit testing.
   */
  public static <T> List<T> makeList(T... args) {
    ArrayList<T> list = new ArrayList<T>();
    for (T t : args) {
      list.add(t);
    }
    return list;
  }

  /**
   * Returns an empty list containing the specified class type.
   */
  public static <T> List<T> emptyList(Class<T> T) {
    List<T> list = Collections.emptyList();
    return list;
  }

  /**
   * Escapes a string so it can be pretty-printed.
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
   * Unescapes a string from pretty-printed input format.
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
