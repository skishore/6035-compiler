package edu.mit.compilers.le02.symboltable;


public class ClassDescriptor extends Descriptor {
  private SymbolTable symbolTable;

  public ClassDescriptor(SymbolTable parent, String id, SymbolTable st) {
    super(parent, id);

    this.symbolTable = st;
  }

  public SymbolTable getSymbolTable() {
    return symbolTable;
  }

  @Override
  public String toString() {
    return "[" + symbolTable.toString() + "]";
  }


}
