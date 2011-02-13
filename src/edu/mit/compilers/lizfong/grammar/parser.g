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
field_decl:
  type (ID | ID LSQUARE INT RSQUARE)
       (COMMA (ID | ID LSQUARE INT RSQUARE))* SEMICOLON;
method_decl:
  (type | TK_void) ID LPAREN (
    // Deliberately empty
    | type ID (COMMA type ID)*) RPAREN block;
block: LCURLY (var_decl)* (statement)* RCURLY;
var_decl: type ID (COMMA ID)* SEMICOLON;
type: (TK_int | TK_boolean);
statement: (
  location assign_op expr SEMICOLON |
  method_call SEMICOLON |
  TK_if LPAREN expr RPAREN block ( // Deliberately empty
                                   | TK_else block) |
  TK_for ID ASSIGN expr COMMA expr block |
  TK_return ( // Deliberately empty
              | expr) SEMICOLON |
  TK_break SEMICOLON |
  TK_continue SEMICOLON |
  block
);
assign_op: (ASSIGN | INC_ASSIGN | DEC_ASSIGN);
method_call: (
  method_name LPAREN ( // Deliberately empty
                       | expr (COMMA expr)*) RPAREN |
  TK_callout LPAREN STRING (COMMA callout_arg)* RPAREN
);
method_name: ID;
location: (ID | ID LSQUARE expr RSQUARE);

expr: term_zero expr_prime;
expr_prime: (LOGICAL_OR term_zero expr_prime |
             // Deliberately empty
            );
term_zero: term_one term_zero_prime;
term_zero_prime: (LOGICAL_AND term_one term_zero_prime |
                  // Deliberately empty
                 );
term_one: term_two term_one_prime;
term_one_prime: (EQUALS term_two term_one_prime |
                 NOT_EQUALS term_two term_one_prime |
                 // Deliberately empty
                );
term_two: term_three term_two_prime;
term_two_prime: (LT term_three term_two_prime |
                 GT term_three term_two_prime |
                 LE term_three term_two_prime |
                 GE term_three term_two_prime |
                 // Deliberately empty
                );
term_three: term_four term_three_prime;
term_three_prime: (PLUS term_four term_three_prime |
                   MINUS term_four term_three_prime|
                   // Deliberately empty
                  );
term_four: term_five term_four_prime;
term_four_prime: (TIMES term_five term_four_prime |
                  DIVIDE term_five term_four_prime |
                  MODULO term_five term_four_prime |
                  // Deliberately empty
                 );
term_five: (NOT term_five | term_six);
term_six: (MINUS term_six | term_final);
term_final: (location | method_call | literal | LPAREN expr RPAREN);

callout_arg: expr | STRING;
bin_op: (arith_op | rel_op | eq_op | cond_op);
arith_op: (PLUS | MINUS | TIMES | DIVIDE | MODULO);
rel_op: (LT | GT | LE | GE);
eq_op: (EQ | NOT_EQ);
cond_op: (LOGICAL_AND | LOGICAL_OR);
literal: INT | CHAR | BOOL;
