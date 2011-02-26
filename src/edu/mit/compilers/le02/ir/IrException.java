package edu.mit.compilers.le02.ir;

import edu.mit.compilers.le02.semanticchecks.SemanticException;
import edu.mit.compilers.le02.SourceLocation;

public class IrException extends SemanticException {
  public IrException(SourceLocation loc) {
    super(loc);
  }
  public IrException(SourceLocation loc, String text) {
    super(loc, text);
  }
  public IrException(int line, int col, String msg) {
    super(line, col, msg);
  }
  public IrException(int line, int col) {
    super(line, col);
  }
}
