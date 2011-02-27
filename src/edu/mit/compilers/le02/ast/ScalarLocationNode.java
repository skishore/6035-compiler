package edu.mit.compilers.le02.ast;

import java.util.Collections;
import java.util.List;

import edu.mit.compilers.le02.SourceLocation;


public final class ScalarLocationNode extends LocationNode {

  public ScalarLocationNode(SourceLocation sl, String name) {
    super(sl, name);
  }

  @Override
  public List<ASTNode> getChildren() {
    return Collections.emptyList();
  }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof ScalarLocationNode)) {
      return false;
    }
    ScalarLocationNode other = (ScalarLocationNode)o;
    return (name.equals(other.getName()) &&
            getType().equals(other.getType()));
  }

  @Override
  public String toString() {
    return super.toString() + " " + getType() + " " + name;
  }

  @Override
  public <T> T accept(ASTNodeVisitor<T> v) {
    return v.visit(this);
  }
}
