package edu.mit.compilers.le02.grammar;

import antlr.RecognitionException;
import edu.mit.compilers.le02.CompilerException;
import edu.mit.compilers.le02.SourceLocation;

public class ParseException extends CompilerException {
  public ParseException(RecognitionException rex) {
    super(rex.getLine(), rex.getColumn(), rex.getMessage());
  }
  public ParseException(String text) {
    super(SourceLocation.getSourceLocationWithoutDetails(), text);
  }
}
