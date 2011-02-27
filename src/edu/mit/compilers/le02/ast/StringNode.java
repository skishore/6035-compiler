package edu.mit.compilers.le02.ast;

import java.util.Collections;
import java.util.List;

import edu.mit.compilers.le02.SourceLocation;
import edu.mit.compilers.le02.Util;

public final class StringNode extends ASTNode {
  private String value;

  public StringNode(SourceLocation sl, String value) {
    super(sl);
    this.value = value;
  }

  @Override
  public List<ASTNode> getChildren() {
    return Collections.emptyList();
  }

  public String getValue() {
    return value;
  }

  @Override
  public String toString() {
    return "\"" + Util.escape(value) + "\"";
  }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof StringNode)) {
      return false;
    }
    return value.equals(((StringNode)o).getValue());
  }

  @Override
  public <T> T accept(ASTNodeVisitor<T> v) {
    return v.visit(this);
  }
}
