package edu.mit.compilers.le02.grammar;

import antlr.TokenStreamRecognitionException;

import edu.mit.compilers.le02.CompilerException;
import edu.mit.compilers.le02.SourceLocation;


/**
 * Represents an exception discovered in the DecafScanner.
 */
public class ScanException extends CompilerException {
  public ScanException(TokenStreamRecognitionException rex) {
    super(rex.recog.getLine(), rex.recog.getColumn(), rex.recog.getMessage());
  }
  public ScanException(String text) {
    super(SourceLocation.getSourceLocationWithoutDetails(), text);
  }
}
