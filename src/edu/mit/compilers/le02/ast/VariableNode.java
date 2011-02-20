package edu.mit.compilers.le02.ast;

import java.util.List;

public final class VariableNode extends ExpressionNode {
	private LocationNode loc;

	public VariableNode(SourceLocation sl) {
		super(sl);
	}
	
	public VariableNode(SourceLocation sl, LocationNode loc) {
		super(sl);
		this.loc = loc;
	}

	@Override
	public List<ASTNode> getChildren() {
		return NodeUtil.makeChildren(loc);
	}

	public void setLoc(LocationNode loc) {
		this.loc = loc;
	}

	public LocationNode getLoc() {
		return loc;
	}

	@Override
  public boolean equals(Object o) {
    if (!(o instanceof VariableNode)) {
      return false;
    }
    VariableNode other = (VariableNode)o;
    return loc.equals(other.getLoc());
  }

  @Override
	public void visit(ASTNodeVisitor v) { v.accept(this); }
}
