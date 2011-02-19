package edu.mit.compilers.le02.ir;

import java.util.List;

import edu.mit.compilers.le02.DecafType;
import edu.mit.compilers.le02.ast.ArrayDeclNode;
import edu.mit.compilers.le02.ast.BooleanNode;
import edu.mit.compilers.le02.ast.CharNode;
import edu.mit.compilers.le02.ast.ClassNode;
import edu.mit.compilers.le02.ast.FieldDeclNode;
import edu.mit.compilers.le02.ast.IntNode;
import edu.mit.compilers.le02.ast.MethodDeclNode;
import edu.mit.compilers.le02.ast.NodeList;
import edu.mit.compilers.le02.ast.SourceLocation;
import edu.mit.compilers.le02.ast.StringNode;
import edu.mit.compilers.le02.ast.VarDeclNode;
import edu.mit.compilers.le02.grammar.DecafParserTokenTypes;
import edu.mit.compilers.le02.ast.ASTNode;
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

  public ASTNode visit(AST node) throws IrException {
    if (node == null) {
      return null;
    }
    
    SourceLocation loc = new SourceLocation(node);
    switch (node.getType()) {
     case DecafParserTokenTypes.PROGRAM:
      // #([PROGRAM,"Prog"], n, f_accum, m_accum);
      AST name_ast = node.getFirstChild();
      assert(name_ast != null);
      assert(name_ast.getType() == DecafParserTokenTypes.ID);

      AST fields_ast = name_ast.getNextSibling();
      AST methods_ast = fields_ast.getNextSibling();
      assert(methods_ast.getNextSibling() == null);

      // Name is unused in our AST, but if we needed it, we would use:
      // String name = name_ast.getText();
      @SuppressWarnings("unchecked")
      List<FieldDeclNode> fields =
        (List<FieldDeclNode>)visit(fields_ast);
      @SuppressWarnings("unchecked")
      List<MethodDeclNode> methods =
        (List<MethodDeclNode>)visit(methods_ast);
      return new ClassNode(loc, fields, methods);
     case DecafParserTokenTypes.PROGRAM_FIELDS:
      AST pfield = node.getFirstChild();
      NodeList<FieldDeclNode> fdlist = new NodeList<FieldDeclNode>(loc);
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
      // #([FIELD_DECL,"FieldDecl"], type, name, size)
      AST field_type_ast = node.getFirstChild();
      AST field_name_ast = field_type_ast.getNextSibling();
      AST field_size_ast = field_type_ast.getNextSibling();

      String field_name = field_name_ast.getText();
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
          throw new IrException(loc, "Invalid scalar type");
        }
        int array_size = ((IntNode)visit(field_size_ast)).getValue();
        return new ArrayDeclNode(
          loc, field_type, field_name, array_size);
      } else {
        switch (field_type_ast.getType()) {
         case DecafParserTokenTypes.TK_boolean:
          field_type = DecafType.BOOLEAN;
          break;
         case DecafParserTokenTypes.TK_int:
          field_type = DecafType.INT;
          break;
         default:
          throw new IrException(loc, "Invalid array type");
        }
        return new VarDeclNode(loc, field_type, field_name);
      }
     case DecafParserTokenTypes.LOCATION:
      return null;
     case DecafParserTokenTypes.BOOLEAN_LITERAL:
      return new BooleanNode(loc, node.getFirstChild().getText() == "true");
     case DecafParserTokenTypes.CHAR_LITERAL:
      return new CharNode(loc, node.getFirstChild().getText().charAt(0));
     case DecafParserTokenTypes.INTEGER_LITERAL:
      String raw_int = node.getFirstChild().getText();
      String HEX_PREFIX = "0x";
      int HEX_RADIX = 16;
      if (raw_int.startsWith(HEX_PREFIX)) {
        try {
          return new IntNode(loc, Integer.parseInt(
            raw_int.substring(HEX_PREFIX.length()), HEX_RADIX));
        } catch (NumberFormatException nfe) {
          // Since 0x[a-fA-F0-9]+ must parse, this means we were out of range.
          throw new IrException(loc, "Out of range");
        }
      } else {
        try {
          return new IntNode(loc, Integer.parseInt(raw_int));
        } catch (NumberFormatException nfe) {
          // Since [0-9]+ must parse, this means we were out of range.
          throw new IrException(loc, "Out of range value: " + raw_int);
        }
      }
     case DecafParserTokenTypes.STRING_LITERAL:
      return new StringNode(loc, node.getFirstChild().getText());
     default:
      throw new IrException(loc, "Unexpected token: " + node.getText());
    }
  }
}
