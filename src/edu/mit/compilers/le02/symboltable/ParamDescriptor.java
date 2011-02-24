package edu.mit.compilers.le02.symboltable;

import edu.mit.compilers.le02.DecafType;

public class ParamDescriptor extends TypedDescriptor{
	
	public ParamDescriptor(SymbolTable parent, String id, DecafType type){
		super(parent, id, type);
	}

	public String toString(){
		return this.getType().toString();
	}

}
