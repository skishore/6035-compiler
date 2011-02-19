package edu.mit.compilers.le02.ast;

import java.util.Arrays;
import java.util.List;

public abstract class ASTNode {
	protected SourceLocation sourceLoc;

	public ASTNode() {
	}

	public ASTNode(SourceLocation sl) {
		this.sourceLoc = sl;
	}

	public SourceLocation getSourceLoc() {
		return sourceLoc;
	}

	@Override
	public String toString() {
	  return Arrays.toString(getChildren().toArray());
	}
	
	abstract public List<ASTNode> getChildren();
}
