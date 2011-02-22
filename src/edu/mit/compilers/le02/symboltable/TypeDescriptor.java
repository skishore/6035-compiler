package edu.mit.compilers.le02.symboltable;

import edu.mit.compilers.le02.DecafType;

public class TypeDescriptor extends Descriptor{
	
	public DecafType TYPE;
	
	public TypeDescriptor(SymbolTable parent, String id, DecafType type){
		super(parent,id);
		this.TYPE = type;
	}
	
	public DecafType getType(){
		return this.TYPE;
	}
	
	public String toString(){
		return this.TYPE.toString();
	}

}
