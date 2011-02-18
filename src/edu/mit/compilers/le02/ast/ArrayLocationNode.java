package edu.mit.compilers.le02.ast;

import java.util.ArrayList;
import java.util.List;

import edu.mit.compilers.le02.DecafType;

public final class ArrayLocationNode extends LocationNode {
	private ExpressionNode index;

	public ArrayLocationNode(String filename, int line, int col, DecafType type, String name) {
		super(filename, line, col, type, name);
	}

	@Override
	public List<ASTNode> getChildren() {
		List<ASTNode> children = new ArrayList<ASTNode>();
		children.add(index);
		return children;
	}

}
