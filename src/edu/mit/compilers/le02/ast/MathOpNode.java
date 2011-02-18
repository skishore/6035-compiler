package edu.mit.compilers.le02.ast;

import edu.mit.compilers.le02.DecafType;

public final class MathOpNode extends BinaryOpNode {
	private MathOp op;

	public MathOpNode(String filename, int line, int col, MathOp op) {
		super(filename, line, col);
		this.op = op;
	}

	@Override
	public DecafType getType() {
		return DecafType.INT;
	}
	

	public MathOp getOp() {
		return op;
	}


	public enum MathOp {
		ADD,
		SUBTRACT,
		MULTIPLY,
		DIVIDE
	}	

}
