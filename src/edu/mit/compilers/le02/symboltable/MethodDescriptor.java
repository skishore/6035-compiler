package edu.mit.compilers.le02.symboltable;

import edu.mit.compilers.le02.DecafType;
import edu.mit.compilers.le02.ast.BlockNode;
import java.util.List;

public class MethodDescriptor extends TypedDescriptor {
  BlockNode code;
  SymbolTable paramSymbolTable;
  List<String> params;

  public MethodDescriptor(SymbolTable parent, String id, DecafType type,
                          SymbolTable paramSymbolTable, List<String> params,
                          BlockNode node) {
    super(parent, id, type);

    this.code = node;
    this.paramSymbolTable = paramSymbolTable;
        this.params = params;
  }

  @Override
  public String toString() {
    return "[" + this.paramSymbolTable.toString() +
      "],[" + this.code.getLocalSymbolTable().toString() + "]";

  }

  public BlockNode getCode() {
    return code;
  }

  public List<String> getParams() {
    return params;
  }

  public SymbolTable getParamSymbolTable() {
    return paramSymbolTable;
  }
}
