package edu.mit.compilers.le02.ast;

import java.util.ArrayList;
import java.util.List;

public final class ClassNode extends ASTNode {
	protected List<FieldDeclNode> fields;
	protected List<MethodDeclNode> methods;

	public ClassNode(SourceLocation sl) {
		super(sl);
	}

	public ClassNode(SourceLocation sl, List<FieldDeclNode> fields, List<MethodDeclNode> methods) {
		super(sl);
		this.fields = fields;
		this.methods = methods;
	}

	@Override
	public List<ASTNode> getChildren() {
		List<ASTNode> children = new ArrayList<ASTNode>();
		children.addAll(fields);
		children.addAll(methods);
		return children;
	}

	public List<FieldDeclNode> getFields() {
		return fields;
	}

	public void setFields(List<FieldDeclNode> fields) {
		this.fields = fields;
	}

	public List<MethodDeclNode> getMethods() {
		return methods;
	}

	public void setMethods(List<MethodDeclNode> methods) {
		this.methods = methods;
	}

}
