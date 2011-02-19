package edu.mit.compilers.le02.ast;


public final class MinusNode extends UnaryOpNode {


	public MinusNode(SourceLocation sl, ExpressionNode expr) {
		super(sl, expr);
	}

	@Override
	public void visit(ASTNodeVisitor v) { v.accept(this); }
}
