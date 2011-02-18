package edu.mit.compilers.le02.ast;

import java.util.Collections;
import java.util.List;

import edu.mit.compilers.le02.DecafType;

public final class FieldDeclNode extends DeclNode {
	private int length;

	public FieldDeclNode(SourceLocation sl, DecafType type, String id, int length) {
		super(sl, type, id);
	}

	@Override
	public List<ASTNode> getChildren() {
		return Collections.emptyList();
	}

	public int getLength() {
		return length;
	}

}
