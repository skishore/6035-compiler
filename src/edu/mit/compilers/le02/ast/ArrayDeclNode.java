package edu.mit.compilers.le02.ast;

import java.util.Collections;
import java.util.List;

import edu.mit.compilers.le02.DecafType;

public final class ArrayDeclNode extends FieldDeclNode {
  private int length;

  public ArrayDeclNode(SourceLocation sl, DecafType type, String id, int length) {
    super(sl, type, id);
    this.length = length;
  }

  @Override
  public List<ASTNode> getChildren() {
    return Collections.emptyList();
  }

  public int getLength() {
    return length;
  }

  @Override
  public <T> T accept(ASTNodeVisitor<T> v) { return v.visit(this); }
}
