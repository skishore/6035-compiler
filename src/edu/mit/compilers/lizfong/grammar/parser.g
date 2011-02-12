header {package edu.mit.compilers.lizfong.grammar;}

options
{
  mangleLiteralPrefix = "TK_";
  language="Java";
}

class DecafParser extends Parser;
options
{
  importVocab=DecafScanner;
  k=3;
  buildAST=true;
}

program: TK_class ID LCURLY RCURLY EOF;
