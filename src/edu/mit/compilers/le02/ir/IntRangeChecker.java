package edu.mit.compilers.le02.ir;

import edu.mit.compilers.le02.ErrorReporting;
import edu.mit.compilers.le02.ast.ASTNodeVisitor;
import edu.mit.compilers.le02.ast.IntNode;

public class IntRangeChecker extends ASTNodeVisitor<Boolean> {
  public Boolean visit(IntNode intNode) {
    if (intNode.isInverted() &&
        intNode.getValue() == -Integer.MIN_VALUE) {
      ErrorReporting.reportError(new IrException(intNode.getSourceLoc(),
        "Integer literal out of range"));
    }
    return true;
  }
}
