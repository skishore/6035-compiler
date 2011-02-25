package edu.mit.compilers.le02.ast;

import edu.mit.compilers.le02.DecafType;
import edu.mit.compilers.le02.SourceLocation;


public final class MinusNode extends UnaryOpNode {

  public MinusNode(SourceLocation sl, ExpressionNode expr) {
    super(sl, expr);
  }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof MinusNode)) {
      return false;
    }
    MinusNode other = (MinusNode)o;
    return expr.equals(other.expr);
  }

  @Override
  public <T> T accept(ASTNodeVisitor<T> v) { 
    return v.visit(this); 
  }

  @Override
  public DecafType getType() {
    return DecafType.INT;
  }
}
