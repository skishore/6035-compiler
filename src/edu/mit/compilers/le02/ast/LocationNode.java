package edu.mit.compilers.le02.ast;

import edu.mit.compilers.le02.DecafType;


public abstract class LocationNode extends ASTNode {
	protected DecafType type;
	protected String name;

	public LocationNode(String filename, int line, int col, DecafType type, String name) {
		super(filename, line, col);
		this.type = type;
		this.name = name;
	}

	public DecafType getType() {
		return type;
	}

	public String getName() {
		return name;
	}
}
