package edu.mit.compilers.le02.ast;

import java.util.List;

import edu.mit.compilers.le02.SourceLocation;

public final class ForNode extends StatementNode {
  private AssignNode init;
  private ExpressionNode end;
  private BlockNode body;

  public ForNode(SourceLocation sl, AssignNode init, ExpressionNode end, BlockNode body) {
    super(sl);
    this.init = init;
    this.end = end;
    this.body = body;
  }

  @Override
  public List<ASTNode> getChildren() {
    return NodeUtil.makeChildren(init, end, body);
  }

  public AssignNode getInit() {
    return init;
  }

  public void setInit(AssignNode init) {
    this.init = init;
  }

  public ExpressionNode getEnd() {
    return end;
  }

  public void setEnd(ExpressionNode end) {
    this.end = end;
  }

  public BlockNode getBody() {
    return body;
  }

  public void setBody(BlockNode body) {
    this.body = body;
  }

  @Override
  public void visit(ASTNodeVisitor v) { v.accept(this); }
}
