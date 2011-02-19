package edu.mit.compilers.le02.ast;

import java.util.Arrays;


public final class MathOpNode extends BinaryOpNode {
	private MathOp op;

	public MathOpNode(SourceLocation sl, ExpressionNode left, ExpressionNode right, MathOp op) {
		super(sl, left, right);
		this.op = op;
	}

	public MathOp getOp() {
		return op;
	}

	@Override
  public String toString() {
	  return op + " " + Arrays.toString(getChildren().toArray());
	}

	public enum MathOp {
		ADD("+"),
		SUBTRACT("-"),
		MULTIPLY("*"),
		DIVIDE("/"),
		MODULO("%");
		private String disp; 
		private MathOp(String display) {
		  disp = display; 
		}
		@Override
		public String toString() {
		  return disp;
		}
	}	

}
