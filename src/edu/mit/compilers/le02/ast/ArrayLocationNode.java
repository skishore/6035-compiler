package edu.mit.compilers.le02.ast;

import java.util.List;


public final class ArrayLocationNode extends LocationNode {
	private ExpressionNode index;

	public ArrayLocationNode(SourceLocation sl, String name, ExpressionNode index) {
		super(sl, name);
		this.index = index;
	}

	@Override
	public List<ASTNode> getChildren() {
		return NodeUtil.makeChildren(index);
	}

	public void setIndex(ExpressionNode index) {
		this.index = index;
	}

	public ExpressionNode getIndex() {
		return index;
	}
	
	public void visit(ASTNodeVisitor v) { v.accept(this); }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof ArrayLocationNode)) {
      return false;
    }
    ArrayLocationNode other = (ArrayLocationNode)o;
    return (name.equals(other.getName()) &&
            type.equals(other.getType()) &&
            index.equals(other.getIndex()));
  }
}
