package edu.mit.compilers.le02.ast;


public final class BoolOpNode extends BinaryOpNode {
	private BoolOp op;

	public BoolOpNode(SourceLocation sl, BoolOp op) {
		super(sl);
	}
	
	public BoolOpNode(SourceLocation sl, ExpressionNode left, ExpressionNode right, BoolOp op) {
		super(sl, left, right);
		this.op = op;
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
