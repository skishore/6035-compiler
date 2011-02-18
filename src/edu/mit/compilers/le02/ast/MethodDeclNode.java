package edu.mit.compilers.le02.ast;

import java.util.ArrayList;
import java.util.List;

import edu.mit.compilers.le02.DecafType;

public final class MethodDeclNode extends DeclNode {
	private List<VarDeclNode> params;
	private BlockNode body;

	public MethodDeclNode(SourceLocation sl, DecafType type,
			String id) {
		super(sl, type, id);
	}

	public MethodDeclNode(SourceLocation sl, DecafType type,
			String id, List<VarDeclNode> params) {
		super(sl, type, id);
		params = this.params;
	}

	@Override
	public List<ASTNode> getChildren() {
		List<ASTNode> children = new ArrayList<ASTNode>(params);
		children.add(body);
		return children;
	}

}
