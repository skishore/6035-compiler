package edu.mit.compilers.le02.ast;


public class NotNode extends UnaryOpNode {

	public NotNode(SourceLocation sl, ExpressionNode expr) {
		super(sl, expr);
	}
}
