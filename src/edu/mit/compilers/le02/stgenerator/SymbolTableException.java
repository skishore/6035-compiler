package edu.mit.compilers.le02.stgenerator;

import edu.mit.compilers.le02.CompilerException;
import edu.mit.compilers.le02.SourceLocation;

public class SymbolTableException extends CompilerException {

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
