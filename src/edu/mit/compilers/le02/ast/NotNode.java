package edu.mit.compilers.le02.ast;

import edu.mit.compilers.le02.DecafType;
import edu.mit.compilers.le02.SourceLocation;


public class NotNode extends UnaryOpNode {

  public NotNode(SourceLocation sl, ExpressionNode expr) {
    super(sl, expr);
  }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof NotNode)) {
      return false;
    }
    NotNode other = (NotNode)o;
    return expr.equals(other.expr);
  }

  @Override
  public String toString() {
    return "!";
  }

  @Override
  public <T> T accept(ASTNodeVisitor<T> v) { 
    return v.visit(this); 
  }

  @Override
  public DecafType getType() {
    return DecafType.BOOLEAN;
  }
}
