package edu.mit.compilers.le02.symboltable;

import edu.mit.compilers.le02.DecafType;

public class TypeDescriptor extends Descriptor{
	
	public DecafType type;
	
	public TypeDescriptor(SymbolTable parent, String id, DecafType type){
		super(parent, id);
		this.type = type;
	}
	
	public DecafType getType(){
		return this.type;
	}
	
	@Override
	public String toString(){
		return this.type.toString();
	}

}
