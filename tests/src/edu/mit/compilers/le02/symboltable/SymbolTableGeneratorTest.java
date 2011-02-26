package edu.mit.compilers.le02.symboltable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import junit.framework.TestCase;
import edu.mit.compilers.le02.DecafType;
import edu.mit.compilers.le02.Util;
import edu.mit.compilers.le02.ast.ArrayDeclNode;
import edu.mit.compilers.le02.ast.AssignNode;
import edu.mit.compilers.le02.ast.BlockNode;
import edu.mit.compilers.le02.ast.BreakNode;
import edu.mit.compilers.le02.ast.ClassNode;
import edu.mit.compilers.le02.ast.FieldDeclNode;
import edu.mit.compilers.le02.ast.ForNode;
import edu.mit.compilers.le02.ast.IntNode;
import edu.mit.compilers.le02.ast.LocationNode;
import edu.mit.compilers.le02.ast.MethodDeclNode;
import edu.mit.compilers.le02.ast.ScalarLocationNode;
import edu.mit.compilers.le02.ast.StatementNode;
import edu.mit.compilers.le02.ast.VarDeclNode;
import edu.mit.compilers.le02.stgenerator.SymbolTableException;
import edu.mit.compilers.le02.stgenerator.SymbolTableGenerator;


public class SymbolTableGeneratorTest extends TestCase {
  private SymbolTableGenerator stg;
  
  public void setUp() {
    
    stg = SymbolTableGenerator.getInstance();
  }

  public void testFields() {
    ClassNode ast = 
      new ClassNode(null, "Program",
        Util.makeList(
          new ArrayDeclNode(null, DecafType.INT_ARRAY, "array1", 10),
          new VarDeclNode(null, DecafType.INT, "var1"),
          new ArrayDeclNode(null, DecafType.BOOLEAN_ARRAY, "array2", 20),
          new VarDeclNode(null, DecafType.BOOLEAN, "var2")),        
        Util.emptyList(MethodDeclNode.class)
      );
    
    SymbolTable st = null;
    try {
      st = SymbolTableGenerator.generateSymbolTable(ast);
    } catch (SymbolTableException e) {
      e.printStackTrace();
      assertTrue(false);
    }
    
    assertNotNull(st);
    ClassDescriptor cd = (ClassDescriptor) st.get("Program", null);
    assertNotNull(cd);
    SymbolTable fst = cd.getFieldSymbolTable();
    assertNotNull(fst);
       
    FieldDescriptor fd = (FieldDescriptor) fst.get("array1", null);
    checkTypedDesc(fd, "array1", DecafType.INT_ARRAY);
    fd = (FieldDescriptor) fst.get("array2", null);
    checkTypedDesc(fd, "array2", DecafType.BOOLEAN_ARRAY);
    fd = (FieldDescriptor) fst.get("var1", null);
    checkTypedDesc(fd, "var1", DecafType.INT);
    fd = (FieldDescriptor) fst.get("var2", null);
    checkTypedDesc(fd, "var2", DecafType.BOOLEAN);
  }
 
  
  public void testMethod() {
    ClassNode ast = 
      new ClassNode(null, "Program",
        Util.emptyList(FieldDeclNode.class),
        Util.makeList(
          new MethodDeclNode(null, DecafType.INT, "method1",
            Util.makeList(
              new VarDeclNode(null, DecafType.INT, "param1"),
              new VarDeclNode(null, DecafType.BOOLEAN, "param2")),
            new BlockNode(null, 
              Util.makeList(
                new VarDeclNode(null, DecafType.BOOLEAN, "local1"),
                new VarDeclNode(null, DecafType.INT, "local2")),
              Util.emptyList(StatementNode.class)
            )
          )
        )
      );
    
    SymbolTable st = null;
    try {
      st = SymbolTableGenerator.generateSymbolTable(ast);
    } catch (SymbolTableException e) {
      e.printStackTrace();
      assertTrue(false);
    }
    
    assertNotNull(st);
    ClassDescriptor cd = (ClassDescriptor) st.get("Program", null);
    assertNotNull(cd);
    SymbolTable mst = cd.getMethodSymbolTable();
    assertNotNull(mst);
    
    MethodDescriptor md = (MethodDescriptor) mst.get("method1", null);
    checkTypedDesc(md, "method1", DecafType.INT);
    SymbolTable pst = md.getParamSymbolTable();
    SymbolTable lst = md.getCode().getLocalSymbolTable();
    assertNotNull(pst);
    assertNotNull(lst);
    
    
    ParamDescriptor pd = (ParamDescriptor) pst.get("param1", null);
    checkTypedDesc(pd, "param1", DecafType.INT);
    
    pd = (ParamDescriptor) pst.get("param2", null);
    checkTypedDesc(pd, "param2", DecafType.BOOLEAN);
    
    LocalDescriptor ld = (LocalDescriptor) lst.get("local1", null);
    checkTypedDesc(ld, "local1", DecafType.BOOLEAN);
    ld = (LocalDescriptor) lst.get("local2", null);
    checkTypedDesc(ld, "local2", DecafType.INT);
  }
  

  public void testBlocks() {
    ClassNode ast = 
      new ClassNode(null, "Program",
        Util.emptyList(FieldDeclNode.class),
        Util.makeList(
          new MethodDeclNode(null, DecafType.INT, "method1",
            Util.emptyList(VarDeclNode.class),
            new BlockNode(null, 
              Util.makeList(
                new VarDeclNode(null, DecafType.BOOLEAN, "local1"),
                new VarDeclNode(null, DecafType.INT, "local2")),
              Util.makeList(
                new BreakNode(null),
                new BlockNode(null,
                  Util.makeList(
                    new VarDeclNode(null, DecafType.BOOLEAN, "local3"),
                    new VarDeclNode(null, DecafType.INT, "local4")
                  ),
                  Util.emptyList(StatementNode.class)
                ),
                new ForNode(null,
                  new AssignNode(null,
                    new ScalarLocationNode(null, "forVar"),
                    new IntNode(null, 1)
                  ),
                  new IntNode(null, 2),
                  new BlockNode(null,
                    Util.emptyList(VarDeclNode.class),
                    Util.emptyList(StatementNode.class)
                  )
                )
              )
            )
          )
        )
      );
    
    SymbolTable st = null;
    try {
      st = SymbolTableGenerator.generateSymbolTable(ast);
    } catch (SymbolTableException e) {
      e.printStackTrace();
      assertTrue(false);
    }
    
    assertNotNull(st);
    ClassDescriptor cd = (ClassDescriptor) st.get("Program", null);
    assertNotNull(cd);
    SymbolTable mst = cd.getMethodSymbolTable();
    assertNotNull(mst);
    
    MethodDescriptor md = (MethodDescriptor) mst.get("method1", null);
    checkTypedDesc(md, "method1", DecafType.INT);
    BlockNode node = (BlockNode) md.getCode().getStatements().get(1);
    SymbolTable lst = node.getLocalSymbolTable();
    assertNotNull(lst);
   
    LocalDescriptor ld = (LocalDescriptor) lst.get("local1", null);
    checkTypedDesc(ld, "local1", DecafType.BOOLEAN);
    ld = (LocalDescriptor) lst.get("local2", null);
    checkTypedDesc(ld, "local2", DecafType.INT);
    ld = (LocalDescriptor) lst.get("local3", null);
    checkTypedDesc(ld, "local3", DecafType.BOOLEAN);
    ld = (LocalDescriptor) lst.get("local4", null);
    checkTypedDesc(ld, "local4", DecafType.INT);

    ForNode f = (ForNode) md.getCode().getStatements().get(2);
    lst = f.getBody().getLocalSymbolTable();
    assertNotNull(lst);
    ld = (LocalDescriptor) lst.get("forVar", null);
    checkTypedDesc(ld, "forVar", DecafType.INT);
    
  }
  
  private void checkTypedDesc(TypedDescriptor td, String id, DecafType type) {
    assertNotNull(td);
    assertEquals(id, td.getId());
    assertEquals(type, td.getType());
  }
  
}
