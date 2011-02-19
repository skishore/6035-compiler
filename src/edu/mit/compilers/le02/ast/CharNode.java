package edu.mit.compilers.le02.ast;

import java.util.Collections;
import java.util.List;

public final class CharNode extends ExpressionNode {
  private int value;

  public CharNode(SourceLocation sl, char c) {
    super(sl);
    this.value = c;
  }

  @Override
  public List<ASTNode> getChildren() {
    return Collections.emptyList();
  }

  public int getValue() {
    return value;
  }
}
