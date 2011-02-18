package edu.mit.compilers.le02.ast;

import edu.mit.compilers.le02.DecafType;

public final class MinusNode extends UnaryOpNode {

	public MinusNode(SourceLocation sl) {
		super(sl);
	}

	public MinusNode(SourceLocation sl, ExpressionNode expr) {
		super(sl, expr);
	}

}
