package edu.mit.compilers.le02.ir;

import edu.mit.compilers.le02.SourceLocation;

public class IrException extends Exception {

  private SourceLocation loc;

  public IrException(SourceLocation location) {
    loc = location;
  }
  public IrException(SourceLocation location, String msg) {
    super(msg);
    loc = location;
  }

  @Override
  public String getMessage() {
    return super.getMessage() + " at " + loc;
  }
}
