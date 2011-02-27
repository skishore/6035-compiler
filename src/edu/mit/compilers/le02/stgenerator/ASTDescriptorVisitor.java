package edu.mit.compilers.le02.stgenerator;

import edu.mit.compilers.le02.ast.ASTNodeVisitor;
import edu.mit.compilers.le02.ast.ArrayLocationNode;
import edu.mit.compilers.le02.ast.BlockNode;
import edu.mit.compilers.le02.ast.ClassNode;
import edu.mit.compilers.le02.ast.ForNode;
import edu.mit.compilers.le02.ast.MethodCallNode;
import edu.mit.compilers.le02.ast.ScalarLocationNode;
import edu.mit.compilers.le02.symboltable.ClassDescriptor;
import edu.mit.compilers.le02.symboltable.MethodDescriptor;
import edu.mit.compilers.le02.symboltable.SymbolTable;
import edu.mit.compilers.le02.symboltable.TypedDescriptor;
import edu.mit.compilers.le02.symboltable.SymbolTable.SymbolType;

/**
 * Sets the descriptors of the nodes in the AST
 * @author David Koh
 *
 */
public final class ASTDescriptorVisitor extends ASTNodeVisitor<Object> {
  private SymbolTable currST;

  public void setASTDescriptors(ClassNode node, ClassDescriptor desc) {
    currST = desc.getSymbolTable();
    node.setDesc(desc);

    defaultBehavior(node);
  }

  @Override
  public Object visit(BlockNode node) {
    SymbolTable last = currST;
    currST = node.getLocalSymbolTable();

    defaultBehavior(node);

    currST = last;
    return null;
  }

  @Override
  public Object visit(MethodCallNode node) {
    node.setDesc((MethodDescriptor) currST.get(node.getName(),
                                               SymbolType.METHOD));

    defaultBehavior(node);
    return null;
  }

  @Override
  public Object visit(ForNode node) {
    node.getInit().getLoc().setDesc(
      (TypedDescriptor)node.getBody().getLocalSymbolTable().get(
        node.getInit().getLoc().getName(), SymbolType.VARIABLE));
    // Do not visit init statement again, because it'll cause us to overwrite.
    node.getInit().getValue().accept(this);
    node.getEnd().accept(this);
    node.getBody().accept(this);
    return null;
  }

  @Override
  public Object visit(ScalarLocationNode node) {
    node.setDesc((TypedDescriptor) currST.get(node.getName(),
                                              SymbolType.VARIABLE));

    defaultBehavior(node);
    return null;
  }

  @Override
  public Object visit(ArrayLocationNode node) {
    node.setDesc((TypedDescriptor) currST.get(node.getName(),
                                              SymbolType.VARIABLE));

    defaultBehavior(node);
    return null;
  }

}
