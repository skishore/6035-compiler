package edu.mit.compilers.le02;

import antlr.collections.AST;
import edu.mit.compilers.tools.CLI;

public final class SourceLocation {
  private String filename;
  private int line;

  public String getFilename() {
    return filename;
  }

  public int getLine() {
    return line;
  }

  public int getCol() {
    return col;
  }

  private int col;

  public SourceLocation(String filename, int line, int col) {
    this.filename = filename;
    this.line = line;
    this.col = col;
  }

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
