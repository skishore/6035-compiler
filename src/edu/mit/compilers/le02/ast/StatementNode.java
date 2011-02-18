package edu.mit.compilers.le02.ast;


public abstract class StatementNode extends ASTNode {

	public StatementNode(String filename, int line, int col) {
		super(filename, line, col);
	}

}
