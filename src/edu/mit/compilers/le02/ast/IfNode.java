package edu.mit.compilers.le02.ast;

import java.util.ArrayList;
import java.util.List;

public final class IfNode extends StatementNode {
	private ExpressionNode condition;
	private BlockNode thenBlock;
	private BlockNode elseBlock;
	private boolean hasElse;

	public IfNode(SourceLocation sl, ExpressionNode condition, BlockNode then) {
		super(sl);
		this.condition = condition;
		this.thenBlock = then;
		this.hasElse = false;
	}

	public IfNode(SourceLocation sl, ExpressionNode condition, BlockNode thenBlock, BlockNode elseBlock) {
		super(sl);
		this.condition = condition;
		this.thenBlock = thenBlock;
		this.elseBlock = elseBlock;
		this.hasElse = true;
	}

	@Override
	public List<ASTNode> getChildren() {
		List<ASTNode> children = new ArrayList<ASTNode>();
		children.add(condition);
		children.add(thenBlock);
		
		if (hasElse) {
			children.add(elseBlock);
		}

		return children;
	}

	public ExpressionNode getCondition() {
		return condition;
	}

	public void setCondition(ExpressionNode condition) {
		this.condition = condition;
	}

	public BlockNode getThenBlock() {
		return thenBlock;
	}

	public void setThenBlock(BlockNode thenBlock) {
		this.thenBlock = thenBlock;
	}

	public BlockNode getElseBlock() {
		return elseBlock;
	}

	public void setElseBlock(BlockNode elseBlock) {
		this.elseBlock = elseBlock;
	}

	public boolean hasElse() {
		return hasElse;
	}

}
