package edu.mit.compilers.le02.ast;

import edu.mit.compilers.le02.DecafType;

public final class BoolOpNode extends BinaryOpNode {
	private BoolOp op;

	public BoolOpNode(String filename, int line, int col, BoolOp op) {
		super(filename, line, col);
		this.op = op;
	}

	@Override
	public DecafType getType() {
		return DecafType.BOOLEAN;
	}

	public BoolOp getOp() {
		return op;
	}

	public enum BoolOp {
		LT,
		GT,
		LE,
		GE,
		EQ,
		NEQ,
		AND,
		OR;
	}	
}
