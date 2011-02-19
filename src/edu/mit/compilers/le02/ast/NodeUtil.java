package edu.mit.compilers.le02.ast;

import java.util.ArrayList;
import java.util.List;

public class NodeUtil {
  public static List<ASTNode> makeChildren(ASTNode... nodes) {
    List<ASTNode> children = new ArrayList<ASTNode>();
    for (ASTNode node : nodes) {
      children.add(node);
    }
    return children;
  }
}
