header {
package edu.mit.compilers.le02.grammar;
import edu.mit.compilers.le02.ErrorReporting;
}

options
{
  mangleLiteralPrefix = "TK_";
  language = "Java";
}

class DecafParser extends Parser;
options
{
  importVocab = DecafScanner;
  k = 3;
  buildAST = true;
}

tokens
{
  PROGRAM;
  PROGRAM_FIELDS;
  PROGRAM_METHODS;
  CALL;
  CALL_ARGS;
  CALL_ARG;
  DECLARATION_ARGS;
  DECLARATION_ARG;
  EXPR;
  TERM;
  TERM_PRIME;
  TERMF;
  CALL_STMT;
  BLOCK;
  BLOCK_VARS;
  BLOCK_STMTS;
  ASSIGNMENT;
  METHOD_DECL;
  LOCAL_VAR_DECL;
  FIELD_DECL;
  LOCATION;
  BOOLEAN_LITERAL;
  CHAR_LITERAL;
  INTEGER_LITERAL;
  STRING_LITERAL;
}


{
  // Do our own reporting of errors so the parser can return a non-zero status
  // if any errors are detected.
  /** Reports if any errors were reported during parse. */
  private boolean error;

  @Override
  public void reportError (RecognitionException ex) {
    ErrorReporting.reportError(new ParseException(ex));
    error = true;
  }
  @Override
  public void reportError (String s) {
    ErrorReporting.reportError(new ParseException(s));
    error = true;
  }
  public boolean getError () {
    return error;
  }

  // Selectively turns on debug mode.

  /** Whether to display debug information. */
  private boolean trace = false;

  public void setTrace(boolean shouldTrace) {
    trace = shouldTrace;
  }
  @Override
  public void traceIn(String rname) throws TokenStreamException {
    if (trace) {
      super.traceIn(rname);
    }
  }
  @Override
  public void traceOut(String rname) throws TokenStreamException {
    if (trace) {
      super.traceOut(rname);
    }
  }
}

// Apply ! to all syntactic sugar that exists only to make the parser's job
// easier but does not need to live in parse trees.
// Examples: SEMICOLON, [LR]SQUARE, [LR]PAREN
// Explicitly create the tree by using raw tree creation (e.g. #rule = [...])

//
// Primitives - must be flagged with appropriate type.
//
bool_literal!:
  t:TK_true
    { #bool_literal = #([BOOLEAN_LITERAL,"Bool"], t); } |
  f:TK_false
    { #bool_literal = #([BOOLEAN_LITERAL,"Bool"], f); };
int_literal!: i:INT
  { #int_literal = #([INTEGER_LITERAL,"Int"], i); };
char_literal!: c:CHAR
  { #char_literal = #([CHAR_LITERAL,"Char"], c); };
string_literal!: s:STRING
  { #string_literal = #([STRING_LITERAL,"String"], s); };

//
// Categories
//
literal: int_literal | char_literal | bool_literal;
type: TK_int | TK_boolean;
assign_op: ASSIGN | INC_ASSIGN | DEC_ASSIGN;

// Locations can contain just a variable, or variable plus index.
location!: n:ID ( // Deliberately empty
                 | LSQUARE! i:expr RSQUARE!)
  { #location = #([LOCATION,"Location"], n, i); };

//
// Declarations
//

// A program is a list of Field declarations followed by a list of Method
// declarations.
program!:
  {
    AST f_accum = #([PROGRAM_FIELDS,"Fields"]);
    AST m_accum = #([PROGRAM_METHODS,"Methods"]);
  }
  TK_class! n:ID LCURLY! (f:field_decl {f_accum.addChild(#f);})*
                         (m:method_decl {m_accum.addChild(#m);})*
                         RCURLY! EOF!
  { #program = #([PROGRAM,"Prog"], n, f_accum, m_accum); };

// A field declaration can be compound, but we can use magic to expand on the
// spot. We have to be careful about cloning t since we reuse it repeatedly.
field_decl!:
  t:type (n1:ID ( // Deliberately empty
                 | LSQUARE! s1:int_literal RSQUARE!)
                 {AST t_copy = astFactory.create(#t);
                  astFactory.addASTChild(currentAST,
                        #([FIELD_DECL,"FieldDecl"], t_copy, n1, s1));}
                )
       (COMMA! (n2:ID ( // Deliberately empty
                      | LSQUARE! s2:int_literal RSQUARE!)
                      {AST t_copy = astFactory.create(#t);
                      astFactory.addASTChild(currentAST,
                        #([FIELD_DECL,"FieldDecl"], t_copy, n2, s2));})
                     )* SEMICOLON!
  { #field_decl = (AST)currentAST.root; };

// Method declarations consist of a type, an identifier, a list of arguments,
// and a body block. The list of Args contains individual Args with both type
// and name.
method_decl!:
  { AST darg_accum = #([DECLARATION_ARGS,"Args"]); }
  (ty:type | vo:TK_void) name:method_name LPAREN! (
    // Deliberately empty
    | arg1t:type arg1n:ID
      {darg_accum.addChild(#([DECLARATION_ARG,"Arg"], arg1t, arg1n));}
      (COMMA! arg2t:type arg2n:ID
        {darg_accum.addChild(#([DECLARATION_ARG,"Arg"], arg2t, arg2n));})*)
    RPAREN! body:block
  { #method_decl = #([METHOD_DECL,"MethodDecl"],
                     ty, vo, name, darg_accum, body); };

// A local variable declaration can be compound, but we can use magic to
// expand on the spot. We have to be careful about cloning t since we reuse
// it repeatedly.
var_decl!:
  t:type n1:ID {AST t_copy = astFactory.create(#t);
                astFactory.addASTChild(currentAST,
                            #([LOCAL_VAR_DECL,"VarDecl"], t_copy, n1));}
           (COMMA! n2:ID {AST t_copy2 = astFactory.create(#t);
                          astFactory.addASTChild(currentAST,
                            #([LOCAL_VAR_DECL,"VarDecl"], t_copy2, n2));}
           )* SEMICOLON!
  { #var_decl = (AST)currentAST.root; };

//
// Control flows.
//

// A block contains a list of variables followed by a list of statements.
// We proceed similarly to program by using accumulators.
block!:
  {
    AST bv_accum = #([BLOCK_VARS,"Variables"]);
    AST bs_accum = #([BLOCK_STMTS,"Statements"]);
  }
  LCURLY! (v:var_decl {bv_accum.addChild(#v);})*
          (s:statement {bs_accum.addChild(#s);})* RCURLY!
  { #block = #([BLOCK,"Block"], bv_accum, bs_accum); };

// An assignment consists of a location, operation, and value.
assignment!: loc:location op:assign_op value:expr
  { #assignment = #([ASSIGNMENT,"Assignment"], loc, op, value); };

// An if statement contains the condition, the block to execute if true, and
// optionally the block to execute if false.
if_stmt!: i:TK_if LPAREN! cond:expr RPAREN! b_true:block ( // Deliberately empty
                                           | TK_else! b_false:block)
  { #if_stmt = #(i, cond, b_true, b_false); };

// A for loop contains an initial assignment with a var and an init value,
// a maximum value, and a block to loop over.
for_loop!: f:TK_for var:ID ASSIGN init:expr COMMA! max:expr loop:block
  { #for_loop = #(f,
      #([ASSIGNMENT,"Assignment"],
        #([LOCATION,"Location"], var), [ASSIGN], init),
      max, loop); };

// A statement is an assignment, method call, if block, for block,
// return (containing the value to return), break, continue, and any block.
statement:
  assignment SEMICOLON! |!
  m:method_call SEMICOLON!
    { #statement = #([CALL_STMT, "CallStatement"], m); } |
  if_stmt |
  for_loop |!
  r:TK_return ( // Deliberately empty
              | v:expr) SEMICOLON!
    {#statement = #(r, v);} |
  TK_break SEMICOLON! |
  TK_continue SEMICOLON! |
  block
;

//
// Method calls.
//
method_name: ID;
callout_arg: expr | string_literal;

// A method call is either a callout or a direct call.
// A callout contains the callout function's name, and a list of arguments.
// A direct call contains a method name and a list of arguments.
method_call!:
  { AST carg_accum = #([CALL_ARGS,"Args"]); }
  (c:TK_callout LPAREN! cf:string_literal (COMMA! ca:callout_arg
       {carg_accum.addChild(#([CALL_ARG,"Arg"], ca));})* RPAREN!
       { #method_call = #(c, cf, carg_accum); } |
  m:method_name LPAREN! ( // Deliberately empty
                          | (a1:expr
                             {carg_accum.addChild(#([CALL_ARG,"Arg"], a1));}
                          (COMMA! a2:expr
                             {carg_accum.addChild(#([CALL_ARG,"Arg"], a2));}
                        )*)) RPAREN!
    { #method_call = #([CALL,"Call"], m, carg_accum); })
;

//
// Expressions
//

// Rewriting the grammar for expression evaluation to not cascade left,
// and simultaneously ensuring order of operations is observed.

// Tier -1: ||
expr!: zero:term_zero prime:expr_prime
 { #expr = #([EXPR,"Expr"], zero, prime); };
expr_prime: (LOGICAL_OR term_zero expr_prime |
             // Deliberately empty
            )
 { #expr_prime = #([TERM_PRIME,"Term'"], #expr_prime); };
// Tier 0: &&
term_zero!: one:term_one prime:term_zero_prime
  { #term_zero = #([TERM,"Term"], one, prime); };
term_zero_prime: (LOGICAL_AND term_one term_zero_prime |
                  // Deliberately empty
                 )
  { #term_zero_prime = #([TERM_PRIME,"Term'"], #term_zero_prime); };
// Tier 1: ==, !=
term_one!: two:term_two prime:term_one_prime
  { #term_one = #([TERM,"Term"], two, prime); };
term_one_prime: (EQUALS term_two term_one_prime |
                 NOT_EQUALS term_two term_one_prime |
                 // Deliberately empty
                )
  { #term_one_prime = #([TERM_PRIME,"Term'"], #term_one_prime); };
// Tier 2: <, >, <=, >=
term_two!: three:term_three prime:term_two_prime
  { #term_two = #([TERM,"Term"], three, prime); };
term_two_prime: (LT term_three term_two_prime |
                 GT term_three term_two_prime |
                 LE term_three term_two_prime |
                 GE term_three term_two_prime |
                 // Deliberately empty
                )
  { #term_two_prime = #([TERM_PRIME,"Term'"], #term_two_prime); };
// Tier 3: +, -
term_three!: four:term_four prime:term_three_prime
  { #term_three = #([TERM,"Term"], four, prime); };
term_three_prime: (PLUS term_four term_three_prime |
                   MINUS term_four term_three_prime|
                   // Deliberately empty
                  )
  { #term_three_prime = #([TERM_PRIME,"Term'"], #term_three_prime); };
// Tier 4: *, /, %
term_four!: five:term_five prime:term_four_prime
  { #term_four = #([TERM,"Term"], five, prime); };
term_four_prime: (TIMES term_five term_four_prime |
                  DIVIDE term_five term_four_prime |
                  MODULO term_five term_four_prime |
                  // Deliberately empty
                 )
  { #term_four_prime = #([TERM_PRIME,"Term'"], #term_four_prime); };
// Tier 5: !
term_five!: t6:term_six { #term_five = #([TERMF,"TermF"], t6); } |
            n:NOT t5:term_five
              { #term_five = #([TERMF,"TermF"], n,
                  #([TERMF,"TermF"], t5)); };
// Tier 6: urnary -
term_six: term_final |! m:MINUS t:term_six
                        { #term_six = #([TERMF,"TermF"], m,
                            #([TERMF,"TermF"], t)); };
// Tier 7: base expressions and parenthesized subexpressions.
term_final: location | method_call | literal | LPAREN! expr RPAREN!;
