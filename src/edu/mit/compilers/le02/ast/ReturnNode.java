package edu.mit.compilers.le02.ast;

import java.util.Collections;
import java.util.List;

import edu.mit.compilers.le02.SourceLocation;

public final class ReturnNode extends StatementNode {
  private ExpressionNode retValue;
  private boolean hasValue;

  public ReturnNode(SourceLocation sl) {
    super(sl);
    this.hasValue = false;
  }

  public ReturnNode(SourceLocation sl, ExpressionNode retValue) {
    super(sl);
    this.retValue = retValue;
    this.hasValue = true;
  }

  @Override
  public List<ASTNode> getChildren() {
    if (!hasValue) {
      return Collections.emptyList();
    }
    return NodeUtil.makeChildren(retValue);
  }

  public boolean hasValue() {
    return hasValue;
  }

  public ExpressionNode getRetValue() {
    return retValue;
  }

  public void setRetValue(ExpressionNode retValue) {
    this.retValue = retValue;
  }

  @Override
  public void visit(ASTNodeVisitor v) { v.accept(this); }
}
