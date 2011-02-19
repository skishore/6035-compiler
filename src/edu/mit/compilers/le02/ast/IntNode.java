package edu.mit.compilers.le02.ast;

import java.util.Collections;
import java.util.List;

public final class IntNode extends ExpressionNode {
  private int value;

  public IntNode(SourceLocation sl, int value) {
    super(sl);
    this.value = value;
  }

  @Override
  public List<ASTNode> getChildren() {
    return Collections.emptyList();
  }

  public int getValue() {
    return value;
  }

  @Override
  public String toString() {
    return "" + value;
  }
}
