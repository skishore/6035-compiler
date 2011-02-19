package edu.mit.compilers.le02.ast;

import java.util.Collections;
import java.util.List;

import edu.mit.compilers.le02.DecafType;

public final class ScalarLocationNode extends LocationNode {

	public ScalarLocationNode(SourceLocation sl, DecafType type, String name) {
		super(sl, type, name);
	}

	@Override
	public List<ASTNode> getChildren() {
		return Collections.emptyList();
	}
}
