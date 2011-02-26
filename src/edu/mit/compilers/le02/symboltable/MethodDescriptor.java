package edu.mit.compilers.le02.symboltable;

import edu.mit.compilers.le02.DecafType;
import edu.mit.compilers.le02.ast.ASTNode;
import edu.mit.compilers.le02.ast.BlockNode;

public class MethodDescriptor extends TypedDescriptor {
	BlockNode code;
  SymbolTable paramSymbolTable;
	
	public MethodDescriptor(SymbolTable parent, String id, DecafType type,
	                        SymbolTable paramSymbolTable, 
	                        BlockNode node) {
		super(parent, id, type);
		
		this.code = node;
		this.paramSymbolTable = paramSymbolTable;
	}
	
	@Override
	public String toString(){
		return "[" + this.paramSymbolTable.toString() + 
			"],[" + this.code.getLocalSymbolTable().toString() + "]";
		
	}

  public BlockNode getCode() {
    return code;
  }

  public SymbolTable getParamSymbolTable() {
    return paramSymbolTable;
  }
}
