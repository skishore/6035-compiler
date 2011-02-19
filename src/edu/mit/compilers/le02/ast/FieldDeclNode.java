package edu.mit.compilers.le02.ast;

import edu.mit.compilers.le02.DecafType;

public abstract class FieldDeclNode extends DeclNode {

  public FieldDeclNode(SourceLocation sl, DecafType type, String id) {
    super(sl, type, id);
  }

}
