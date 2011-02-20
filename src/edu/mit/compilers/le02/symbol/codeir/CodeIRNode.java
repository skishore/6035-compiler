package edu.mit.compilers.le02.symbol.codeir;

import java.util.List;

public abstract class CodeIRNode {
	
	abstract public List<CodeIRNode> getChildren();
}
