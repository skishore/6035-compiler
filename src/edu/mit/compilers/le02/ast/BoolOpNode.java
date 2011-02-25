package edu.mit.compilers.le02.ast;

import java.util.Arrays;

import edu.mit.compilers.le02.DecafType;


public final class BoolOpNode extends BinaryOpNode {
  private BoolOp op;

  public BoolOpNode(SourceLocation sl,
                    ExpressionNode left, ExpressionNode right, BoolOp op) {
    super(sl, left, right);
    this.op = op;
  }

  public BoolOp getOp() {
    return op;
  }

  @Override
  public String toString() {
    return op + " " + Arrays.toString(getChildren().toArray());
  }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof BoolOpNode)) {
      return false;
    }
    BoolOpNode other = (BoolOpNode)o;
    return (left.equals(other.left) &&
            right.equals(other.right) &&
            op.equals(other.getOp()));
  }

  public enum BoolOp {
    LT("<"),
    GT(">"),
    LE("<="),
    GE(">="),
    EQ("=="),
    NEQ("!="),
    AND("&&"),
    OR("||");
    private String disp;
    private BoolOp(String display) {
      disp = display;
    }
    @Override
    public String toString() {
      return disp;
    }
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
