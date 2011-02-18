package edu.mit.compilers.le02.ast;

import edu.mit.compilers.le02.DecafType;

public abstract class DeclNode extends ASTNode {
	protected DecafType type;
	protected String name;

	public DeclNode(String filename, int line, int col, DecafType type, String id) {
		super(filename, line, col);
		this.type = type;
		this.name = id;
	}

	public DecafType getType() {
		return type;
	}

	public String getName() {
		return name;
	}

}
