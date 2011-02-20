package edu.mit.compilers.le02.ast;

import java.util.Collections;
import java.util.List;


public final class ScalarLocationNode extends LocationNode {

	public ScalarLocationNode(SourceLocation sl, String name) {
		super(sl, name);
	}

	@Override
	public List<ASTNode> getChildren() {
		return Collections.emptyList();
	}

	@Override
  public boolean equals(Object o) {
    if (!(o instanceof ScalarLocationNode)) {
      return false;
    }
    ScalarLocationNode other = (ScalarLocationNode)o;
    return (name.equals(other.getName()) &&
            type.equals(other.getType()));
  }

	@Override
	public void visit(ASTNodeVisitor v) { v.accept(this); }
}
