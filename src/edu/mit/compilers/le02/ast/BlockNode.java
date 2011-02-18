package edu.mit.compilers.le02.ast;

import java.util.ArrayList;
import java.util.List;

public final class BlockNode extends StatementNode {
	protected ArrayList<VarDeclNode> decls;
	protected ArrayList<StatementNode> statements;

	public BlockNode(String filename, int line, int col) {
		super(filename, line, col);
	}

	@Override
	public List<ASTNode> getChildren() {
		List<ASTNode> children = new ArrayList<ASTNode>();
		children.addAll(decls);
		children.addAll(statements);
		return children;
	}

}
