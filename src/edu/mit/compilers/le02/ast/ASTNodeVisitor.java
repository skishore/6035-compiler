package edu.mit.compilers.le02.ast;

public abstract class ASTNodeVisitor {

  /*
   * Helper method: visits the children of the node in order
   */
  protected void defaultBehavior(ASTNode node) {
    for (ASTNode child : node.getChildren()) {
      child.visit(this);
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
  public void accept(ArrayDeclNode node) {defaultBehavior(node);}
  public void accept(ArrayLocationNode node) {defaultBehavior(node);}
  public void accept(AssignNode node) {defaultBehavior(node);}
  public void accept(BlockNode node) {defaultBehavior(node);}
  public void accept(BooleanNode node) {defaultBehavior(node);}
  public void accept(BoolOpNode node) {defaultBehavior(node);}
  public void accept(BreakNode node) {defaultBehavior(node);}
  public void accept(CallStatementNode node) {defaultBehavior(node);}
  public void accept(CharNode node) {defaultBehavior(node);}
  public void accept(ClassNode node) {defaultBehavior(node);}
  public void accept(ContinueNode node) {defaultBehavior(node);}
  public void accept(ForNode node) {defaultBehavior(node);}
  public void accept(IfNode node) {defaultBehavior(node);}
  public void accept(IntNode node) {defaultBehavior(node);}
  public void accept(MathOpNode node) {defaultBehavior(node);}
  public void accept(MethodCallNode node) {defaultBehavior(node);}
  public void accept(MethodDeclNode node) {defaultBehavior(node);}
  public void accept(MinusNode node) {defaultBehavior(node);}
  public void accept(NotNode node) {defaultBehavior(node);}
  public void accept(ReturnNode node) {defaultBehavior(node);}
  public void accept(ScalarLocationNode node) {defaultBehavior(node);}
  public void accept(StringNode node) {defaultBehavior(node);}
  public void accept(SyscallArgNode node) {defaultBehavior(node);}
  public void accept(SystemCallNode node) {defaultBehavior(node);}
  public void accept(VarDeclNode node) {defaultBehavior(node);}
  public void accept(VariableNode node) {defaultBehavior(node);}

}
