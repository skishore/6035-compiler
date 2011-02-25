package edu.mit.compilers.le02.ast;

import java.util.List;

import edu.mit.compilers.le02.SourceLocation;

public abstract class UnaryOpNode extends ExpressionNode {
  protected ExpressionNode expr;

  public UnaryOpNode(SourceLocation sl, ExpressionNode expr) {
    super(sl);
    this.expr = expr;
  }

  @Override
  public List<ASTNode> getChildren() {
    return NodeUtil.makeChildren(expr);
  }

  public void setExpr(ExpressionNode expr) {
    this.expr = expr;
  }

  public ExpressionNode getExpr() {
    return expr;
  }

}
