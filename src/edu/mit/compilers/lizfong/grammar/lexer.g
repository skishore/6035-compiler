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
}

LCURLY options { paraphrase = "{"; } : "{";
RCURLY options { paraphrase = "}"; } : "}";
LSQUARE options { paraphrase = "["; } : "[";
RSQUARE options { paraphrase = "]"; } : "]";
LPAREN options { paraphrase = "("; } : "(";
RPAREN options { paraphrase = ")"; } : ")";
EQUALS options { paraphrase = "=="; } : "==";
NOT_EQUALS options { paraphrase = "!="; } : "!=";
LOGICAL_AND options { paraphrase = "&&"; } : "&&";
LOGICAL_OR options { paraphrase = "||"; } : "||";
PLUS options { paraphrase = "+"; } : "+";
MINUS options { paraphrase = "-"; } : "-";
TIMES options { paraphrase = "*"; } : "*";
DIVIDE options { paraphrase = "/"; } : "/";
MODULO options { paraphrase = "%"; } : "%";
NOT options { paraphrase = "!"; } : "!";
LT options { paraphrase = "<"; } : "<";
GT options { paraphrase = ">"; } : ">";
LE options { paraphrase = "<="; } : "<=";
GE options { paraphrase = ">="; } : ">=";
ASSIGN options { paraphrase = "="; } : "=";
INC_ASSIGN options { paraphrase = "+="; } : "+=";
DEC_ASSIGN options { paraphrase = "-="; } : "-=";
SEMICOLON options { paraphrase = ";"; } : ";";
COMMA options { paraphrase = ","; } : ",";

ID options { paraphrase = "an identifier"; } : 
  ('a'..'z' | 'A'..'Z' | '_') ('a'..'z' | 'A'..'Z' | '0'..'9' | '_')*;

WS_ : (' ' | '\t' | '\n' {newline();}) {_ttype = Token.SKIP; };

SL_COMMENT : "//" (~'\n')* '\n' {_ttype = Token.SKIP; newline(); };

CHAR : '\'' (ESC|' '..'!'|'#'..'&'|'('..'['|']'..'~') '\'';
STRING : '"' (ESC|' '..'!'|'#'..'&'|'('..'['|']'..'~')* '"';
INT : (('0'..'9')+ | "0x" ('0'..'9' | 'a'..'f' | 'A'..'F')+);

protected
ESC :  '\\' ('n'|'"'|'t'|'\\'|'\''|
             ~('n'|'"'|'t'|'\\'|'\'') {if (true) throw new NoViableAltForCharException((char)LA(0), getFilename(), getLine(), getColumn() - 1);});
