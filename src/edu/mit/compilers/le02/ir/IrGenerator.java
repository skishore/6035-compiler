package edu.mit.compilers.le02.ir;

import java.util.List;

import edu.mit.compilers.le02.DecafType;
import edu.mit.compilers.le02.ErrorReporting;
import edu.mit.compilers.le02.SourceLocation;
import edu.mit.compilers.le02.ast.ArrayDeclNode;
import edu.mit.compilers.le02.ast.ArrayLocationNode;
import edu.mit.compilers.le02.ast.AssignNode;
import edu.mit.compilers.le02.ast.BinaryOpNode;
import edu.mit.compilers.le02.ast.BlockNode;
import edu.mit.compilers.le02.ast.BoolOpNode;
import edu.mit.compilers.le02.ast.BooleanNode;
import edu.mit.compilers.le02.ast.BreakNode;
import edu.mit.compilers.le02.ast.CallNode;
import edu.mit.compilers.le02.ast.CallStatementNode;
import edu.mit.compilers.le02.ast.CharNode;
import edu.mit.compilers.le02.ast.ClassNode;
import edu.mit.compilers.le02.ast.ContinueNode;
import edu.mit.compilers.le02.ast.ExpressionNode;
import edu.mit.compilers.le02.ast.FieldDeclNode;
import edu.mit.compilers.le02.ast.ForNode;
import edu.mit.compilers.le02.ast.IfNode;
import edu.mit.compilers.le02.ast.IntNode;
import edu.mit.compilers.le02.ast.LocationNode;
import edu.mit.compilers.le02.ast.MathOpNode;
import edu.mit.compilers.le02.ast.MethodCallNode;
import edu.mit.compilers.le02.ast.MethodDeclNode;
import edu.mit.compilers.le02.ast.MinusNode;
import edu.mit.compilers.le02.ast.NodeList;
import edu.mit.compilers.le02.ast.NotNode;
import edu.mit.compilers.le02.ast.ReturnNode;
import edu.mit.compilers.le02.ast.ScalarLocationNode;
import edu.mit.compilers.le02.ast.StatementNode;
import edu.mit.compilers.le02.ast.StringNode;
import edu.mit.compilers.le02.ast.SyscallArgNode;
import edu.mit.compilers.le02.ast.SystemCallNode;
import edu.mit.compilers.le02.ast.VarDeclNode;
import edu.mit.compilers.le02.ast.VariableNode;
import edu.mit.compilers.le02.grammar.DecafParserTokenTypes;
import edu.mit.compilers.le02.ast.ASTNode;
import edu.mit.compilers.le02.ast.BoolOpNode.BoolOp;
import edu.mit.compilers.le02.ast.MathOpNode.MathOp;
import edu.mit.compilers.tools.CLI;
import antlr.collections.AST;

/**
 * Populates an {@link ASTNode} structure from an antlr AST tree.
 * @author Liz Fong <lizfong@mit.edu>
 */
public class IrGenerator {

  /** Holds the IRGenerator singleton. */
  private static IrGenerator instance;

  /**
   * Retrieves the IRGenerator singleton, creating if necessary.
   */
  public static IrGenerator getInstance() {
    if (instance == null) {
      instance = new IrGenerator();
    }
    return instance;
  }

  /**
   * Generates an intermediate representation based on an input AST.
   */
  public static ASTNode generateIR(AST rootAst) {
    ASTNode rootNode = getInstance().visit(rootAst);
    new IntRangeChecker().visit(rootNode);
    return rootNode;
  }

  /**
   * Converts a single node into our type structure.
   * @param node The raw antlr AST node.
   * @return ast_node The parsed ASTNode.
   */
  public ASTNode visit(AST node) {
    if (node == null) {
      ErrorReporting.reportError(
        new IrException(SourceLocation.getSourceLocationWithoutDetails(),
          "Attempted to visit null node. Check if parse tree is malformed."));
      return new BooleanNode(
        SourceLocation.getSourceLocationWithoutDetails(), false);
    }

    final SourceLocation sl = new SourceLocation(node);
    if (CLI.debug) {
      System.out.println("" + sl + " ["+ node.getType() + "]: " +
                         node.getText());
    }

    switch (node.getType()) {
     case DecafParserTokenTypes.PROGRAM:
      return processProgram(node, sl);

     case DecafParserTokenTypes.PROGRAM_FIELDS:
      // #([PROGRAM_FIELDS,"Fields"], f1, f2, ...)
      NodeList<FieldDeclNode> fdlist = convertToList(node);
      return fdlist;

     case DecafParserTokenTypes.PROGRAM_METHODS:
      // #([PROGRAM_METHODS,"Methods"], m1, m2, ...)
      NodeList<MethodDeclNode> mdlist = convertToList(node);
      return mdlist;

     case DecafParserTokenTypes.TK_callout:
      return processSyscall(node, sl);

     case DecafParserTokenTypes.CALL:
      return processMethodCall(node, sl);

     case DecafParserTokenTypes.CALL_ARGS:
       // #([CALL_ARGS,"Args"], a1, a2, ...)
       NodeList<SyscallArgNode> calist = convertToList(node);
       return calist;

     case DecafParserTokenTypes.CALL_ARG:
      return processSyscallArg(node, sl);

     case DecafParserTokenTypes.DECLARATION_ARGS:
      // #([DECLARATION_ARGS,"Args"], a1, a2, ...)
      NodeList<VarDeclNode> dalist = convertToList(node);
      return dalist;

     case DecafParserTokenTypes.EXPR:
     case DecafParserTokenTypes.TERM:
      return processTerm(node, sl);

     case DecafParserTokenTypes.TERM_PRIME:
      return processTermPrime(node, sl);

     case DecafParserTokenTypes.TERMF:
      return processTermF(node, sl);

     // We need to account for each case of statement separately.
     // statement:
     //  assignment SEMICOLON! |
     case DecafParserTokenTypes.ASSIGNMENT:
      return processAssignment(node, sl);

     //  method_call SEMICOLON! |
     case DecafParserTokenTypes.CALL_STMT:
      return new CallStatementNode(sl,
        (CallNode)visit(node.getFirstChild()));

     //  if_stmt |
     case DecafParserTokenTypes.TK_if:
      // #if_stmt = #([TK_if], cond, b_true, b_false)
      AST cond_ast = node.getFirstChild();
      AST then_ast = cond_ast.getNextSibling();
      AST else_ast = then_ast.getNextSibling();
      ExpressionNode condition = (ExpressionNode)visit(cond_ast);
      BlockNode then = (BlockNode)visit(then_ast);

      if (else_ast != null) {
        BlockNode elseBlock = (BlockNode)visit(else_ast);
        return new IfNode(sl, condition, then, elseBlock);
      } else {
        return new IfNode(sl, condition, then);
      }

     //  for_loop |!
     case DecafParserTokenTypes.TK_for:
      // #for_loop = #([TK_for],
      //   #([ASSIGNMENT,"Assignment"], var, [ASSIGN], init),
      //   max, loop)
      AST for_assignment = node.getFirstChild();
      AST for_max = for_assignment.getNextSibling();
      AST for_loop = for_max.getNextSibling();
      AssignNode init = (AssignNode)visit(for_assignment);
      ExpressionNode end = (ExpressionNode)visit(for_max);
      BlockNode body = (BlockNode)visit(for_loop);
      return new ForNode(sl, init, end, body);

     //  TK_return ( // Deliberately empty
     //             | r:expr) SEMICOLON!
     //   {#statement = #([TK_return], r);} |
     case DecafParserTokenTypes.TK_return:
      if (node.getFirstChild() != null) {
       return new ReturnNode(sl,
         (ExpressionNode)visit(node.getFirstChild()));
      } else {
       return new ReturnNode(sl);
      }

     //  TK_break SEMICOLON! |
     case DecafParserTokenTypes.TK_break:
      return new BreakNode(sl);

     //  TK_continue SEMICOLON! |
     case DecafParserTokenTypes.TK_continue:
      return new ContinueNode(sl);

     //  block
     case DecafParserTokenTypes.BLOCK:
      // #([BLOCK,"Block"], bv_accum, bs_accum)
      AST block_vars = node.getFirstChild();
      AST block_stmts = block_vars.getNextSibling();
      @SuppressWarnings("unchecked")
      List<VarDeclNode> decls = (List<VarDeclNode>)visit(block_vars);
      @SuppressWarnings("unchecked")
      List<StatementNode> statements = (List<StatementNode>)visit(block_stmts);
      return new BlockNode(sl, decls, statements);

     case DecafParserTokenTypes.BLOCK_VARS:
      // #([BLOCK_VARS,"Variables"], v1, v2, ...)
      NodeList<VarDeclNode> bvlist = convertToList(node);
      return bvlist;

     case DecafParserTokenTypes.BLOCK_STMTS:
      // #([BLOCK_STMTS,"Statements"], s1, s1, ...)
      NodeList<StatementNode> bslist = convertToList(node);
      return bslist;

     case DecafParserTokenTypes.METHOD_DECL:
      return processMethodDecl(node, sl);

     case DecafParserTokenTypes.LOCAL_VAR_DECL:
     case DecafParserTokenTypes.DECLARATION_ARG:
      return processArgOrLocal(node, sl);

     case DecafParserTokenTypes.FIELD_DECL:
      return processFieldDec(node, sl);

     case DecafParserTokenTypes.LOCATION:
      return processLocation(node, sl);

     case DecafParserTokenTypes.BOOLEAN_LITERAL:
      return new BooleanNode(sl,
        node.getFirstChild().getType() == DecafParserTokenTypes.TK_true);

     case DecafParserTokenTypes.CHAR_LITERAL:
      return new CharNode(sl, node.getFirstChild().getText().charAt(0));

     case DecafParserTokenTypes.INTEGER_LITERAL:
      return processInt(node, sl);

     case DecafParserTokenTypes.STRING_LITERAL:
      return new StringNode(sl, node.getFirstChild().getText());

     default:
      ErrorReporting.reportError(
        new IrException(sl, "Unexpected token: " + node.getText()));
      return new BooleanNode(sl, false);
    }
  }

  /**
   * Processes a {@link DecafParserTokenTypes.PROGRAM} token.
   */
  protected ClassNode processProgram(AST node, SourceLocation sl) {
    // #([PROGRAM,"Prog"], n, f_accum, m_accum);
    AST name_ast = node.getFirstChild();
    AST fields_ast = name_ast.getNextSibling();
    AST methods_ast = fields_ast.getNextSibling();

    String name = name_ast.getText();
    @SuppressWarnings("unchecked")
    List<FieldDeclNode> fields =
      (List<FieldDeclNode>)visit(fields_ast);
    @SuppressWarnings("unchecked")
    List<MethodDeclNode> methods =
      (List<MethodDeclNode>)visit(methods_ast);
    return new ClassNode(sl, name, fields, methods);
  }

  /**
   * Processes an {@link DecafParserTokenTypes.TK_callout} token.
   */
  protected SystemCallNode processSyscall(AST node, SourceLocation sl) {
    // #([TK_callout], cf, carg_accum);
    AST callout_target_ast = node.getFirstChild();
    AST callout_args_ast = callout_target_ast.getNextSibling();
    StringNode callout_target = (StringNode)visit(callout_target_ast);
    @SuppressWarnings("unchecked")
    List<SyscallArgNode> callout_args =
      (List<SyscallArgNode>)visit(callout_args_ast);
    return new SystemCallNode(sl, callout_target, callout_args);
  }

  /**
   * Processes an {@link DecafParserTokenTypes.CALL_ARG} token.
   */
  protected SyscallArgNode processSyscallArg(AST node, SourceLocation sl) {
    AST call_arg = node.getFirstChild();
    SourceLocation call_arg_loc = new SourceLocation(call_arg);
    switch (call_arg.getType()) {
     case DecafParserTokenTypes.EXPR:
      return new SyscallArgNode(call_arg_loc,
                                 (ExpressionNode)visit(call_arg));
     case DecafParserTokenTypes.STRING_LITERAL:
      return new SyscallArgNode(call_arg_loc,
                                 (StringNode)visit(call_arg));
     default:
       ErrorReporting.reportError(
         new IrException(call_arg_loc, "Invalid syscall argument type"));
       return new SyscallArgNode(call_arg_loc,
         new StringNode(call_arg_loc, "<bad>"));
    }
  }

  /**
   * Processes an {@link DecafParserTokenTypes.CALL} token.
   */
  protected MethodCallNode processMethodCall(AST node, SourceLocation sl) {
    // #([CALL,"Call"], f, carg_accum);
    AST call_target_ast = node.getFirstChild();
    AST call_args_ast = call_target_ast.getNextSibling();
    String call_method = call_target_ast.getText();
    @SuppressWarnings("unchecked")
    List<ExpressionNode> call_args =
      (List<ExpressionNode>)visit(call_args_ast);
    return new MethodCallNode(sl, call_method, call_args);
  }

  /**
   * Processes an {@link DecafParserTokenTypes.EXPR} or a
   * {@link DecafParserTokenTypes.TERM} token.
   *
   * All Expr or Term nodes consist of a left node containing either
   * another Term node or a TermF node, and a right node consisting of
   * a (possibly empty) Term' node (thus either a BinaryOpNode or null).
   * We collect both halves, and then composite them together, inserting
   * the left node into the empty space reserved for it in the right node.
   * If the right node is null, composite simply returns the left node.
   */
  protected ExpressionNode processTerm(AST node, SourceLocation sl) {
    AST termChild = node.getFirstChild();
    ExpressionNode left = (ExpressionNode)visit(termChild);
    BinaryOpNode right = (BinaryOpNode)visit(termChild.getNextSibling());
    return composite(left, right);
  }

  /**
   * Processes an {@link DecafParserTokenTypes.TERMF} token.
   *
   * All TermF nodes contain either other TermF nodes, a literal, an unary
   * expression containing another TermF node, or a parenthesized Expr node.
   *
   * We evaluate the unary expression if present, and otherwise just return
   * the recursively evaluated base literal contained within.
   */
  protected ExpressionNode processTermF(AST node, SourceLocation sl) {
    AST termChild = node.getFirstChild();
    switch (termChild.getType()) {
     case DecafParserTokenTypes.MINUS:
      return processMinus(termChild.getNextSibling(), sl);
     case DecafParserTokenTypes.NOT:
      return new NotNode(sl,
        (ExpressionNode)visit(termChild.getNextSibling()));
     case DecafParserTokenTypes.LOCATION:
      return new VariableNode(new SourceLocation(termChild),
        (LocationNode)visit(termChild));
     case DecafParserTokenTypes.CALL:
     case DecafParserTokenTypes.TK_callout:
     case DecafParserTokenTypes.INTEGER_LITERAL:
     case DecafParserTokenTypes.CHAR_LITERAL:
     case DecafParserTokenTypes.BOOLEAN_LITERAL:
     case DecafParserTokenTypes.EXPR:
     case DecafParserTokenTypes.TERMF:
       // Use general parsing routines.
       return (ExpressionNode)visit(termChild);
     default:
      ErrorReporting.reportError(new IrException(new SourceLocation(termChild),
        "Unknown expression terminal " + termChild.getType()));
      return new BooleanNode(sl, false);
    }
  }

  /**
   * Processes a {@link DecafParserTokenTypes.TERM_PRIME} token.
   *
   * A Term' node either:
   * Is empty, in which case we propagate null up the transform tree, or
   * Contains the operator of a binary operation, the right operand of the
   * binary operation, and possibly another chained Term' node following the
   * right operand.
   *
   * We construct the BinaryOpNode, leaving the left operand blank, evaluate
   * the right operand and place it in the BinaryOpNode, and possibly
   * composite the BinaryOpNode with the chained Term' node to its right.
   */
  protected ExpressionNode processTermPrime(AST node, SourceLocation sl) {
    AST primeChild = node.getFirstChild();
    if (primeChild == null) {
      return null;
    }
    AST rightTerm = primeChild.getNextSibling();
    AST nextPrime = rightTerm.getNextSibling();
    ExpressionNode rightExpr = (ExpressionNode)visit(rightTerm);
    ExpressionNode nextExpr = (ExpressionNode)visit(nextPrime);
    BinaryOpNode left;
    switch (primeChild.getType()) {
     case DecafParserTokenTypes.LOGICAL_OR:
      left = new BoolOpNode(sl, null, rightExpr, BoolOp.OR);
      break;
     case DecafParserTokenTypes.LOGICAL_AND:
      left = new BoolOpNode(sl, null, rightExpr, BoolOp.AND);
      break;
     case DecafParserTokenTypes.EQUALS:
      left = new BoolOpNode(sl, null, rightExpr, BoolOp.EQ);
      break;
     case DecafParserTokenTypes.NOT_EQUALS:
      left = new BoolOpNode(sl, null, rightExpr, BoolOp.NEQ);
      break;
     case DecafParserTokenTypes.LT:
      left = new BoolOpNode(sl, null, rightExpr, BoolOp.LT);
      break;
     case DecafParserTokenTypes.GT:
      left = new BoolOpNode(sl, null, rightExpr, BoolOp.GT);
      break;
     case DecafParserTokenTypes.LE:
      left = new BoolOpNode(sl, null, rightExpr, BoolOp.LE);
      break;
     case DecafParserTokenTypes.GE:
      left = new BoolOpNode(sl, null, rightExpr, BoolOp.GE);
      break;
     case DecafParserTokenTypes.PLUS:
      left = new MathOpNode(sl, null, rightExpr, MathOp.ADD);
      break;
     case DecafParserTokenTypes.MINUS:
      left = new MathOpNode(sl, null, rightExpr, MathOp.SUBTRACT);
      break;
     case DecafParserTokenTypes.TIMES:
      left = new MathOpNode(sl, null, rightExpr, MathOp.MULTIPLY);
      break;
     case DecafParserTokenTypes.DIVIDE:
      left = new MathOpNode(sl, null, rightExpr, MathOp.DIVIDE);
      break;
     case DecafParserTokenTypes.MODULO:
      left = new MathOpNode(sl, null, rightExpr, MathOp.MODULO);
      break;
     default:
      ErrorReporting.reportError(
        new IrException(new SourceLocation(primeChild),
          "Unknown binary operation " + primeChild.getText()));
      return new BooleanNode(sl, false);
    }
    if (nextExpr != null && nextExpr instanceof BinaryOpNode) {
      BinaryOpNode right = (BinaryOpNode)nextExpr;
      return composite(left, right);
    } else {
      return left;
    }
  }

  /**
   * Processes a {@link DecafParserTokenTypes.ASSIGNMENT} token.
   */
  protected AssignNode processAssignment(AST node, SourceLocation sl) {
    // #assignment = #([ASSIGNMENT,"Assignment"], loc, op, value)
    AST loc_ast = node.getFirstChild();
    AST op_ast = loc_ast.getNextSibling();
    AST value_ast = op_ast.getNextSibling();

    LocationNode loc = (LocationNode)visit(loc_ast);
    VariableNode loc_var = new VariableNode(
      new SourceLocation(loc_ast), loc);
    ExpressionNode value = (ExpressionNode)visit(value_ast);
    SourceLocation op_ast_location = new SourceLocation(op_ast);

    switch (op_ast.getType()) {
     case DecafParserTokenTypes.ASSIGN:
      return new AssignNode(sl, loc, value);
     case DecafParserTokenTypes.INC_ASSIGN:
      ExpressionNode inc_result = new MathOpNode(op_ast_location,
        loc_var, value, MathOp.ADD);
      return new AssignNode(sl, loc, inc_result);
     case DecafParserTokenTypes.DEC_ASSIGN:
      ExpressionNode dec_result = new MathOpNode(op_ast_location,
        loc_var, value, MathOp.SUBTRACT);
      return new AssignNode(sl, loc, dec_result);
     default:
      ErrorReporting.reportError(new IrException(op_ast_location,
        "Invalid assignment operation " + op_ast.getText()));
      return new AssignNode(sl, loc, value);
    }
  }

  /**
   * Processes a {@link DecafParserTokenTypes.METHOD_DECL} token.
   */
  protected MethodDeclNode processMethodDecl(AST node, SourceLocation sl) {
    // #([METHOD_DECL,"MethodDecl"], ty, vo, name, darg_accum, body);
    // Either void or a type will be present, but not both.
    AST method_return_type_ast = node.getFirstChild();
    AST method_name_ast = method_return_type_ast.getNextSibling();
    AST method_args_ast = method_name_ast.getNextSibling();
    AST method_body_ast = method_args_ast.getNextSibling();

    SourceLocation method_return_type_loc =
      new SourceLocation(method_return_type_ast);
    DecafType method_return_type;
    switch (method_return_type_ast.getType()) {
     case DecafParserTokenTypes.TK_int:
      method_return_type = DecafType.INT;
      break;
     case DecafParserTokenTypes.TK_boolean:
      method_return_type = DecafType.BOOLEAN;
      break;
     case DecafParserTokenTypes.TK_void:
      method_return_type = DecafType.VOID;
      break;
     default:
      ErrorReporting.reportError(new IrException(method_return_type_loc,
        "Invalid method return type"));
      method_return_type = DecafType.VOID;
    }
    String method_name = method_name_ast.getText();
    @SuppressWarnings("unchecked")
    List<VarDeclNode> method_args = (List<VarDeclNode>)visit(method_args_ast);
    BlockNode method_body = (BlockNode)visit(method_body_ast);
    return new MethodDeclNode(sl, method_return_type, method_name,
                              method_args, method_body);
  }

  /**
   * Processes a {@link DecafParserTokenTypes.LOCAL_VAR_DECL} or a
   * {@link DecafParserTokenTypes.DECLARATION_ARG} token.
   */
  protected VarDeclNode processArgOrLocal(AST node, SourceLocation sl) {
    // #([LOCAL_VAR_DECL,"VarDecl"], t, n)
    // #([DECLARATION_ARG,"Arg"], arg1t, arg1n)
    AST local_type_ast = node.getFirstChild();
    AST local_name_ast = local_type_ast.getNextSibling();

    SourceLocation local_type_location = new SourceLocation(local_type_ast);
    String local_name = local_name_ast.getText();
    DecafType local_type;
    switch (local_type_ast.getType()) {
     case DecafParserTokenTypes.TK_boolean:
      local_type = DecafType.BOOLEAN;
      break;
     case DecafParserTokenTypes.TK_int:
      local_type = DecafType.INT;
      break;
     default:
      ErrorReporting.reportError(
        new IrException(local_type_location, "Invalid array type"));
      local_type = DecafType.VOID;
    }
    return new VarDeclNode(sl, local_type, local_name);
  }

  /**
   * Processes a {@link DecafParserTokenTypes.FIELD_DECL} token.
   */
  protected FieldDeclNode processFieldDec(AST node, SourceLocation sl) {
    // #([FIELD_DECL,"FieldDecl"], type, name, size)
    AST field_type_ast = node.getFirstChild();
    AST field_name_ast = field_type_ast.getNextSibling();
    AST field_size_ast = field_name_ast.getNextSibling();

    SourceLocation field_type_location = new SourceLocation(field_type_ast);
    String field_name = field_name_ast.getText();
    DecafType field_type;
    if (field_size_ast != null) {
      switch (field_type_ast.getType()) {
       case DecafParserTokenTypes.TK_boolean:
        field_type = DecafType.BOOLEAN_ARRAY;
        break;
       case DecafParserTokenTypes.TK_int:
        field_type = DecafType.INT_ARRAY;
        break;
       default:
        ErrorReporting.reportError(
          new IrException(field_type_location, "Invalid scalar type"));
        field_type = DecafType.VOID;
      }
      int array_size = ((IntNode)visit(field_size_ast)).getValue();
      return new ArrayDeclNode(
        sl, field_type, field_name, array_size);
    } else {
      switch (field_type_ast.getType()) {
       case DecafParserTokenTypes.TK_boolean:
        field_type = DecafType.BOOLEAN;
        break;
       case DecafParserTokenTypes.TK_int:
        field_type = DecafType.INT;
        break;
       default:
        ErrorReporting.reportError(
          new IrException(field_type_location, "Invalid array type"));
        field_type = DecafType.VOID;
      }
      return new VarDeclNode(sl, field_type, field_name);
    }
  }

  /**
   * Processes a {@link DecafParserTokenTypes.LOCATION} token.
   */
  protected LocationNode processLocation(AST node, SourceLocation sl) {
    // #([LOCATION,"Location"], n, i)
    AST location_name_ast = node.getFirstChild();
    AST location_index_ast = location_name_ast.getNextSibling();
    String location_name = location_name_ast.getText();
    if (location_index_ast != null) {
      ExpressionNode location_index =
        (ExpressionNode)visit(location_index_ast);
      return new ArrayLocationNode(sl, location_name, location_index);
    } else {
      return new ScalarLocationNode(sl, location_name);
    }
  }


  /**
   * Processes an {@link DecafParserTokenTypes.INTEGER_LITERAL} token.
   */
  protected IntNode processInt(AST node, SourceLocation sl) {
    String raw_int = node.getFirstChild().getText();
    String HEX_PREFIX = "0x";
    int HEX_RADIX = 16;
    if (raw_int.startsWith(HEX_PREFIX)) {
      try {
        return new IntNode(sl, Integer.parseInt(
          "-" + raw_int.substring(HEX_PREFIX.length()), HEX_RADIX), true);
      } catch (NumberFormatException nfe) {
        // Since 0x[a-fA-F0-9]+ must parse, this means we were out of range.
        ErrorReporting.reportError(
          new IrException(sl, "Hex integer literal out of range"));
        return new IntNode(sl, Integer.MAX_VALUE);
      }
    } else {
      try {
        return new IntNode(sl, Integer.parseInt("-" + raw_int), true);
      } catch (NumberFormatException nfe) {
        // Since [0-9]+ must parse, this means we were out of range.
        ErrorReporting.reportError(
          new IrException(sl, "Decimal integer literal out of range"));
        return new IntNode(sl, Integer.MAX_VALUE);
      }
    }
  }

  /**
   * Processes an unary {@link DecafParserTokenTypes.MINUS} token.
   */
  protected ExpressionNode processMinus(AST childAst, SourceLocation sl) {
    ExpressionNode child = (ExpressionNode)visit(childAst);
    if (child instanceof IntNode) {
      IntNode intChild = (IntNode)child;
      return new IntNode(sl, -intChild.getValue());
    } else {
      return new MinusNode(sl, child);
    }
  }
  /**
   * Converts a subtree containing {@link ASTNode} children of type T to a
   * {@link NodeList} of those children.
   */
  @SuppressWarnings("unchecked")
  protected <T extends ASTNode> NodeList<T> convertToList(AST parent) {
    SourceLocation sl = new SourceLocation(parent);
    NodeList<T> list = new NodeList<T>(sl);

    for (AST item = parent.getFirstChild(); item != null;
         item = item.getNextSibling()) {
      list.add(((T)visit(item)));
    }
    return list;
  }

  /**
   * Transforms ([expr]) ([null] [op] [expr]) into ([expr] [op] [expr]).
   */
  protected ExpressionNode composite(ExpressionNode left, BinaryOpNode right) {
    if (right == null) {
      return left;
    }
    BinaryOpNode inner = right;
    while (inner != null && inner.getLeft() != null) {
      ExpressionNode prospective = inner.getLeft();
      if (prospective instanceof BinaryOpNode) {
        inner = (BinaryOpNode)prospective;
      } else {
        ErrorReporting.reportError(new IrException(prospective.getSourceLoc(),
          "Unable to composite expression into a full BinaryOpNode."));
        return right;
      }
    }
    if (inner == null) {
      ErrorReporting.reportError(new IrException(right.getSourceLoc(),
        "Unable to composite expression into a full BinaryOpNode."));
      return right;
    }
    inner.setLeft(left);
    return right;
  }
}
