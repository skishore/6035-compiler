package edu.mit.compilers.le02.ast;

import edu.mit.compilers.le02.DecafType;
import edu.mit.compilers.le02.SourceLocation;


public abstract class ExpressionNode extends ASTNode {

  public ExpressionNode(SourceLocation sl) {
    super(sl);
  }

  abstract public DecafType getType();

}
