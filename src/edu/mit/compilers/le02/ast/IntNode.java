package edu.mit.compilers.le02.ast;

import java.util.Collections;
import java.util.List;

import edu.mit.compilers.le02.DecafType;

public final class IntNode extends ExpressionNode {
	private int value;

	public IntNode(String filename, int line, int col, int value) {
		super(filename, line, col);
		this.value = value;
	}

	@Override
	public DecafType getType() {
		return DecafType.INT;
	}

	@Override
	public List<ASTNode> getChildren() {
		return Collections.emptyList();
	}

	public int getValue() {
		return value;
	}
}
