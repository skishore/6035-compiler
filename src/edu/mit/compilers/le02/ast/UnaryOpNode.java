package edu.mit.compilers.le02.ast;

import java.util.List;

import edu.mit.compilers.le02.SourceLocation;
import edu.mit.compilers.le02.Util;

public abstract class UnaryOpNode extends ExpressionNode {
  protected ExpressionNode expr;

  public UnaryOpNode(SourceLocation sl, ExpressionNode expr) {
    super(sl);
    this.expr = expr;
  }

  @Override
  public List<ASTNode> getChildren() {
    return Util.makeList((ASTNode)expr);
  }

  public void setExpr(ExpressionNode expr) {
    this.expr = expr;
  }

  public ExpressionNode getExpr() {
    return expr;
  }

}
