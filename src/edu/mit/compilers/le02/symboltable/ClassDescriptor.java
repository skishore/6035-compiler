package edu.mit.compilers.le02.symboltable;


public class ClassDescriptor extends Descriptor {
	private SymbolTable methodSymbolTable;
	private SymbolTable fieldSymbolTable;
	
	public ClassDescriptor(SymbolTable parent, String id, SymbolTable fields, SymbolTable methods){
		super(parent, id);
	
		this.fieldSymbolTable = fields;
		this.methodSymbolTable = methods;
	}
	
	public SymbolTable getMethodSymbolTable(){
		return methodSymbolTable;
	}
	
	public SymbolTable getFieldSymbolTable(){
		return fieldSymbolTable;
	}
	
	@Override
	public String toString(){
		return "[" + fieldSymbolTable.toString() + 
			"],[" +methodSymbolTable.toString() + "]";
	}


}
