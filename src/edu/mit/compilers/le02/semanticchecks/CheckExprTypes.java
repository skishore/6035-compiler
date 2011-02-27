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
import edu.mit.compilers.le02.stgenerator.SymbolTableException;
import edu.mit.compilers.le02.symboltable.TypedDescriptor;
import edu.mit.compilers.le02.symboltable.MethodDescriptor;
import edu.mit.compilers.le02.symboltable.SymbolTable;

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
     * Checks that every operation is performed on two expressions of the correct type,
     * that the types of assignments agree,
     * that if statements have boolean conditionals,
     * and that for statements have integer bounds.
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
                "Expected " + expected + " expression"));
        }
        if (DecafType.simplify(node.getRight().getType()) != expected) {
            ErrorReporting.reportError(
                new SemanticException(node.getRight().getSourceLoc(),
                "Expected " + expected + " expression"));
        }

        defaultBehavior(node);
        return true;
    }

    @Override
    public Boolean visit(MathOpNode node) {
        if (DecafType.simplify(node.getLeft().getType()) != DecafType.INT) {
            ErrorReporting.reportError(
                new SemanticException(node.getLeft().getSourceLoc(),
                "Expected INTEGER expression"));
        }
        if (DecafType.simplify(node.getRight().getType()) != DecafType.INT) {
            ErrorReporting.reportError(
                new SemanticException(node.getRight().getSourceLoc(),
                "Expected INTEGER expression"));
        }

        defaultBehavior(node);
        return true;
    }

    @Override
    public Boolean visit(MinusNode node) {
        if (DecafType.simplify(node.getExpr().getType()) != DecafType.INT) {
            ErrorReporting.reportError(
                new SemanticException(node.getExpr().getSourceLoc(),
                "- operator expected INTEGER expression"));
        }

        defaultBehavior(node);
        return true;
    }

    @Override
    public Boolean visit(NotNode node) {
        if (DecafType.simplify(node.getExpr().getType()) != DecafType.BOOLEAN) {
            ErrorReporting.reportError(
                new SemanticException(node.getExpr().getSourceLoc(),
                "! operator expected INTEGER expression"));
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
                "Assignment expected " + expected + " expression"));
        }

        defaultBehavior(node);
        return true;
    }
}
