package edu.mit.compilers.le02;

import edu.mit.compilers.tools.CLI;


/**
 * A generic compiler exception that encapsulates a location in the input
 * where the error occurred. Like other Exceptions, also stores stack trace
 * information for the error occurred in the code.
 */
public class CompilerException extends Exception {

  /** The source location in the input where the error was thrown. */
  protected SourceLocation loc;

  public CompilerException(SourceLocation location) {
    loc = location;
  }
  public CompilerException(SourceLocation location, String msg) {
    super(msg);
    loc = location;
  }
  public CompilerException(int line, int col) {
    loc = new SourceLocation(CLI.getInputFilename(), line, col);
  }
  public CompilerException(int line, int col, String msg) {
    super(msg);
    loc = new SourceLocation(CLI.getInputFilename(), line, col);
  }

  @Override
  public String getMessage() {
    return super.getMessage() + " at " + loc;
  }

  public SourceLocation getLocation() {
    return loc;
  }
}
