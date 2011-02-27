package edu.mit.compilers.le02;

import antlr.collections.AST;
import edu.mit.compilers.tools.CLI;

/**
 * Encapsulates the location of a specific token in the code.
 * Used to associate source locations with ASTNodes and CompilerExceptions.
 */
public final class SourceLocation {
  /** The filename of the token or error. */
  private String filename;

  /** The line number of the token or error. */
  private int line;
  /** The column of the token or error. */
  private int col;

  public String getFilename() {
    return filename;
  }

  public int getLine() {
    return line;
  }

  public int getCol() {
    return col;
  }

  /**
   * Initializes a SourceLocation with a known filename, line, and column.
   */
  public SourceLocation(String filename, int line, int col) {
    this.filename = filename;
    this.line = line;
    this.col = col;
  }

  /**
   * Static method to return a generic error within the current file without
   * line or column information.
   */
  public static SourceLocation getSourceLocationWithoutDetails() {
    return new SourceLocation(CLI.getInputFilename(), -1, -1);
  }

  /**
   * Utility method used to create a SourceLocation from an Antlr AST node.
   */
  public SourceLocation(AST node) {
    this(CLI.getInputFilename(), node.getLine(), node.getColumn());
  }

  @Override
  public String toString() {
    return filename + ":" + line + ":" + col;
  }
}
