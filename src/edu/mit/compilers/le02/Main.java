// Copyright (c) 2011 Liz Fong <lizfong@mit.edu>
// All rights reserved.

package edu.mit.compilers.le02;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import antlr.ASTFactory;
import antlr.CharStreamException;
import antlr.DumpASTVisitor;
import antlr.RecognitionException;
import antlr.Token;
import antlr.ANTLRException;
import antlr.TokenStreamRecognitionException;
import antlr.debug.misc.ASTFrame;

import edu.mit.compilers.le02.ast.ASTNode;
import edu.mit.compilers.le02.grammar.DecafParser;
import edu.mit.compilers.le02.grammar.DecafParserTokenTypes;
import edu.mit.compilers.le02.grammar.DecafScanner;
import edu.mit.compilers.le02.grammar.DecafScannerTokenTypes;
import edu.mit.compilers.le02.grammar.LineNumberedAST;
import edu.mit.compilers.le02.grammar.ScanException;
import edu.mit.compilers.le02.ir.IrException;
import edu.mit.compilers.le02.ir.IrGenerator;
import edu.mit.compilers.le02.stgenerator.SymbolTableException;
import edu.mit.compilers.le02.stgenerator.SymbolTableGenerator;
import edu.mit.compilers.le02.symboltable.SymbolTable;


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
    SEMANTICS_FAILED (3),
    FILE_NOT_FOUND (126),
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
        ErrorReporting.reportErrorCompat(e);
        System.exit(ReturnCode.FILE_NOT_FOUND.numericCode());
      }
    }

    switch (CLI.target) {
     case SCAN:
      if (!runScanner(inputStream)) {
        retCode = ReturnCode.SCAN_FAILED;
      }
      break;
     case PARSE:
      if (!runParser(inputStream)) {
        retCode = ReturnCode.PARSE_FAILED;
      }
      break;
     case INTER:
     case DEFAULT:
      if (!generateIR(inputStream)) {
        retCode = ReturnCode.SEMANTICS_FAILED;
      }
      break;
     default:
      retCode = ReturnCode.NO_SUCH_ACTION;
      ErrorReporting.reportErrorCompat(new NoSuchMethodException(
        "Action " + CLI.target + " not yet implemented."));
    }
    ErrorReporting.printErrors(System.err);
    System.exit(retCode.numericCode());
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
    if (!CLI.compat) { 
      scanner.setFilename(CLI.infile);
    }

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
        if (CLI.compat) {
          ErrorReporting.reportErrorCompat(e);
        } else {
          System.out.println(e);
          if (e instanceof TokenStreamRecognitionException) {
            ErrorReporting.reportError(
              new ScanException((TokenStreamRecognitionException)e));
          } else {
            ErrorReporting.reportError(new ScanException(e.getMessage()));
          }
        }
        try {
          scanner.consume();
        } catch (CharStreamException cse) {
          if (CLI.compat) {
            ErrorReporting.reportErrorCompat(cse);
          } else {
            ErrorReporting.reportError(new ScanException(cse.getMessage()));
          }
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
      final DecafParser parser = new DecafParser(parse_lexer);

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
        DumpASTVisitor dumper = new DumpASTVisitor();
        dumper.visit(parser.getAST());
        if (CLI.graphics) {
          Thread thread = new Thread() {
            @Override
            public void run() {
              ASTFrame frame = new ASTFrame(CLI.getInputFilename(),
                                            parser.getAST());
              frame.validate();
              frame.setVisible(true);
            }
          };

          thread.run();
          try {
            // TODO(lizfong): fix kludge.
            Thread.sleep(3 * 60 * 1000);
          } catch (InterruptedException ie) {
            // ignore
          }
        }
      }

      // If any errors were printed by the parser, note unsuccessful parse.
      if (parser.getError()) {
        success = false;
      }
    } catch (ANTLRException e) {
      ErrorReporting.reportErrorCompat(e);
      success = false;
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
  protected static boolean generateIR(InputStream inputStream) {
    boolean success = true;
    try {
      DecafScanner parse_lexer =
        new DecafScanner(new DataInputStream(inputStream));
      final DecafParser parser = new DecafParser(parse_lexer);

      // The default instantiation is unaware of underlying filenames when
      // pretty-printing exceptions. Set the values appropriately.
      if (inputStream instanceof FileInputStream) {
        parse_lexer.setFilename(CLI.infile);
        parser.setFilename(CLI.infile);
      }

      // Save the line/column numbers so we get meaningful parse data.
      ASTFactory factory = new ASTFactory();
      factory.setASTNodeClass(LineNumberedAST.class);
      parser.setASTFactory(factory);

      // Invoke the parser.
      parser.program();

      ASTNode parent = IrGenerator.generateIR(parser.getAST());
      SymbolTable st = SymbolTableGenerator.generateSymbolTable(parent);
      
      if (CLI.debug) {
        System.out.println(parent);
      }
    } catch (ANTLRException e) {
      ErrorReporting.reportErrorCompat(e);
      success = false;
    } catch (IrException ire) {
      // Don't use reportError since IrExceptions know the filename and
      // already know how to pretty-print, unlike antlr exceptions.
      System.out.println(ire);
      ire.printStackTrace(System.out);
      success = false;
    } catch (SymbolTableException ste) {
      System.out.println(ste);
      ste.printStackTrace(System.out);
      success = false;
    }
    return success;
  }

}
