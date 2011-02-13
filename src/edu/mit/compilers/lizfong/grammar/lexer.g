header {package edu.mit.compilers.lizfong.grammar;}

options 
{
  mangleLiteralPrefix = "TK_";
  language="Java";
}

class DecafScanner extends Lexer;
options 
{
  k = 2;
}

tokens 
{
  "boolean";
  "break";
  "callout";
  "class";
  "continue";
  "else";
  "false";
  "for";
  "if";
  "int";
  "return";
  "true";
  "void";
  "==";
  "!=";
  "&&";
  "||";
  "+";
  "-";
  "*";
  "/";
  "%";
  "<";
  ">";
  "<=";
  ">=";
  "=";
  "+=";
  "-=";
}

LCURLY options { paraphrase = "{"; } : "{";
RCURLY options { paraphrase = "}"; } : "}";
LSQUARE options { paraphrase = "["; } : "[";
RSQUARE options { paraphrase = "]"; } : "]";

ID options { paraphrase = "an identifier"; } : 
  ('a'..'z' | 'A'..'Z') ('a'..'z' | 'A'..'Z' | '0'..'9')*;

WS_ : (' ' | '\n' {newline();}) {_ttype = Token.SKIP; };

SL_COMMENT : "//" (~'\n')* '\n' {_ttype = Token.SKIP; newline(); };

CHAR : '\'' (ESC|~('\''|'\n')) '\'';
STRING : '"' (ESC|~('"'|'\n'))* '"';

protected
ESC :  '\\' ('n'|'"'|'t'|'\\');
