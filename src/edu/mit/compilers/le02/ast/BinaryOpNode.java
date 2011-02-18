package edu.mit.compilers.le02.ast;

import java.util.ArrayList;
import java.util.List;

public abstract class BinaryOpNode extends ExpressionNode {
	protected ExpressionNode left, right;

	public BinaryOpNode(String filename, int line, int col) {
		super(filename, line, col);
	}

	@Override
	public List<ASTNode> getChildren() {
		List<ASTNode> children = new ArrayList<ASTNode>();
		children.add(left);
		children.add(right);
		return null;
	}

}
