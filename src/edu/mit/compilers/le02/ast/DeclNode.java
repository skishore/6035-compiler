package edu.mit.compilers.le02.ast;

import edu.mit.compilers.le02.DecafType;
import edu.mit.compilers.le02.SourceLocation;

public abstract class DeclNode extends ASTNode {
  protected DecafType type;
  protected String name;

  public DeclNode(SourceLocation sl, DecafType type, String id) {
    super(sl);
    this.type = type;
    this.name = id;
  }

  public DecafType getType() {
    return type;
  }

  public String getName() {
    return name;
  }

}
