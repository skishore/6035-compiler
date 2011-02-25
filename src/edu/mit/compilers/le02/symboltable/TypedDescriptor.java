package edu.mit.compilers.le02.symboltable;

import edu.mit.compilers.le02.DecafType;

public abstract class TypedDescriptor extends Descriptor {
  private DecafType type;
	
	
	public TypedDescriptor(SymbolTable parent, String id, DecafType type) {
		super(parent, id);
    this.type = type;
	}

  public DecafType getType() {
    return type;
  }
  
  @Override
  public String toString() {
		return this.getType().toString();
	}

}
