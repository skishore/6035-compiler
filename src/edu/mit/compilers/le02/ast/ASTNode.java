package edu.mit.compilers.le02.ast;

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

	
	abstract public List<ASTNode> getChildren();

}
