package edu.mit.compilers.le02.ast;

import java.util.ArrayList;
import java.util.List;

public class AssignNode extends StatementNode {
	protected LocationNode loc;
	protected ExpressionNode value;

	public AssignNode(SourceLocation sl) {
		super(sl);
	}
	
	public AssignNode(SourceLocation sl, LocationNode loc, ExpressionNode value) {
		super(sl);
		this.loc = loc;
		this.value = value;
	}

	@Override
	public List<ASTNode> getChildren() {
		List<ASTNode> children = new ArrayList<ASTNode>();
		children.add(loc);
		children.add(value);
		return children;
	}

	public LocationNode getLoc() {
		return loc;
	}

	public void setLoc(LocationNode loc) {
		this.loc = loc;
	}

	public ExpressionNode getValue() {
		return value;
	}

	public void setValue(ExpressionNode value) {
		this.value = value;
	}


}
