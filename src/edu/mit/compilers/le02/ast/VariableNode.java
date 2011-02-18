package edu.mit.compilers.le02.ast;

import java.util.ArrayList;
import java.util.List;

import edu.mit.compilers.le02.DecafType;

public final class VariableNode extends ExpressionNode {
	private LocationNode loc;

	public VariableNode(String filename, int line, int col) {
		super(filename, line, col);
	}

	@Override
	public DecafType getType() {
		return loc.getType();
	}

	@Override
	public List<ASTNode> getChildren() {
		List<ASTNode> children = new ArrayList<ASTNode>();
		children.add(loc);		
		return null;
	}

	public LocationNode getLoc() {
		return loc;
	}

}
