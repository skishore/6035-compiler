package edu.mit.compilers.le02.ast;

import java.util.ArrayList;
import java.util.List;

import edu.mit.compilers.le02.DecafType;
import edu.mit.compilers.le02.SourceLocation;
import edu.mit.compilers.le02.symboltable.MethodDescriptor;


public final class MethodCallNode extends CallNode {
  private String name;
  private List<ExpressionNode> args;
  private MethodDescriptor desc;

  public MethodCallNode(SourceLocation sl,
                        String name, List<ExpressionNode> args) {
    super(sl);
    this.name = name;
    this.args = args;
  }

  @Override
  public List<ASTNode> getChildren() {
    List<ASTNode> children = new ArrayList<ASTNode>();
    children.addAll(args);
    return children;
  }

  public List<ExpressionNode> getArgs() {
    return args;
  }

  public void setArgs(List<ExpressionNode> args) {
    this.args = args;
  }

  public String getName() {
    return name;
  }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof MethodCallNode)) {
      return false;
    }
    MethodCallNode other = (MethodCallNode)o;
    return (name.equals(other.getName()) &&
            args.equals(other.getArgs()));
  }

  public MethodDescriptor getDesc() {
    return desc;
  }

  public void setDesc(MethodDescriptor desc) {
    this.desc = desc;
  }

  @Override
  public <T> T accept(ASTNodeVisitor<T> v) { 
    return v.visit(this); 
  }

  @Override
  public DecafType getType() {
    return desc.getType();
  }
}
