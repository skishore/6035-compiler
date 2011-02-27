package edu.mit.compilers.le02.ast;

import java.util.ArrayList;
import java.util.List;

import edu.mit.compilers.le02.SourceLocation;
import edu.mit.compilers.le02.symboltable.SymbolTable;

public final class BlockNode extends StatementNode {
  private List<VarDeclNode> decls;
  private List<StatementNode> statements;
  private SymbolTable localSymbolTable;

  public BlockNode(SourceLocation sl,
                   List<VarDeclNode> decls, List<StatementNode> statements) {
    super(sl);
    this.decls = decls;
    this.statements = statements;
  }

  @Override
  public List<ASTNode> getChildren() {
    List<ASTNode> children = new ArrayList<ASTNode>();
    children.addAll(decls);
    children.addAll(statements);
    return children;
  }

  public List<VarDeclNode> getDecls() {
    return decls;
  }

  public void setDecls(List<VarDeclNode> decls) {
    this.decls = decls;
  }

  public List<StatementNode> getStatements() {
    return statements;
  }

  public void setStatements(List<StatementNode> statements) {
    this.statements = statements;
  }

  public void setLocalSymbolTable(SymbolTable locals) {
    localSymbolTable = locals;
  }

  public SymbolTable getLocalSymbolTable() {
    return localSymbolTable;
  }


  @Override
  public <T> T accept(ASTNodeVisitor<T> v) {
    return v.visit(this);
  }

}
