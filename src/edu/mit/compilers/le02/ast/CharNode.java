package edu.mit.compilers.le02.ast;

import java.util.Collections;
import java.util.List;

import edu.mit.compilers.le02.SourceLocation;

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

 @Override
  public boolean equals(Object o) {
    if (!(o instanceof CharNode)) {
      return false;
    }
    return value == ((CharNode)o).getValue();
  }

  @Override
  public void visit(ASTNodeVisitor v) { v.accept(this); }
}
