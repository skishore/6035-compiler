package edu.mit.compilers.le02.ast;

import java.util.Collections;
import java.util.List;

import edu.mit.compilers.le02.DecafType;
import edu.mit.compilers.le02.SourceLocation;


public final class CharNode extends ExpressionNode {
  private char value;

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
  public String toString() {
    return Character.toString(value);
  }

  @Override
  public <T> T accept(ASTNodeVisitor<T> v) { 
    return v.visit(this); 
  }

  @Override
  public DecafType getType() {
    return DecafType.INT;
  }
}
