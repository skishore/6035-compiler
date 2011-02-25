package edu.mit.compilers.le02.ast;

import java.util.ArrayList;
import java.util.List;

import edu.mit.compilers.le02.SourceLocation;
import edu.mit.compilers.le02.symboltable.ClassDescriptor;


public final class ClassNode extends ASTNode {
  private String name;
  protected List<FieldDeclNode> fields;
  protected List<MethodDeclNode> methods;
  protected ClassDescriptor desc;

  public ClassNode(SourceLocation sl,
                   String name, List<FieldDeclNode> fields,
                   List<MethodDeclNode> methods) {
    super(sl);
    this.name = name;
    this.fields = fields;
    this.methods = methods;
  }

  public String getName() {
    return name;
  }

  @Override
  public List<ASTNode> getChildren() {
    List<ASTNode> children = new ArrayList<ASTNode>();
    children.addAll(fields);
    children.addAll(methods);
    return children;
  }

  public List<FieldDeclNode> getFields() {
    return fields;
  }

  public void setFields(List<FieldDeclNode> fields) {
    this.fields = fields;
  }

  public List<MethodDeclNode> getMethods() {
    return methods;
  }

  public void setMethods(List<MethodDeclNode> methods) {
    this.methods = methods;
  }
  
  public ClassDescriptor getDesc() {
    return desc;
  }

  public void setDesc(ClassDescriptor desc) {
    this.desc = desc;
  }
  
  @Override
  public <T> T accept(ASTNodeVisitor<T> v) { 
    return v.visit(this); 
  }
}
