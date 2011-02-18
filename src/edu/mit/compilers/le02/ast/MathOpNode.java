package edu.mit.compilers.le02.ast;

import edu.mit.compilers.le02.DecafType;

public final class MathOpNode extends BinaryOpNode {
	private MathOp op;

	public MathOpNode(SourceLocation sl, MathOp op) {
		super(sl);
		this.op = op;
	}
	
	public MathOpNode(SourceLocation sl, ExpressionNode left, ExpressionNode right, MathOp op) {
		super(sl, left, right);
		this.op = op;
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
