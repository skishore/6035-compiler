package edu.mit.compilers.le02.symboltable;


public abstract class Descriptor {
	private SymbolTable parent;
	private String id;
	
	public Descriptor(SymbolTable parent, String id){
		this.parent = parent;
		this.id = id;
	}
	
	public SymbolTable getParent(){
		return this.parent;
	}
	
	public String getId(){
		return this.id;
	}
	
	public abstract String toString();

	
}
