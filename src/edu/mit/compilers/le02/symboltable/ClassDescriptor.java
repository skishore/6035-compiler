package edu.mit.compilers.le02.symboltable;


public class ClassDescriptor extends Descriptor{
	private SymbolTable methodSymbolTable;
	private SymbolTable fieldSymbolTable;
	
	public ClassDescriptor(SymbolTable parent, String id, SymbolTable fields, SymbolTable methods){
		super(parent,id);
	
		this.fieldSymbolTable = fields;
		this.methodSymbolTable = methods;
	}
	
	public SymbolTable getMethodSymbolTable(){
		return this.methodSymbolTable;
	}
	
	public SymbolTable getFieldSymbolTable(){
		return this.fieldSymbolTable;
	}
	
	public String toString(){
		return "["+this.fieldSymbolTable.toString()+"],["+
		this.methodSymbolTable.toString()+"]";
	}


}
