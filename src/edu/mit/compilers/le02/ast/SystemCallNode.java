package edu.mit.compilers.le02.ast;

import java.util.ArrayList;
import java.util.List;

public final class SystemCallNode extends CallNode {
  private StringNode funcName;
  private List<SyscallArgNode> args;

  public SystemCallNode(SourceLocation sl) {
    super(sl);
  }

  public SystemCallNode(SourceLocation sl, StringNode name, List<SyscallArgNode> args) {
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

}
