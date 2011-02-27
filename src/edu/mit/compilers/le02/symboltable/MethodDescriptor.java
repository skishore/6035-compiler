package edu.mit.compilers.le02.symboltable;

import edu.mit.compilers.le02.DecafType;
import edu.mit.compilers.le02.ast.BlockNode;
import java.util.List;

public class MethodDescriptor extends TypedDescriptor {
  BlockNode code;
  SymbolTable symbolTable;
  List<String> params;

  public MethodDescriptor(SymbolTable parent, String id, DecafType type,
                          SymbolTable symbolTable, List<String> params,
                          BlockNode node) {
    super(parent, id, type);

    this.code = node;
    this.symbolTable = symbolTable;
        this.params = params;
  }

  @Override
  public String toString() {
    return "[" + this.symbolTable.toString() +
      "],[" + this.code.getLocalSymbolTable().toString() + "]";

  }

  public BlockNode getCode() {
    return code;
  }

  public List<String> getParams() {
    return params;
  }

  public SymbolTable getSymbolTable() {
    return symbolTable;
  }
}
