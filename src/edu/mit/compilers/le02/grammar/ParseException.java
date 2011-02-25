package edu.mit.compilers.le02.grammar;

import antlr.MismatchedTokenException;
import antlr.RecognitionException;
import edu.mit.compilers.le02.CompilerException;
import edu.mit.compilers.le02.SourceLocation;

public class ParseException extends CompilerException {
  public ParseException(RecognitionException rex) {
    super(rex.getLine(), rex.getColumn(), rex.getMessage());

    if (rex instanceof MismatchedTokenException) {
      MismatchedTokenException mte = (MismatchedTokenException)rex;
      if (mte.token.getText() == null) {
        loc = new SourceLocation(loc.getFilename(), loc.getLine() - 1, 80);
      }
    }
  }
  public ParseException(String text) {
    super(SourceLocation.getSourceLocationWithoutDetails(), text);
  }
  @Override
  public String getMessage() {
    return super.getMessage().replaceFirst("null", "EOF");
  }
}
