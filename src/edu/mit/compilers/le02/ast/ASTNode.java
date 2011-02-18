package edu.mit.compilers.le02.ast;

import java.util.List;

public abstract class ASTNode {
	protected String filename;
	protected int line;
	protected int col;
	
	public ASTNode(String filename, int line, int col) {
		this.filename = filename;
		this.line = line;
		this.col = col;
	}

	public String getFilename() {
		return filename;
	}

	public int getLine() {
		return line;
	}

	public int getCol() {
		return col;
	}
	
	abstract public List<ASTNode> getChildren();

}
