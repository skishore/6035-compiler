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

  public void visit(ASTNodeVisitor v) { v.accept(this); }
}
