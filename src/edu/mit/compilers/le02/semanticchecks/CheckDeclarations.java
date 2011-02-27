package edu.mit.compilers.le02.semanticchecks;

import edu.mit.compilers.le02.DecafType;
import edu.mit.compilers.le02.ErrorReporting;
import edu.mit.compilers.le02.ast.ArrayLocationNode;
import edu.mit.compilers.le02.ast.ASTNode;
import edu.mit.compilers.le02.ast.ASTNodeVisitor;
import edu.mit.compilers.le02.ast.BlockNode;
import edu.mit.compilers.le02.ast.ClassNode;
import edu.mit.compilers.le02.ast.ForNode;
import edu.mit.compilers.le02.ast.MethodCallNode;
import edu.mit.compilers.le02.ast.MethodDeclNode;
import edu.mit.compilers.le02.ast.ScalarLocationNode;
import edu.mit.compilers.le02.semanticchecks.SemanticException;
import edu.mit.compilers.le02.stgenerator.SymbolTableException;
import edu.mit.compilers.le02.symboltable.TypedDescriptor;
import edu.mit.compilers.le02.symboltable.MethodDescriptor;
import edu.mit.compilers.le02.symboltable.SymbolTable;
import edu.mit.compilers.le02.symboltable.SymbolTable.SymbolType;

public class CheckDeclarations extends ASTNodeVisitor<Boolean> {
  /** Holds the CheckDeclarations singleton. */
  private static CheckDeclarations instance;
  private static SymbolTable symbolTable;

  /**
   * Retrieves the CheckDeclarations singleton, creating if necessary.
   */
  public static CheckDeclarations getInstance() {
      if (instance == null) {
        instance = new CheckDeclarations();
      }
      return instance;
  }

  /**
   * Checks that every identifier is declared before it is used.
   */
  public static void check(ASTNode root) {
      assert(root instanceof ClassNode);
      root.accept(getInstance());
  }

  @Override
  public Boolean visit(ClassNode node) {
      symbolTable = node.getDesc().getSymbolTable();

      defaultBehavior(node);
      return true;
  }

  @Override
  public Boolean visit(MethodDeclNode node) {
      SymbolTable parent = symbolTable;
      symbolTable = ((MethodDescriptor) 
          symbolTable.get(node.getName(), SymbolType.METHOD)).getSymbolTable();

      defaultBehavior(node);

      symbolTable = parent;
      return true;
  }

  @Override
  public Boolean visit(ForNode node) {
      node.getBody().accept(this);
      return true;
  }

  @Override
  public Boolean visit(BlockNode node) {
      SymbolTable parent = symbolTable;
      symbolTable = node.getLocalSymbolTable();

      defaultBehavior(node);

      symbolTable = parent;
      return true;
  }

  @Override
  public Boolean visit(ArrayLocationNode node) {
    TypedDescriptor desc =
      (TypedDescriptor)(symbolTable.get(node.getName(), SymbolType.VARIABLE));
    if (desc == null) {
      ErrorReporting.reportError(
        new SymbolTableException(node.getSourceLoc(),
          "Undeclared array " + node.getName()));
    } else {
      if (!((desc.getType() == DecafType.INT_ARRAY) ||
          (desc.getType() == DecafType.BOOLEAN_ARRAY))) {
        ErrorReporting.reportError(
          new SemanticException(node.getSourceLoc(),
            "Indexing into non-array " + node.getName()));
      }

      if (node.getIndex().getType() != DecafType.INT) {
        ErrorReporting.reportError(
          new SemanticException(node.getIndex().getSourceLoc(),
            "Non-integer index into array " + node.getName()));
      }
    }

    defaultBehavior(node);
    return true;
  }

  @Override
  public Boolean visit(ScalarLocationNode node) {
    TypedDescriptor desc =
      (TypedDescriptor)(symbolTable.get(node.getName(), SymbolType.VARIABLE));
    if (desc == null) {
      ErrorReporting.reportError(
        new SymbolTableException(node.getSourceLoc(),
          "Undeclared variable " + node.getName()));
    } else if ((desc.getType() == DecafType.INT_ARRAY) ||
               (desc.getType() == DecafType.BOOLEAN_ARRAY)) {
      ErrorReporting.reportError(
        new SemanticException(node.getSourceLoc(),
          "Array " + node.getName() + " with no index"));
    }

    defaultBehavior(node);
    return true;
  }

  @Override
  public Boolean visit(MethodCallNode node) {
    if (symbolTable.get(node.getName(), SymbolType.METHOD) == null) {
      ErrorReporting.reportError(
        new SymbolTableException(node.getSourceLoc(),
          "Undeclared method " + node.getName()));
    }

    defaultBehavior(node);
    return true;
  }
}
