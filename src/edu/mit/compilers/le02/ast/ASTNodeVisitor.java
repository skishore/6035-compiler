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
   * need to.
   * 
   * Replacing these with a base class method doesn't work, due to Java's
   * inheritance handling.
   */
  public T visit(ArrayDeclNode node) {defaultBehavior(node); return null;}
  public T visit(ArrayLocationNode node) {defaultBehavior(node); return null;}
  public T visit(AssignNode node) {defaultBehavior(node); return null;}
  public T visit(BlockNode node) {defaultBehavior(node); return null;}
  public T visit(BooleanNode node) {defaultBehavior(node); return null;}
  public T visit(BoolOpNode node) {defaultBehavior(node); return null;}
  public T visit(BreakNode node) {defaultBehavior(node); return null;}
  public T visit(CallStatementNode node) {defaultBehavior(node); return null;}
  public T visit(CharNode node) {defaultBehavior(node); return null;}
  public T visit(ClassNode node) {defaultBehavior(node); return null;}
  public T visit(ContinueNode node) {defaultBehavior(node); return null;}
  public T visit(ForNode node) {defaultBehavior(node); return null;}
  public T visit(IfNode node) {defaultBehavior(node); return null;}
  public T visit(IntNode node) {defaultBehavior(node); return null;}
  public T visit(MathOpNode node) {defaultBehavior(node); return null;}
  public T visit(MethodCallNode node) {defaultBehavior(node); return null;}
  public T visit(MethodDeclNode node) {defaultBehavior(node); return null;}
  public T visit(MinusNode node) {defaultBehavior(node); return null;}
  public T visit(NodeList node) {defaultBehavior(node); return null;}
  public T visit(NotNode node) {defaultBehavior(node); return null;}
  public T visit(ReturnNode node) {defaultBehavior(node); return null;}
  public T visit(ScalarLocationNode node) {defaultBehavior(node); return null;}
  public T visit(StringNode node) {defaultBehavior(node); return null;}
  public T visit(SyscallArgNode node) {defaultBehavior(node); return null;}
  public T visit(SystemCallNode node) {defaultBehavior(node); return null;}
  public T visit(VarDeclNode node) {defaultBehavior(node); return null;}
  public T visit(VariableNode node) {defaultBehavior(node); return null;}

}
