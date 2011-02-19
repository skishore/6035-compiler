package edu.mit.compilers.le02.ast;

import java.util.Collections;
import java.util.List;


public final class ScalarLocationNode extends LocationNode {

	public ScalarLocationNode(SourceLocation sl, String name) {
		super(sl, name);
	}

	@Override
	public List<ASTNode> getChildren() {
		return Collections.emptyList();
	}
}
