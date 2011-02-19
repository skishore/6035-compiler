package edu.mit.compilers.le02.ast;

import java.util.ArrayList;
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
		List<ASTNode> children = new ArrayList<ASTNode>();
		children.add(loc);		
		return null;
	}

	public void setLoc(LocationNode loc) {
		this.loc = loc;
	}

	public LocationNode getLoc() {
		return loc;
	}

	public void visit(ASTNodeVisitor v) { v.accept(this); }
}
