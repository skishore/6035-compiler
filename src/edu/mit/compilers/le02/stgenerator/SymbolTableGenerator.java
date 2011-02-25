package edu.mit.compilers.le02.stgenerator;

import edu.mit.compilers.le02.ast.ASTNode;
import edu.mit.compilers.le02.ast.ASTNodeVisitor;
import edu.mit.compilers.le02.ast.ClassNode;
import edu.mit.compilers.le02.ast.FieldDeclNode;
import edu.mit.compilers.le02.ast.MethodDeclNode;
import edu.mit.compilers.le02.ast.VarDeclNode;
import edu.mit.compilers.le02.symboltable.ClassDescriptor;
import edu.mit.compilers.le02.symboltable.Descriptor;
import edu.mit.compilers.le02.symboltable.FieldDescriptor;
import edu.mit.compilers.le02.symboltable.LocalDescriptor;
import edu.mit.compilers.le02.symboltable.MethodDescriptor;
import edu.mit.compilers.le02.symboltable.ParamDescriptor;
import edu.mit.compilers.le02.symboltable.SymbolTable;

public class SymbolTableGenerator extends ASTNodeVisitor<Descriptor> {
  private SymbolTable currParent = null;
  private boolean isField = false;
  private boolean isParam = false;

  /** Holds the SymbolTableVisitor singleton. */
  private static SymbolTableGenerator instance;

  /**
   * Retrieves the SymbolTableVisitor singleton, creating if necessary.
   */
  public static SymbolTableGenerator getInstance() {
    if (instance == null) {
      instance = new SymbolTableGenerator();
    }
    return instance;
  }

  /**
   * Generates an symbol table based on an input IR.
   */
  public static SymbolTable generateSymbolTable(ASTNode root)
      throws SymbolTableException {
    assert(root instanceof ClassNode);
    return getInstance().createClassST((ClassNode)root);
  }

  /**
   * Converts a ClassNode into a SymbolTable.  The root of all ASTNode trees 
   * must be a ClassNode, otherwise we should throw an exception
   * 
   * @param root The root of our AST tree
   * @return SymbolTable The expanded SymbolTable
   */
  public SymbolTable createClassST(ClassNode root) throws SymbolTableException {
    SymbolTable st = new SymbolTable(null);
    currParent = st;
    st.put(root.getName(), this.accept(root));
    return st;
  }
  
  public Descriptor accept(ClassNode node) {
    SymbolTable parent = currParent;

    // Create and fill fieldSymbolTable
    SymbolTable fieldSymbolTable = new SymbolTable(parent);
    currParent = fieldSymbolTable;
    isField = true;
    for (FieldDeclNode n : node.getFields()) {
      fieldSymbolTable.put(n.getName(), n.accept(this));
    }
    isField = false;

    // Create and fill methodSymbolTable
    SymbolTable methodSymbolTable = new SymbolTable(fieldSymbolTable);
    currParent = methodSymbolTable;
    for (MethodDeclNode m : node.getMethods()) {
      methodSymbolTable.put(m.getName(), m.accept(this));
    }
    
    currParent = parent;
    return new ClassDescriptor(parent, node.getName(), fieldSymbolTable, 
                               methodSymbolTable);
  }

  public Descriptor accept(MethodDeclNode node) {
    SymbolTable parent = currParent;

    // Create and fill paramSymbolTable
    SymbolTable paramSymbolTable = new SymbolTable(parent);
    currParent = paramSymbolTable;
    isParam = true;
    for (VarDeclNode v : node.getParams()) {
      paramSymbolTable.put(v.getName(), v.accept(this));
    }

    // Create and fill localSymbolTable
    SymbolTable localSymbolTable = new SymbolTable(paramSymbolTable);
    currParent = localSymbolTable;
    isParam = false;
    for (VarDeclNode v : node.getBody().getDecls()) {
      localSymbolTable.put(v.getName(), v.accept(this));
    }
    
    currParent = parent;
    return new MethodDescriptor(parent, node.getName(), node.getType(),
                                localSymbolTable, paramSymbolTable, 
                                node.getBody());
  }
  
  public Descriptor accept(FieldDeclNode node) {
    return new FieldDescriptor(currParent, node.getName(), node.getType());
  }

  public Descriptor accept(VarDeclNode node) {
    if (isField) {
      return new FieldDescriptor(currParent, node.getName(), node.getType());
    }
    else if (isParam) {
      return new ParamDescriptor(currParent, node.getName(), node.getType());
    }
    else {
      return new LocalDescriptor(currParent, node.getName(), node.getType());
    }
  }
}
