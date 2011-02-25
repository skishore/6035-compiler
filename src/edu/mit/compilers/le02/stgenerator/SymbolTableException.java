package edu.mit.compilers.le02.stgenerator;

import edu.mit.compilers.le02.SourceLocation;

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
