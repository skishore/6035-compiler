package edu.mit.compilers.le02.ast;

import java.util.Collections;
import java.util.List;

import edu.mit.compilers.le02.DecafType;
import edu.mit.compilers.le02.SourceLocation;

public final class VarDeclNode extends FieldDeclNode {

  public VarDeclNode(SourceLocation sl, DecafType type,
                     String id) {
    super(sl, type, id);
  }

  @Override
  public List<ASTNode> getChildren() {
    return Collections.emptyList();
  }

  @Override
  public String toString() {
    return super.toString() + " " + type + " " + name;
  }

  @Override
  public <T> T accept(ASTNodeVisitor<T> v) {
    return v.visit(this);
  }
}
