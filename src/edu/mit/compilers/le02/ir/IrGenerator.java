package edu.mit.compilers.le02.ir;

import java.util.List;

import edu.mit.compilers.le02.DecafType;
import edu.mit.compilers.le02.ast.FieldDeclNode;
import edu.mit.compilers.le02.ast.MethodDeclNode;
import edu.mit.compilers.le02.ast.NodeList;
import edu.mit.compilers.le02.grammar.DecafParserTokenTypes;
import edu.mit.compilers.le02.ast.ASTNode;
import edu.mit.compilers.tools.CLI;
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
  public static ASTNode GenerateIR(AST root) throws IrException {
    return getInstance().visit(root);
  }

  @SuppressWarnings("unchecked")
  public ASTNode visit(AST node) throws IrException {
    if (node == null) {
      return null;
    }
    switch (node.getType()) {
     case DecafParserTokenTypes.PROGRAM:
      // #([PROGRAM,"Prog"], n, f_accum, m_accum);
      AST name_ast = node.getFirstChild();
      assert(name_ast != null);
      assert(name_ast.getType() == DecafParserTokenTypes.ID);

      AST fields_ast = name_ast.getNextSibling();
      AST methods_ast = fields_ast.getNextSibling();
      assert(methods_ast.getNextSibling() == null);

      String name = name_ast.getText();
      List<FieldDeclNode> fields =
        (List<FieldDeclNode>)visit(fields_ast);
      List<MethodDeclNode> methods =
        (List<MethodDeclNode>)visit(methods_ast);
      //return new ClassDeclNode(name, fields, methods);
      return null;
     case DecafParserTokenTypes.PROGRAM_FIELDS:
      AST pfield = node.getFirstChild();
      NodeList<FieldDeclNode> fdlist = new NodeList<FieldDeclNode>(
        CLI.infile, node.getLine(), node.getColumn());
      do {
        fdlist.add((FieldDeclNode)visit(pfield));
      } while (pfield.getNextSibling() != null);
      return fdlist;
     case DecafParserTokenTypes.PROGRAM_METHODS:
      return null;
     case DecafParserTokenTypes.CALL:
      return null;
     case DecafParserTokenTypes.CALL_ARGS:
      return null;
     case DecafParserTokenTypes.CALL_ARG:
      return null;
     case DecafParserTokenTypes.DECLARATION_ARGS:
      return null;
     case DecafParserTokenTypes.DECLARATION_ARG:
      return null;
     case DecafParserTokenTypes.EXPR:
      return null;
     case DecafParserTokenTypes.TERM:
      return null;
     case DecafParserTokenTypes.TERM_PRIME:
      return null;
     case DecafParserTokenTypes.STMT:
      return null;
     case DecafParserTokenTypes.BLOCK:
      return null;
     case DecafParserTokenTypes.BLOCK_VARS:
      return null;
     case DecafParserTokenTypes.BLOCK_STMTS:
      return null;
     case DecafParserTokenTypes.ASSIGNMENT:
      return null;
     case DecafParserTokenTypes.METHOD_DECL:
      return null;
     case DecafParserTokenTypes.LOCAL_VAR_DECL:
      return null;
     case DecafParserTokenTypes.FIELD_DECL:
      AST field_type_ast = node.getFirstChild();
      AST field_name_ast = field_type_ast.getNextSibling();
      AST field_size_ast = field_type_ast.getNextSibling();

      DecafType field_type;
      if (field_size_ast != null) {
        switch (field_type_ast.getType()) {
         case DecafParserTokenTypes.TK_boolean:
          field_type = DecafType.BOOLEAN_ARRAY;
          break;
         case DecafParserTokenTypes.TK_int:
          field_type = DecafType.INT_ARRAY;
          break;
         default:
          throw new IrException();
        }
      } else {
        switch (field_type_ast.getType()) {
         case DecafParserTokenTypes.TK_boolean:
          field_type = DecafType.BOOLEAN;
          break;
         case DecafParserTokenTypes.TK_int:
          field_type = DecafType.INT;
          break;
         default:
          throw new IrException();
        }
      }
      String field_name = field_name_ast.getText();
      FieldDeclNode fdn = new FieldDeclNode(
        CLI.infile, node.getLine(), node.getColumn(),
        field_type, field_name);
      return fdn;
     case DecafParserTokenTypes.TYPES:
      return null;
     case DecafParserTokenTypes.LOCATION:
      return null;
     case DecafParserTokenTypes.BOOLEAN_LITERAL:
      return null;
     case DecafParserTokenTypes.CHAR_LITERAL:
      return null;
     case DecafParserTokenTypes.INTEGER_LITERAL:
      return null;
     case DecafParserTokenTypes.STRING_LITERAL:
      return null;
     default:
      throw new IrException();
    }
  }
}
