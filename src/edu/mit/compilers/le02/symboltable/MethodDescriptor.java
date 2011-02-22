package edu.mit.compilers.le02.symboltable;

import edu.mit.compilers.le02.ast.ASTNode;
import edu.mit.compilers.le02.ast.MethodDeclNode;

public class MethodDescriptor extends Descriptor{
	ASTNode code;
	SymbolTable localSymbolTable;
	SymbolTable paramSymbolTable;
	
	public MethodDescriptor(SymbolTable parent, String id, SymbolTable localSymbolTable, 
			SymbolTable paramSymbolTable, MethodDeclNode node){
		super(parent,id);
		
		this.code = node;
		this.paramSymbolTable = paramSymbolTable;
		this.localSymbolTable = localSymbolTable;
	}
	
	public String toString(){
		return "["+ this.paramSymbolTable.toString() +"],["+
		this.localSymbolTable.toString()+"]";
		
	}
}
