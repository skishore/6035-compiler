package edu.mit.compilers.le02.ast;

import edu.mit.compilers.le02.DecafType;


public abstract class LocationNode extends ASTNode {
  protected DecafType type;
  protected String name;

  /**
   * Initializes a LocationNode with no DecafType.
   * Suitable for use at time of conversion from rough antlr tree to AST.
   */
  public LocationNode(SourceLocation sl, String name) {
    super(sl);
    this.name = name;
  }

  public DecafType getType() {
    return type;
  }

  /**
   * Sets the type of the location after it has already been created.
   * At the time of conversion from rough antlr tree to AST, we don't yet
   * know enough information to populate this in the constructor.
   */
  public void setType(DecafType type) {
    this.type = type;
  }

  public String getName() {
    return name;
  }

}
