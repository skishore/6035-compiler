package edu.mit.compilers.le02.ast;

import java.util.ArrayList;
import java.util.List;

import edu.mit.compilers.le02.DecafType;

public final class ArrayLocationNode extends LocationNode {
	private ExpressionNode index;

	public ArrayLocationNode(SourceLocation sl, DecafType type, String name, ExpressionNode index) {
		super(sl, type, name);
		this.index = index;
	}

	@Override
	public List<ASTNode> getChildren() {
		List<ASTNode> children = new ArrayList<ASTNode>();
		children.add(index);
		return children;
	}

	public void setIndex(ExpressionNode index) {
		this.index = index;
	}

	public ExpressionNode getIndex() {
		return index;
	}
	
	public void visit(ASTNodeVisitor v) { v.accept(this); }

}
