package edu.mit.compilers.le02.ast;


public class NotNode extends UnaryOpNode {

	public NotNode(SourceLocation sl, ExpressionNode expr) {
		super(sl, expr);
	}

	@Override
	public void visit(ASTNodeVisitor v) { v.accept(this); }
}
