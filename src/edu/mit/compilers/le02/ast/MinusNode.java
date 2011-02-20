package edu.mit.compilers.le02.ast;


public final class MinusNode extends UnaryOpNode {

	public MinusNode(SourceLocation sl, ExpressionNode expr) {
		super(sl, expr);
	}

	@Override
  public boolean equals(Object o) {
    if (!(o instanceof MinusNode)) {
      return false;
    }
    MinusNode other = (MinusNode)o;
    return expr.equals(other.expr);
  }

	@Override
	public void visit(ASTNodeVisitor v) { v.accept(this); }
}
