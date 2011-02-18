package edu.mit.compilers.le02.ast;

import java.util.Collections;
import java.util.List;

import edu.mit.compilers.le02.DecafType;

public final class VarDeclNode extends DeclNode {

	public VarDeclNode(String filename, int line, int col, DecafType type,
			String id) {
		super(filename, line, col, type, id);
	}

	@Override
	public List<ASTNode> getChildren() {
		return Collections.emptyList();
	}

}
