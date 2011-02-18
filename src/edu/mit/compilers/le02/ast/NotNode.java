package edu.mit.compilers.le02.ast;

import edu.mit.compilers.le02.DecafType;

public class NotNode extends UnaryOpNode {

	public NotNode(SourceLocation sl) {
		super(sl);
	}

	public NotNode(SourceLocation sl, ExpressionNode expr) {
		super(sl, expr);
	}
}
