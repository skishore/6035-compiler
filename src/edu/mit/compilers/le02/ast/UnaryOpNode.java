package edu.mit.compilers.le02.ast;

import java.util.ArrayList;
import java.util.List;

public abstract class UnaryOpNode extends ExpressionNode {
	protected ExpressionNode expr;
	
	public UnaryOpNode(SourceLocation sl, ExpressionNode expr) {
		super(sl);
		this.expr = expr;
	}
	
	@Override
	public List<ASTNode> getChildren() {
		List<ASTNode> children = new ArrayList<ASTNode>();
		children.add(expr);
		return children;
	}

	public void setExpr(ExpressionNode expr) {
		this.expr = expr;
	}

	public ExpressionNode getExpr() {
		return expr;
	}

}
