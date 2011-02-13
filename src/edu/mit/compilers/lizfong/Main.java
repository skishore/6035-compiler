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

public class Main {
  public enum ReturnCode {
    SUCCESS (0),
    SCAN_FAILED (1),
    PARSE_FAILED (2),
    NO_SUCH_ACTION (255);

    private int numericCode;
    private ReturnCode (int code) {
      numericCode = code;
    }
    public int numericCode() {
      return numericCode;
    }
  };

  public static void main (String[] args) {
    ReturnCode retCode = ReturnCode.SUCCESS;
    try {
      CLI.parse(args, new String[0]);

      InputStream inputStream = (args.length == 0) ?
        System.in : new FileInputStream(CLI.infile);

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
        throw new NoSuchMethodException("Action " + CLI.target + " not yet implemented.");
      }
    }
    catch (IOException e) {
      // print the error:
      reportError(e);
    } catch (NoSuchMethodException nsme) {
      reportError(nsme);
    }
    System.exit(retCode.numericCode());
  }

  protected static void reportError (Exception e) {
    System.out.println(CLI.infile + " " + e);
  }

  protected static boolean runScanner (InputStream inputStream) {
    boolean success = true;
    DecafScanner lexer =
      new DecafScanner(new DataInputStream(inputStream));
    Token token;
    boolean done = false;
    while (!done) {
      try {
        for (token = lexer.nextToken();
             token.getType() != DecafParserTokenTypes.EOF;
             token = lexer.nextToken()) {
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
        // print the error and continue
        reportError(e);
        try {
          lexer.consume();
        } catch (CharStreamException cse) {
          reportError(cse);
        }
        success = false;
      }
    }
    return success;
  }

  protected static boolean runParser (InputStream inputStream) {
    boolean success = true;
    try {
      DecafScanner parse_lexer =
        new DecafScanner(new DataInputStream(inputStream));
      DecafParser parser = new DecafParser(parse_lexer);
      if (inputStream instanceof FileInputStream) {
        parse_lexer.setFilename(CLI.infile);
        parser.setFilename(CLI.infile);
      }
      parser.program();
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
