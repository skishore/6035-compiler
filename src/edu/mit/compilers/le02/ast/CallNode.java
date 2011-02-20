package edu.mit.compilers.le02.ast;

import edu.mit.compilers.le02.SourceLocation;


public abstract class CallNode extends ExpressionNode {

  public CallNode(SourceLocation sl) {
    super(sl);
  }

}
