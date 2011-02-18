package edu.mit.compilers.le02.ast;

import java.util.Collections;
import java.util.List;

import edu.mit.compilers.le02.DecafType;

public final class BooleanNode extends ExpressionNode {
	private boolean value;

	public BooleanNode(String filename, int line, int col, boolean b) {
		super(filename, line, col);
		this.value = b;
	}

	@Override
	public DecafType getType() {
		return DecafType.BOOLEAN;
	}

	@Override
	public List<ASTNode> getChildren() {
		return Collections.emptyList();
	}

	public boolean getValue() {
		return value;
	}
}
