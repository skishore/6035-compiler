package edu.mit.compilers.le02.ast;

import java.util.ArrayList;
import java.util.List;

public final class SystemCallNode extends CallNode {
  private StringNode funcName;
  private List<SyscallArgNode> args;

  public SystemCallNode(SourceLocation sl,
                        StringNode name, List<SyscallArgNode> args) {
    super(sl);
    this.funcName = name;
    this.args = args;
  }

  @Override
  public List<ASTNode> getChildren() {
    List<ASTNode> children = new ArrayList<ASTNode>();
    children.add(funcName);
    children.addAll(args);
    return children;
  }

  public StringNode getFuncName() {
    return funcName;
  }

  public List<SyscallArgNode> getArgs() {
    return args;
  }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof SystemCallNode)) {
      return false;
    }
    SystemCallNode other = (SystemCallNode)o;
    return (funcName.equals(other.getFuncName()) &&
            args.equals(other.getArgs()));
  }

  @Override
  public void visit(ASTNodeVisitor v) { v.accept(this); };
}
