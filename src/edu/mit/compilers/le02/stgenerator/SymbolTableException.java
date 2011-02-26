package edu.mit.compilers.le02.stgenerator;

import edu.mit.compilers.le02.semanticchecks.SemanticException;
import edu.mit.compilers.le02.SourceLocation;

public class SymbolTableException extends SemanticException {

  public SymbolTableException(int line, int col, String msg) {
    super(line, col, msg);
  }
  public SymbolTableException(int line, int col) {
    super(line, col);
  }
  public SymbolTableException(SourceLocation location, String msg) {
    super(location, msg);
  }
  public SymbolTableException(SourceLocation location) {
    super(location);
  }

}
