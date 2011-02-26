package edu.mit.compilers.le02.ir;

import edu.mit.compilers.le02.ErrorReporting;
import edu.mit.compilers.le02.ast.ASTNodeVisitor;
import edu.mit.compilers.le02.ast.IntNode;

/**
 * Performs any remaining integer range checking not detected at time of
 * initialization of the IntNode.
 * The only corner case this needs to catch is setting a value 2^31 without
 * subsequently negating it.
 */
public class IntRangeChecker extends ASTNodeVisitor<Boolean> {
  @Override
  public Boolean visit(IntNode intNode) {
    if (intNode.isInverted() &&
        intNode.getValue() == -Integer.MIN_VALUE) {
      ErrorReporting.reportError(new IrException(intNode.getSourceLoc(),
        "Integer literal out of range"));
      return false;
    }
    return true;
  }
}
