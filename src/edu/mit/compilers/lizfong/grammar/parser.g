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

program: TK_class ID LCURLY (field_decl)* (method_decl)* RCURLY EOF;
field_decl: type ((ID | ID LSQUARE INT RSQUARE) COMMA)* (ID | ID LSQUARE INT RSQUARE) SEMICOLON;
method_decl: (type | TK_void) ID LPAREN ((type ID COMMA)* type ID |) block;
block: LCURLY (var_decl)* (statement)* RCURLY;
var_decl: type (ID COMMA)* ID SEMICOLON;
type: (TK_int | TK_boolean);
statement: (
  location assign_op expr SEMICOLON |
  method_call SEMICOLON |
  TK_if LPAREN expr RPAREN block (TK_else block |) |
  TK_for ID ASSIGN expr COMMA expr block |
  TK_return (expr|) SEMICOLON |
  TK_break SEMICOLON |
  TK_continue SEMICOLON |
  block
);
assign_op: (ASSIGN | INC_ASSIGN | DEC_ASSIGN);
method_call: (
  method_name LPAREN ((ID COMMA)* ID |) RPAREN |
  TK_callout LPAREN STRING (COMMA (callout_arg COMMA)* callout_arg |)
);
method_name: ID;
location: (ID | ID LSQUARE expr RSQUARE);
expr: (location | method_call | literal | expr bin_op expr |
       MINUS expr | NOT expr | LPAREN expr RPAREN);
callout_arg: (expr | STRING);
bin_op: (arith_op | rel_op | eq_op | cond_op);
arith_op: (PLUS | MINUS | TIMES | DIVIDE | MODULO);
rel_op: (LT | GT | LE | GE);
eq_op: (EQ | NOT_EQ);
cond_op: (LOGICAL_AND | LOGICAL_OR);
literal: INT | CHAR | STRING;
