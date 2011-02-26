package edu.mit.compilers.le02.semanticchecks;

import edu.mit.compilers.le02.CompilerException;
import edu.mit.compilers.le02.SourceLocation;

public class SemanticException extends CompilerException {

  public SemanticException(int line, int col, String msg) {
    super(line, col, msg);
  }
  public SemanticException(int line, int col) {
    super(line, col);
  }
  public SemanticException(SourceLocation location, String msg) {
    super(location, msg);
  }
  public SemanticException(SourceLocation location) {
    super(location);
  }

}
