package edu.mit.compilers.le02;

public enum DecafType {
  VOID,
  INT,
  BOOLEAN,
  INT_ARRAY,
  BOOLEAN_ARRAY;

  public static DecafType simplify(DecafType type) {
    if (type == INT_ARRAY) {
      return INT;
    } else if (type == BOOLEAN_ARRAY) {
      return BOOLEAN;
    }
    return type;
  }
}
