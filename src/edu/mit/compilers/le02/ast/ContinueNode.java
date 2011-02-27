package edu.mit.compilers.le02.ast;

import java.util.Collections;
import java.util.List;

import edu.mit.compilers.le02.SourceLocation;

public final class ContinueNode extends StatementNode {

  public ContinueNode(SourceLocation sl) {
    super(sl);
  }

  @Override
  public List<ASTNode> getChildren() {
    return Collections.emptyList();
  }

  @Override
  public <T> T accept(ASTNodeVisitor<T> v) {
    return v.visit(this);
  }
}
