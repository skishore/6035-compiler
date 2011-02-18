package edu.mit.compilers.le02.ast;

import edu.mit.compilers.le02.DecafType;

public final class MinusNode extends UnaryOpNode {

	public MinusNode(String filename, int line, int col) {
		super(filename, line, col);
	}

	@Override
	public DecafType getType() {
		return DecafType.INT;
	}

}
