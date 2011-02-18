package edu.mit.compilers.le02.ir;

import edu.mit.compilers.le02.grammar.DecafParserTokenTypes;
import antlr.collections.AST;

public class IRGenerator {

  /** Holds the IRGenerator singleton. */
  private static IRGenerator instance;

  /**
   * Retrieves the IRGenerator singleton, creating if necessary.
   */
  public static IRGenerator getInstance() {
    if (instance == null) {
      instance = new IRGenerator();
    }
    return instance;
  }

  /**
   * Generates an intermediate representation based on an input AST.
   */
  public static IrElement GenerateIR(AST root) throws IRException {
    return getInstance().visit(root);
  }

  @SuppressWarnings("unchecked")
  public IrElement visit(AST node) throws IRException {
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
      return new IrElement() {};
     case DecafParserTokenTypes.PROGRAM_METHODS:
      return new IrElement() {};
     case DecafParserTokenTypes.CALL:
      return new IrElement() {};
     case DecafParserTokenTypes.CALL_ARGS:
      return new IrElement() {};
     case DecafParserTokenTypes.CALL_ARG:
      return new IrElement() {};
     case DecafParserTokenTypes.DECLARATION_ARGS:
      return new IrElement() {};
     case DecafParserTokenTypes.DECLARATION_ARG:
      return new IrElement() {};
     case DecafParserTokenTypes.EXPR:
      return new IrElement() {};
     case DecafParserTokenTypes.TERM:
      return new IrElement() {};
     case DecafParserTokenTypes.TERM_PRIME:
      return new IrElement() {};
     case DecafParserTokenTypes.STMT:
      return new IrElement() {};
     case DecafParserTokenTypes.BLOCK:
      return new IrElement() {};
     case DecafParserTokenTypes.BLOCK_VARS:
      return new IrElement() {};
     case DecafParserTokenTypes.BLOCK_STMTS:
      return new IrElement() {};
     case DecafParserTokenTypes.ASSIGNMENT:
      return new IrElement() {};
     case DecafParserTokenTypes.METHOD_DECL:
      return new IrElement() {};
     case DecafParserTokenTypes.LOCAL_VAR_DECL:
      return new IrElement() {};
     case DecafParserTokenTypes.FIELD_DECL:
      return new IrElement() {};
     case DecafParserTokenTypes.TYPES:
      return new IrElement() {};
     case DecafParserTokenTypes.LOCATION:
      return new IrElement() {};
     case DecafParserTokenTypes.BOOLEAN_LITERAL:
      return new IrElement() {};
     case DecafParserTokenTypes.CHAR_LITERAL:
      return new IrElement() {};
     case DecafParserTokenTypes.INTEGER_LITERAL:
      return new IrElement() {};
     case DecafParserTokenTypes.STRING_LITERAL:
      return new IrElement() {};
     default:
      throw new IRException();
    }
  }
}
