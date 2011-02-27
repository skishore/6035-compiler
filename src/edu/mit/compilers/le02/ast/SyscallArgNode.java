package edu.mit.compilers.le02.ast;

import java.util.ArrayList;
import java.util.List;

import edu.mit.compilers.le02.SourceLocation;

public final class SyscallArgNode extends ASTNode {
  private boolean isString;
  private ExpressionNode expr;
  private StringNode str;

  public SyscallArgNode(SourceLocation sl, ExpressionNode expr) {
    super(sl);
    this.expr = expr;
    this.isString = false;
  }

  public SyscallArgNode(SourceLocation sl, StringNode str) {
    super(sl);
    this.str = str;
    this.isString = true;
  }

  @Override
  public List<ASTNode> getChildren() {
    List<ASTNode> children = new ArrayList<ASTNode>();
    if (isString) {
      children.add(str);
    } else {
      children.add(expr);
    }

    return children;
  }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof SyscallArgNode)) {
      return false;
    }
    SyscallArgNode other = (SyscallArgNode)o;
    return getChildren().equals(other.getChildren());
  }

  @Override
  public <T> T accept(ASTNodeVisitor<T> v) {
    return v.visit(this);
  }
}
