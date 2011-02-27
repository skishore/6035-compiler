package edu.mit.compilers.le02;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import edu.mit.compilers.tools.CLI;

public class ErrorReporting {

  /**
   * Utility method to immediately pretty-print an exception to stdout
   * along with the corresponding file name. No location information is
   * printed since generic Exceptions do not have attached SourceLocations.
   */
  public static void reportErrorCompat(Exception e) {
    System.out.println(CLI.getInputFilename() + " " + e);
  }

  /**
   * Reports a specific CompilerException to be stored in the list of
   * compilation errors and reported later.
   */
  public static void reportError(CompilerException ce) {
    errorList.add(ce);
  }

  /**
   * Prints all currently stored CompilerExceptions to the specified
   * PrintStream e.g. System.out or System.err.
   */
  public static void printErrors(PrintStream ps) {
    for (CompilerException ce : errorList) {
      ps.println(ce.getMessage());
      SourceLocation loc = ce.getLocation();

      if (loc.getLine() >= 0 && loc.getCol() >= 0 &&
          !loc.getFilename().equals(CLI.STDIN)) {
        try {
          BufferedReader reader = new BufferedReader(
            new FileReader(loc.getFilename()));
          int line = 0;
          String lineContents = "";
          while (line < loc.getLine()) {
            lineContents = reader.readLine();
            line++;
          }
          ps.println(lineContents);
          String marker = String.format("%1$#" + loc.getCol() + "s", "^");
          ps.println(marker);
        } catch (IOException e) {
          // Swallow the error and just bypass printing out debug information.
        }
      }
    }
  }

  /**
   * Returns whether there are no errors accumulated since ErrorReporting was
   * last cleared.
   */
  public static boolean noErrors() {
    return errorList.isEmpty();
  }

  /**
   * Clears the list of errors. Most useful for unit testing.
   */
  public static void clearErrors() {
    errorList.clear();
  }

  /** The list of all CompilerExceptions reported thus far. */
  private static List<CompilerException> errorList =
    new ArrayList<CompilerException>();
}
