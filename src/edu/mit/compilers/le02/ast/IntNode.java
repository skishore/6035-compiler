package edu.mit.compilers.le02.ast;

import java.util.Collections;
import java.util.List;

import edu.mit.compilers.le02.DecafType;
import edu.mit.compilers.le02.SourceLocation;

/**
 * Stores an integer. We initially store the inverse of the value since we
 * need to store input values of [0,2^31]. We then later verify that the
 * values of 2^31 (outside the range) were in fact inverted to -2^31.
 */
public final class IntNode extends ExpressionNode {
  private int value;
  private boolean invert;

  public IntNode(SourceLocation sl, int value) {
    this(sl, value, false);
  }
  public IntNode(SourceLocation sl, int value, boolean invert) {
    super(sl);
    this.value = value;
    this.invert = invert;
  }

  @Override
  public List<ASTNode> getChildren() {
    return Collections.emptyList();
  }

  public int getValue() {
    if (invert == true) {
      return -value;
    } else {
      return value;
    }
  }

  @Override
  public String toString() {
    return "" + getValue();
  }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof IntNode)) {
      return false;
    }
    return getValue() == ((IntNode)o).getValue();
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
