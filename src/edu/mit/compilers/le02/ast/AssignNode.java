package edu.mit.compilers.le02.ast;

import java.util.List;

import edu.mit.compilers.le02.SourceLocation;

public class AssignNode extends StatementNode {
  protected LocationNode loc;
  protected ExpressionNode value;

  public AssignNode(SourceLocation sl, LocationNode loc, ExpressionNode value) {
    super(sl);
    this.loc = loc;
    this.value = value;
  }

  @Override
  public List<ASTNode> getChildren() {
    return NodeUtil.makeChildren(loc, value);
  }

  public LocationNode getLoc() {
    return loc;
  }

  public void setLoc(LocationNode loc) {
    this.loc = loc;
  }

  public ExpressionNode getValue() {
    return value;
  }

  public void setValue(ExpressionNode value) {
    this.value = value;
  }

  @Override
  public <T> T accept(ASTNodeVisitor<T> v) { return v.visit(this); }
}
