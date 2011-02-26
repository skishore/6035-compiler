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
   * Utility method to pretty-print an exception along with the corresponding
   * file name.
   */
  public static void reportErrorCompat(Exception e) {
    System.out.println(CLI.getInputFilename() + " " + e);
  }

  public static void reportError(CompilerException ce) {
    errorList.add(ce);
  }

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

  public static boolean noErrors() {
    return errorList.isEmpty();
  }

  public static void clearErrors() {
    errorList.clear();
  }

  private static List<CompilerException> errorList =
    new ArrayList<CompilerException>();
}
