package edu.mit.compilers.le02.ast;

import edu.mit.compilers.le02.DecafType;

public abstract class ExpressionNode extends ASTNode {

	public ExpressionNode(String filename, int line, int col) {
		super(filename, line, col);
	}
	
	abstract public DecafType getType();

}
