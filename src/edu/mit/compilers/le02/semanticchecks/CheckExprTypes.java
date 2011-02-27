package edu.mit.compilers.le02.semanticchecks;

import edu.mit.compilers.le02.DecafType;
import edu.mit.compilers.le02.ErrorReporting;
import edu.mit.compilers.le02.ast.AssignNode;
import edu.mit.compilers.le02.ast.ASTNode;
import edu.mit.compilers.le02.ast.ASTNodeVisitor;
import edu.mit.compilers.le02.ast.BoolOpNode;
import edu.mit.compilers.le02.ast.BoolOpNode.BoolOp;
import edu.mit.compilers.le02.ast.ClassNode;
import edu.mit.compilers.le02.ast.IfNode;
import edu.mit.compilers.le02.ast.ForNode;
import edu.mit.compilers.le02.ast.MathOpNode;
import edu.mit.compilers.le02.ast.MinusNode;
import edu.mit.compilers.le02.ast.NotNode;
import edu.mit.compilers.le02.semanticchecks.SemanticException;

public class CheckExprTypes extends ASTNodeVisitor<Boolean> {
  /** Holds the CheckExprTypes singleton. */
  private static CheckExprTypes instance;

  /**
   * Retrieves the CheckExprTypes singleton, creating if necessary.
   */
  public static CheckExprTypes getInstance() {
    if (instance == null) {
      instance = new CheckExprTypes();
    }
    return instance;
  }

  /**
   * Checks that every operation is performed on two expressions of the
   * correct type,
   * that the types of assignments agree,
   * that for statements have integer bounds,
   * and that if statements have boolean conditionals,
   */
  public static void check(ASTNode root) {
    assert(root instanceof ClassNode);
    root.accept(getInstance());
  }

  @Override
  public Boolean visit(BoolOpNode node) {
    DecafType expected = DecafType.INT;

    if ((node.getOp() == BoolOp.AND) || (node.getOp() == BoolOp.OR)) {
      expected = DecafType.BOOLEAN;
    }

    if ((node.getOp() == BoolOp.EQ) || (node.getOp() == BoolOp.NEQ)) {
      expected = DecafType.simplify(node.getLeft().getType());
    } else if (DecafType.simplify(node.getLeft().getType()) != expected) {
      ErrorReporting.reportError(
        new SemanticException(node.getLeft().getSourceLoc(),
          "Type mismatch: " + node.getOp() + " requires " + expected +
          " expression"));
    }
    if (DecafType.simplify(node.getRight().getType()) != expected) {
      ErrorReporting.reportError(
        new SemanticException(node.getRight().getSourceLoc(),
          "Type mismatch: " + node.getOp() + " requires " + expected +
          " expression"));
    }

    defaultBehavior(node);
    return true;
  }

  @Override
  public Boolean visit(MathOpNode node) {
    if (DecafType.simplify(node.getLeft().getType()) != DecafType.INT) {
      ErrorReporting.reportError(
        new SemanticException(node.getLeft().getSourceLoc(),
          "Type mismatch: " + node.getOp() + " requires integer expression"));
    }
    if (DecafType.simplify(node.getRight().getType()) != DecafType.INT) {
      ErrorReporting.reportError(
        new SemanticException(node.getRight().getSourceLoc(),
          "Type mismatch: " + node.getOp() + " requires integer expression"));
    }

    defaultBehavior(node);
    return true;
  }

  @Override
  public Boolean visit(MinusNode node) {
    if (DecafType.simplify(node.getExpr().getType()) != DecafType.INT) {
      ErrorReporting.reportError(
        new SemanticException(node.getExpr().getSourceLoc(),
          "Type mismatch: - operator requires integer expression"));
    }

    defaultBehavior(node);
    return true;
  }

  @Override
  public Boolean visit(NotNode node) {
    if (DecafType.simplify(node.getExpr().getType()) != DecafType.BOOLEAN) {
      ErrorReporting.reportError(
        new SemanticException(node.getExpr().getSourceLoc(),
          "Type mismatch: ! operator requires boolean expression"));
    }

    defaultBehavior(node);
    return true;
  }

  @Override
  public Boolean visit(AssignNode node) {
    DecafType expected = DecafType.simplify(node.getLoc().getType());
    if (expected != DecafType.simplify(node.getValue().getType())) {
      ErrorReporting.reportError(
        new SemanticException(node.getValue().getSourceLoc(),
          "Type mismatch: assignment to " + node.getLoc().getName() +
          " should be " + expected + " expression"));
    }

    defaultBehavior(node);
    return true;
  }

  @Override
  public Boolean visit(ForNode node) {
    AssignNode init = node.getInit();
    if (DecafType.simplify(init.getValue().getType()) != DecafType.INT) {
      ErrorReporting.reportError(
        new SemanticException(init.getValue().getSourceLoc(),
          "Type mismatch: for initializer for " + init.getLoc().getName() +
          " should be integer expression"));
    }
    if (DecafType.simplify(node.getEnd().getType()) != DecafType.INT) {
      ErrorReporting.reportError(
        new SemanticException(node.getEnd().getSourceLoc(),
          "Type mismatch: for terminator for " + init.getLoc().getName() +
          " should be integer expression"));
    }

    node.getBody().accept(this);
    return true;
  }

  @Override
  public Boolean visit(IfNode node) {
    if (DecafType.simplify(node.getCondition().getType()) !=
          DecafType.BOOLEAN) {
      ErrorReporting.reportError(
        new SemanticException(node.getCondition().getSourceLoc(),
          "Type mismatch: if condition should be boolean expression"));
    }

    defaultBehavior(node);
    return true;
  }

}
