package edu.mit.compilers.le02.ast;

import java.util.ArrayList;
import java.util.List;

public abstract class UnaryOpNode extends ExpressionNode {
	protected ExpressionNode expr;

	public UnaryOpNode(String filename, int line, int col) {
		super(filename, line, col);
	}

	@Override
	public List<ASTNode> getChildren() {
		List<ASTNode> children = new ArrayList<ASTNode>();
		children.add(expr);
		return children;
	}

	public ExpressionNode getExpr() {
		return expr;
	}

}
