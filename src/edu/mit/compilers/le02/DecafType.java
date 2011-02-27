package edu.mit.compilers.le02;

/**
 * Records all possible types in Decaf.
 */
public enum DecafType {
  VOID,
  INT,
  BOOLEAN,
  INT_ARRAY,
  BOOLEAN_ARRAY;

  /**
   * Given a type, returns the flattened type of the array if applicable,
   * or else the original scalar type.
   */
  public static DecafType simplify(DecafType type) {
    if (type == INT_ARRAY) {
      return INT;
    } else if (type == BOOLEAN_ARRAY) {
      return BOOLEAN;
    }
    return type;
  }
}
