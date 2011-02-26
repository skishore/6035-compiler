package edu.mit.compilers.le02.ast;

public class AstPrettyPrinter extends ASTNodeVisitor<Object> {
  private static int indent = 1;

  @Override
  protected void defaultBehavior(ASTNode node) {
    System.out.println(
      String.format("%1$#" + indent + "s", "") + node.toString());
    indent++;
    super.defaultBehavior(node);
    indent--;
  }
}
