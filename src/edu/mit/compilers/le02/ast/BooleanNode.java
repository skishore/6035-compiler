package edu.mit.compilers.le02.ast;

import java.util.Collections;
import java.util.List;

public final class BooleanNode extends ExpressionNode {
	private boolean value;

	public BooleanNode(SourceLocation sl, boolean b) {
		super(sl);
		this.value = b;
	}

	@Override
	public List<ASTNode> getChildren() {
		return Collections.emptyList();
	}

	public boolean getValue() {
		return value;
	}

	@Override
  public String toString() {
    return "" + value;
  }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof BooleanNode)) {
      return false;
    }
    return value == ((BooleanNode)o).getValue();
  }
}
