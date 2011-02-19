package edu.mit.compilers.le02.ast;

import java.util.ArrayList;
import java.util.List;

public final class ForNode extends StatementNode {
	private AssignNode init;
	private ExpressionNode end;
	private BlockNode body;

	public ForNode(SourceLocation sl) {
		super(sl);
	}

	public ForNode(SourceLocation sl, AssignNode init, ExpressionNode end, BlockNode body) {
		super(sl);
		this.init = init;
		this.end = end;
		this.body = body;
	}

	@Override
	public List<ASTNode> getChildren() {
		List<ASTNode> children = new ArrayList<ASTNode>();
		children.add(init);
		children.add(end);
		children.add(body);
		return children;
	}

	public AssignNode getInit() {
		return init;
	}

	public void setInit(AssignNode init) {
		this.init = init;
	}

	public ExpressionNode getEnd() {
		return end;
	}

	public void setEnd(ExpressionNode end) {
		this.end = end;
	}

	public BlockNode getBody() {
		return body;
	}

	public void setBody(BlockNode body) {
		this.body = body;
	}

	@Override
	public void visit(ASTNodeVisitor v) { v.accept(this); }
}
