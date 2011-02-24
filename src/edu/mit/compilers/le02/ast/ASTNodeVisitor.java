package edu.mit.compilers.le02.ast;

public abstract class ASTNodeVisitor<T> {

  /**
   * Helper method: visits the children of the node in order
   */
  protected void defaultBehavior(ASTNode node) {
    for (ASTNode child : node.getChildren()) {
      child.accept(this);
    }
  }

  /*
   * Default behavior is to visit all nodes, but do nothing at each.
   * This way you only need to override the ones you care about to make
   * a new visitor.
   *
   * Alternatively, you can override the default behavior method if you
   * need to
   */
  public T visit(ASTNode node) {
    defaultBehavior(node); 
    return null;
  }

  /*
   * DK: I realized that I can just use the one above, without actually messing
   * up the way that Java deals with the classes.  I'm leaving the list below
   * for easy reference of the concrete classes for when you want to make a new
   * visitor.
   *
  public T visit(ArrayDeclNode node) {defaultBehavior(node);}
  public T visit(ArrayLocationNode node) {defaultBehavior(node);}
  public T visit(AssignNode node) {defaultBehavior(node);}
  public T visit(BlockNode node) {defaultBehavior(node);}
  public T visit(BooleanNode node) {defaultBehavior(node);}
  public T visit(BoolOpNode node) {defaultBehavior(node);}
  public T visit(BreakNode node) {defaultBehavior(node);}
  public T visit(CallStatementNode node) {defaultBehavior(node);}
  public T visit(CharNode node) {defaultBehavior(node);}
  public T visit(ClassNode node) {defaultBehavior(node);}
  public T visit(ContinueNode node) {defaultBehavior(node);}
  public T visit(ForNode node) {defaultBehavior(node);}
  public T visit(IfNode node) {defaultBehavior(node);}
  public T visit(IntNode node) {defaultBehavior(node);}
  public T visit(MathOpNode node) {defaultBehavior(node);}
  public T visit(MethodCallNode node) {defaultBehavior(node);}
  public T visit(MethodDeclNode node) {defaultBehavior(node);}
  public T visit(MinusNode node) {defaultBehavior(node);}
  public T visit(NotNode node) {defaultBehavior(node);}
  public T visit(ReturnNode node) {defaultBehavior(node);}
  public T visit(ScalarLocationNode node) {defaultBehavior(node);}
  public T visit(StringNode node) {defaultBehavior(node);}
  public T visit(SyscallArgNode node) {defaultBehavior(node);}
  public T visit(SystemCallNode node) {defaultBehavior(node);}
  public T visit(VarDeclNode node) {defaultBehavior(node);}
  public T visit(VariableNode node) {defaultBehavior(node);}
  */

}
