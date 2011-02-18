package edu.mit.compilers.le02.ast;

import java.util.ArrayList;
import java.util.List;

public class AssignNode extends StatementNode {
	protected LocationNode loc;
	protected ExpressionNode value;

	public AssignNode(String filename, int line, int col) {
		super(filename, line, col);
	}

	@Override
	public List<ASTNode> getChildren() {
		List<ASTNode> children = new ArrayList<ASTNode>();
		children.add(loc);
		children.add(value);
		return children;
	}

}
