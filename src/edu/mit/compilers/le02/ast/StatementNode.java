package edu.mit.compilers.le02.ast;

import edu.mit.compilers.le02.SourceLocation;


public abstract class StatementNode extends ASTNode {

  public StatementNode(SourceLocation sl) {
    super(sl);
  }

}
