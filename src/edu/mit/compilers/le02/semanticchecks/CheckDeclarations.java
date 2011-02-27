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

public class CheckDeclarations extends ASTNodeVisitor<Boolean> {
  /** Holds the CheckDeclarations singleton. */
  private static CheckDeclarations instance;
  private static SymbolTable methodTable;
  private static SymbolTable varTable;

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
      methodTable = node.getDesc().getMethodSymbolTable();
      varTable = node.getDesc().getFieldSymbolTable();

      defaultBehavior(node);
      return true;
  }

  @Override
  public Boolean visit(MethodDeclNode node) {
      SymbolTable parent = varTable;
      varTable = ((MethodDescriptor)methodTable.getMap().get(
        node.getName())).getParamSymbolTable();

      defaultBehavior(node);

      varTable = parent;
      return true;
  }

  @Override
  public Boolean visit(ForNode node) {
      node.getBody().accept(this);
      return true;
  }

  @Override
  public Boolean visit(BlockNode node) {
      SymbolTable parent = varTable;
      varTable = node.getLocalSymbolTable();

      defaultBehavior(node);

      varTable = parent;
      return true;
  }

  @Override
  public Boolean visit(ArrayLocationNode node) {
    TypedDescriptor desc =
      (TypedDescriptor)(varTable.get(node.getName(), true));
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
      (TypedDescriptor)(varTable.get(node.getName(), true));
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
    if (!methodTable.contains(node.getName())) {
      ErrorReporting.reportError(
        new SymbolTableException(node.getSourceLoc(),
          "Undeclared method " + node.getName()));
    }

    defaultBehavior(node);
    return true;
  }
}
