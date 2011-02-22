package edu.mit.compilers.le02.symboltable;

import edu.mit.compilers.le02.ast.SourceLocation;

public class SymbolTableException extends Exception {

  private SourceLocation loc;

  public SymbolTableException(SourceLocation location) {
    loc = location;
  }
  public SymbolTableException(SourceLocation location, String msg) {
    super(msg);
    loc = location;
  }

  @Override
  public String getMessage() {
    return super.getMessage() + " at " + loc;
  }
}
