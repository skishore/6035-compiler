package edu.mit.compilers.le02.ast;

import java.util.Collections;
import java.util.List;

public final class BreakNode extends StatementNode {

	public BreakNode(SourceLocation sl) {
		super(sl);
	}

	@Override
	public List<ASTNode> getChildren() {
		return Collections.emptyList();
	}

}
