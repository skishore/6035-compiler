package edu.mit.compilers.le02.ast;

import java.util.Arrays;


public final class MathOpNode extends BinaryOpNode {
  private MathOp op;

  public MathOpNode(SourceLocation sl, ExpressionNode left, ExpressionNode right, MathOp op) {
    super(sl, left, right);
    this.op = op;
  }

  public MathOp getOp() {
    return op;
  }

  @Override
  public String toString() {
    return op + " " + Arrays.toString(getChildren().toArray());
  }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof MathOpNode)) {
      return false;
    }
    MathOpNode other = (MathOpNode)o;
    return (left.equals(other.left) &&
            right.equals(other.right) &&
            op.equals(other.getOp()));
  }

  public enum MathOp {
    ADD("+"),
    SUBTRACT("-"),
    MULTIPLY("*"),
    DIVIDE("/"),
    MODULO("%");
    private String disp;
    private MathOp(String display) {
      disp = display;
    }
    @Override
    public String toString() {
      return disp;
    }
  }

  @Override
  public void visit(ASTNodeVisitor v) { v.accept(this); }
}
