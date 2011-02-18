package edu.mit.compilers.le02.ast;

import edu.mit.compilers.le02.DecafType;

public class NotNode extends UnaryOpNode {

	public NotNode(String filename, int line, int col) {
		super(filename, line, col);
	}

	@Override
	public DecafType getType() {
		return DecafType.BOOLEAN;
	}

}
