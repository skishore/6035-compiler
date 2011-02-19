package edu.mit.compilers.le02.ast;

import java.util.Arrays;


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

	@Override
  public String toString() {
    return op + " " + Arrays.toString(getChildren().toArray());
  }

	public enum BoolOp {
		LT("<"),
		GT(">"),
		LE("<="),
		GE(">="),
		EQ("=="),
		NEQ("!="),
		AND("&&"),
		OR("||");
    private String disp; 
    private BoolOp(String display) {
      disp = display;
    }
    @Override
    public String toString() {
      return disp;
    }
	}	
}
