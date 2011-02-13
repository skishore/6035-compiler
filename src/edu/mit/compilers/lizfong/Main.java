// Copyright (c) 2011 Liz Fong <lizfong@mit.edu>
// All rights reserved.

package edu.mit.compilers.lizfong;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import antlr.CharStreamException;
import antlr.Token;
import antlr.ANTLRException;

import edu.mit.compilers.lizfong.grammar.DecafParser;
import edu.mit.compilers.lizfong.grammar.DecafParserTokenTypes;
import edu.mit.compilers.lizfong.grammar.DecafScanner;
import edu.mit.compilers.lizfong.grammar.DecafScannerTokenTypes;

import edu.mit.compilers.tools.CLI;

/**
 * Main class used to invoke subcomponents of the compiler.
 * @author Liz Fong <lizfong@mit.edu>
 */
public class Main {
  /** Enumerates all valid return codes. */
  public enum ReturnCode {
    SUCCESS (0),
    SCAN_FAILED (1),
    PARSE_FAILED (2),
    NO_SUCH_ACTION (127);

    private int numericCode;
    private ReturnCode (int code) {
      numericCode = code;
    }
    public int numericCode() {
      return numericCode;
    }
  };

  public enum Optimization {
    SAMPLE_OPT ("sample");
    private String flagName;
    private Optimization(String flag) {
      flagName = flag;
    }
    public String flagName() {
      return flagName;
    }
  }

  /**
   * Main entry point for compiler.
   */
  public static void main (String[] args) {
    // We should exit successfully unless something goes awry.
    ReturnCode retCode = ReturnCode.SUCCESS;
    // Default to reading from stdin unless we get a valid file input.
    InputStream inputStream = System.in;

    // Prepare the list of all optimization flags for the CLI utility. 
    String[] optimizations = new String[Optimization.values().length];
    int ii = 0;
    for (Optimization opt : Optimization.values()) {
      optimizations[ii] = opt.flagName();
    }

    CLI.parse(args, optimizations);

    // If we have a valid file input, set up the input stream.
    if (CLI.infile != null) {
      try {
        inputStream = new FileInputStream(CLI.infile);
      } catch (IOException e) {
        // print the error:
        reportError(e);
      }
    }

    switch (CLI.target) {
     case SCAN:
      if (!runScanner(inputStream)) {
        retCode = ReturnCode.SCAN_FAILED;
      }
      break;
     case PARSE:
     case DEFAULT:
      if (!runParser(inputStream)) {
        retCode = ReturnCode.PARSE_FAILED;
      }
      break;
     default:
      retCode = ReturnCode.NO_SUCH_ACTION;
      reportError(new NoSuchMethodException(
        "Action " + CLI.target + " not yet implemented."));
    }
    System.exit(retCode.numericCode());
  }

  /**
   * Utility method to pretty-print an exception along with the corresponding
   * file name.
   */
  protected static void reportError (Exception e) {
    String prefix = "<stdin> ";
    if (CLI.infile != null) {
      prefix = CLI.infile + " ";
    }
    System.out.println(prefix + e);
  }

  /**
   * Runs the scanner on an input and displays all tokens successfully
   * parsed from the input, along with any error messages.
   *
   * @param inputStream The stream to read input from.
   * @return true if scanner ran without errors, false if errors found.
   */
  protected static boolean runScanner (InputStream inputStream) {
    boolean success = true;
    DecafScanner scanner = new DecafScanner(new DataInputStream(inputStream));

    // If debug mode is set, enable tracing in the scanner.
    scanner.setTrace(CLI.debug);
    
    Token token;
    boolean done = false;
    while (!done) {
      try {
        for (token = scanner.nextToken();
             token.getType() != DecafParserTokenTypes.EOF;
             token = scanner.nextToken()) {
          String type = "";
          String text = token.getText();

          switch (token.getType()) {
           case DecafScannerTokenTypes.ID:
            type = " IDENTIFIER";
            break;
           case DecafScannerTokenTypes.CHAR:
            type = " CHARLITERAL";
           break;
           case DecafScannerTokenTypes.STRING:
            type = " STRINGLITERAL";
            break;
           case DecafScannerTokenTypes.INT:
            type = " INTLITERAL";
            break;
           case DecafScannerTokenTypes.TK_false:
           case DecafScannerTokenTypes.TK_true:
            type = " BOOLEANLITERAL";
            break;
          }
          System.out.println(token.getLine() + type + " " + text);
        }
        done = true;
      } catch (ANTLRException e) {
        // Print the error and continue by discarding the invalid token.
        // We hope that this gets us onto the right track again.
        reportError(e);
        try {
          scanner.consume();
        } catch (CharStreamException cse) {
          reportError(cse);
        }
        success = false;
      }
    }
    return success;
  }

  /**
   * Runs the parser on an input and displays any error messages found while
   * parsing.
   *
   * @param inputStream The stream to read input from.
   * @return true if parser ran without errors, false if errors found.
   */
  protected static boolean runParser (InputStream inputStream) {
    boolean success = true;
    try {
      DecafScanner parse_lexer =
        new DecafScanner(new DataInputStream(inputStream));
      DecafParser parser = new DecafParser(parse_lexer);

      // The default instantiation is unaware of underlying filenames when
      // pretty-printing exceptions. Set the values appropriately.
      if (inputStream instanceof FileInputStream) {
        parse_lexer.setFilename(CLI.infile);
        parser.setFilename(CLI.infile);
      }
      // If debug mode is set, enable tracing in the parser.
      parser.setTrace(CLI.debug);

      // Invoke the parser.
      parser.program();

      // If we are in debug mode, output the AST so it can be inspected.
      if (CLI.debug) {
        System.out.println(parser.getAST().toStringList());
      }

      // If any errors were printed by the parser, note unsuccessful parse.
      if (parser.getError()) {
        success = false;
      }
    } catch (ANTLRException e) {
      reportError(e);
      success = false;
    }
    return success;
  }
}
