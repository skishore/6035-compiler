// Copyright (c) 2011 Liz Fong <lizfong@mit.edu>
// All rights reserved.

package edu.mit.compilers.lizfong;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStream;

import antlr.Token;

import edu.mit.compilers.lizfong.grammar.DecafParser;
import edu.mit.compilers.lizfong.grammar.DecafParserTokenTypes;
import edu.mit.compilers.lizfong.grammar.DecafScanner;
import edu.mit.compilers.lizfong.grammar.DecafScannerTokenTypes;

import edu.mit.compilers.tools.CLI;
import edu.mit.compilers.tools.CLI.Action;

class Main {
  public static void main (String[] args) {
    int retCode = 0;
    try {
      CLI.parse(args, new String[0]);

      InputStream inputStream = args.length == 0 ?
        System.in : new FileInputStream(CLI.infile);

      switch (CLI.target) {
       case SCAN:
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
          }
          catch (Exception e) {
            // print the error and continue
            System.out.println(CLI.infile + " " + e);
            lexer.consume();
            retCode = 1;
          }
        }
        break;
       case PARSE:
       case DEFAULT:
        try {
          DecafScanner parse_lexer =
            new DecafScanner(new DataInputStream(inputStream));
          DecafParser parser = new DecafParser(parse_lexer);
          parser.program();
        } catch (Exception e) {
          retCode = 2;
        }
        break;
       default:
        retCode = 255;
        throw new Exception("Action " + CLI.target + " not yet implemented.");
      }
    }
    catch (Exception e) {
      // print the error:
      System.out.println(CLI.infile + " " + e);
    }
    System.exit(retCode);
  }
}
