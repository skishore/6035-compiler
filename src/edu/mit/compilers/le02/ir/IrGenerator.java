package edu.mit.compilers.le02.ir;

import edu.mit.compilers.le02.grammar.DecafParserTokenTypes;
import edu.mit.compilers.le02.ir.nodes.IrClassDecl;
import edu.mit.compilers.le02.ir.nodes.IrNode;
import edu.mit.compilers.le02.ir.nodes.IrFieldDecl;
import edu.mit.compilers.le02.ir.nodes.IrList;
import edu.mit.compilers.le02.ir.nodes.IrMethodDecl;
import antlr.collections.AST;

public class IrGenerator {

  /** Holds the IRGenerator singleton. */
  private static IrGenerator instance;

  /**
   * Retrieves the IRGenerator singleton, creating if necessary.
   */
  public static IrGenerator getInstance() {
    if (instance == null) {
      instance = new IrGenerator();
    }
    return instance;
  }

  /**
   * Generates an intermediate representation based on an input AST.
   */
  public static IrNode GenerateIR(AST root) throws IrException {
    return getInstance().visit(root);
  }

  @SuppressWarnings("unchecked")
  public IrNode visit(AST node) throws IrException {
    AST current_node = node;
    if (current_node == null) {
      return null;
    }
    switch (current_node.getType()) {
     case DecafParserTokenTypes.PROGRAM:
      // #([PROGRAM,"Prog"], n, f_accum, m_accum);
      AST name_ast = current_node.getFirstChild();
      assert(name_ast != null);
      assert(name_ast.getType() == DecafParserTokenTypes.ID);

      AST fields_ast = name_ast.getNextSibling();
      AST methods_ast = fields_ast.getNextSibling();
      assert(methods_ast.getNextSibling() == null);

      String name = name_ast.getText();
      IrList<IrFieldDecl> fields =
        (IrList<IrFieldDecl>)visit(fields_ast);
      IrList<IrMethodDecl> methods =
        (IrList<IrMethodDecl>)visit(methods_ast);
      return new IrClassDecl(name, fields, methods);
     case DecafParserTokenTypes.PROGRAM_FIELDS:
      return new IrNode() {};
     case DecafParserTokenTypes.PROGRAM_METHODS:
      return new IrNode() {};
     case DecafParserTokenTypes.CALL:
      return new IrNode() {};
     case DecafParserTokenTypes.CALL_ARGS:
      return new IrNode() {};
     case DecafParserTokenTypes.CALL_ARG:
      return new IrNode() {};
     case DecafParserTokenTypes.DECLARATION_ARGS:
      return new IrNode() {};
     case DecafParserTokenTypes.DECLARATION_ARG:
      return new IrNode() {};
     case DecafParserTokenTypes.EXPR:
      return new IrNode() {};
     case DecafParserTokenTypes.TERM:
      return new IrNode() {};
     case DecafParserTokenTypes.TERM_PRIME:
      return new IrNode() {};
     case DecafParserTokenTypes.STMT:
      return new IrNode() {};
     case DecafParserTokenTypes.BLOCK:
      return new IrNode() {};
     case DecafParserTokenTypes.BLOCK_VARS:
      return new IrNode() {};
     case DecafParserTokenTypes.BLOCK_STMTS:
      return new IrNode() {};
     case DecafParserTokenTypes.ASSIGNMENT:
      return new IrNode() {};
     case DecafParserTokenTypes.METHOD_DECL:
      return new IrNode() {};
     case DecafParserTokenTypes.LOCAL_VAR_DECL:
      return new IrNode() {};
     case DecafParserTokenTypes.FIELD_DECL:
      return new IrNode() {};
     case DecafParserTokenTypes.TYPES:
      return new IrNode() {};
     case DecafParserTokenTypes.LOCATION:
      return new IrNode() {};
     case DecafParserTokenTypes.BOOLEAN_LITERAL:
      return new IrNode() {};
     case DecafParserTokenTypes.CHAR_LITERAL:
      return new IrNode() {};
     case DecafParserTokenTypes.INTEGER_LITERAL:
      return new IrNode() {};
     case DecafParserTokenTypes.STRING_LITERAL:
      return new IrNode() {};
     default:
      throw new IrException();
    }
  }
}
