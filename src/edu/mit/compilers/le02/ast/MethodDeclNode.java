package edu.mit.compilers.le02.ast;

import java.util.ArrayList;
import java.util.List;

import edu.mit.compilers.le02.DecafType;

public final class MethodDeclNode extends DeclNode {
	private ArrayList<VarDeclNode> params;
	private BlockNode body;

	public MethodDeclNode(String filename, int line, int col, DecafType type,
			String id) {
		super(filename, line, col, type, id);
	}

	@Override
	public List<ASTNode> getChildren() {
		List<ASTNode> children = new ArrayList<ASTNode>(params);
		children.add(body);
		return children;
	}

}
