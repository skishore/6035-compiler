package edu.mit.compilers.le02.ast;

import edu.mit.compilers.le02.DecafType;
import edu.mit.compilers.le02.SourceLocation;
import edu.mit.compilers.le02.symboltable.TypedDescriptor;



public abstract class LocationNode extends ASTNode {
  protected String name;
  protected TypedDescriptor desc;

  /**
   * Initializes a LocationNode with no DecafType.
   * Suitable for use at time of conversion from rough antlr tree to AST.
   */
  public LocationNode(SourceLocation sl, String name) {
    super(sl);
    this.name = name;
  }

  public DecafType getType() {
    return (desc == null) ? null : desc.getType();
  }

  public String getName() {
    return name;
  }

  public TypedDescriptor getDesc() {
    return desc;
  }

  public void setDesc(TypedDescriptor desc) {
    this.desc = desc;
  }
}
