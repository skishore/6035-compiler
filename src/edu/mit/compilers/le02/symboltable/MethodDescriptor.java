package edu.mit.compilers.le02.symboltable;

import edu.mit.compilers.le02.DecafType;
import edu.mit.compilers.le02.ast.ASTNode;
import edu.mit.compilers.le02.ast.BlockNode;

public class MethodDescriptor extends TypedDescriptor {
	ASTNode code;
	SymbolTable localSymbolTable;
	SymbolTable paramSymbolTable;
	
	public MethodDescriptor(SymbolTable parent, String id, DecafType type,
	                        SymbolTable localSymbolTable, 
	                        SymbolTable paramSymbolTable, BlockNode node) {
		super(parent, id, type);
		
		this.code = node;
		this.paramSymbolTable = paramSymbolTable;
		this.localSymbolTable = localSymbolTable;
	}
	
	@Override
	public String toString(){
		return "[" + this.paramSymbolTable.toString() + 
			"],[" + this.localSymbolTable.toString() + "]";
		
	}
}
