package edu.mit.compilers.le02.ir;

import java.io.DataInputStream;

import edu.mit.compilers.le02.DecafType;
import edu.mit.compilers.le02.StreamUtil;
import edu.mit.compilers.le02.ast.ASTNode;
import edu.mit.compilers.le02.ast.ArrayDeclNode;
import edu.mit.compilers.le02.ast.ArrayLocationNode;
import edu.mit.compilers.le02.ast.AssignNode;
import edu.mit.compilers.le02.ast.BlockNode;
import edu.mit.compilers.le02.ast.BoolOpNode;
import edu.mit.compilers.le02.ast.BooleanNode;
import edu.mit.compilers.le02.ast.ClassNode;
import edu.mit.compilers.le02.ast.IntNode;
import edu.mit.compilers.le02.ast.MathOpNode;
import edu.mit.compilers.le02.ast.MinusNode;
import edu.mit.compilers.le02.ast.NotNode;
import edu.mit.compilers.le02.ast.ScalarLocationNode;
import edu.mit.compilers.le02.ast.SourceLocation;
import edu.mit.compilers.le02.ast.BoolOpNode.BoolOp;
import edu.mit.compilers.le02.ast.MathOpNode.MathOp;
import edu.mit.compilers.le02.grammar.DecafParser;
import edu.mit.compilers.le02.grammar.DecafParserTokenTypes;
import edu.mit.compilers.le02.grammar.DecafScanner;
import antlr.CommonAST;
import antlr.RecognitionException;
import antlr.TokenStreamException;
import antlr.collections.AST;
import junit.framework.TestCase;


public class IrGeneratorTest extends TestCase {

  /**
   * Verifies that stripping of empty Term' nodes is happening correctly.
   */
  public void testProcessTermStripEmptyPrimes() {
    AST parent = new CommonAST();
    AST firstChild = generateEmptyTerm();
    AST secondChild = generateEmptyTermF();
    AST emptyPrime = generateEmptyTermPrime();
    AST emptyPrime2 = generateEmptyTermPrime();

    AST number = new CommonAST();
    number.initialize(DecafParserTokenTypes.INTEGER_LITERAL, "Int");

    AST theAnswer = new CommonAST();
    theAnswer.initialize(DecafParserTokenTypes.INT, "42");

    number.addChild(theAnswer);
    secondChild.addChild(number);
    firstChild.addChild(secondChild);
    firstChild.addChild(emptyPrime2);

    parent.addChild(firstChild);
    parent.addChild(emptyPrime);

    IrGenerator gen = IrGenerator.getInstance();
    try {
      ASTNode node = gen.processTerm(parent, new SourceLocation(parent));
      assertNotNull(node);
      assert(node instanceof IntNode);
      assertEquals(42, ((IntNode)node).getValue());
    } catch (IrException ie) {
      // We should never get here.
      assert(false);
    }
  }

  /**
   * Verifies that arithmetic order of operations is happening correctly.
   */
  public void testOrderOfOperations() {
    DecafScanner parse_lexer =
      new DecafScanner(new DataInputStream(
        StreamUtil.createInputStream(ARITHMETIC_PROGRAM)));
    final DecafParser parser = new DecafParser(parse_lexer);
    try {
      parser.program();
      ASTNode root = IrGenerator.generateIR(parser.getAST());

      // Take a nice walk through the tree.
      assert(root instanceof ClassNode);
      ClassNode classNode = (ClassNode)root;

      assertEquals(2, classNode.getFields().size());
      assertEquals("a", classNode.getFields().get(0).getName());
      assertEquals(DecafType.INT, classNode.getFields().get(0).getType());
      assertEquals("b", classNode.getFields().get(1).getName());
      assertEquals(DecafType.BOOLEAN_ARRAY,
                   classNode.getFields().get(1).getType());
      assertEquals(10,
                   ((ArrayDeclNode)classNode.getFields().get(1)).getLength());

      assertEquals(1, classNode.getMethods().size());
      assertEquals("main", classNode.getMethods().get(0).getName());
      assertEquals(DecafType.VOID, classNode.getMethods().get(0).getType());
      assertTrue(classNode.getMethods().get(0).getParams().isEmpty());

      BlockNode body = classNode.getMethods().get(0).getBody();
      assertTrue(body.getDecls().isEmpty());
      assertEquals(2, body.getStatements().size());

      AssignNode firstAssignment = (AssignNode)body.getStatements().get(0);
      assertTrue(firstAssignment.getLoc() instanceof ScalarLocationNode);
      assertEquals("a", firstAssignment.getLoc().getName());

      AssignNode secondAssignment = (AssignNode)body.getStatements().get(1);
      assertTrue(secondAssignment.getLoc() instanceof ArrayLocationNode);
      assertEquals("b", secondAssignment.getLoc().getName());
      assertEquals(new IntNode(null, 1),
                   ((ArrayLocationNode)secondAssignment.getLoc()).getIndex());

      assertEquals(
        new MathOpNode(null,
          new MathOpNode(null,
            new MathOpNode(null,
              new MathOpNode(null,
                new IntNode(null, 1), new IntNode(null, 2),
                MathOp.ADD),
              new MathOpNode(null,
                new IntNode(null, 3),new IntNode(null, 4),
                  MathOp.MULTIPLY),
              MathOp.ADD),
              new MathOpNode(null,
                new IntNode(null, 5), new IntNode(null, 10),
                  MathOp.DIVIDE),
              MathOp.ADD),
            new MathOpNode(null,
                new IntNode(null, 2),
                new IntNode(null, 3),
                MathOp.MODULO),
            MathOp.SUBTRACT),
        firstAssignment.getValue());
      assertEquals(
        new BoolOpNode(null,
          new NotNode(null, new BooleanNode(null, true)),
          new BoolOpNode(null,
            new MathOpNode(null,
              new MathOpNode(null,
                new IntNode(null, 1),
                new IntNode(null, 2),
                MathOp.ADD),
              new MathOpNode(null,
                new MathOpNode(null,
                  new IntNode(null, 3),
                  new MathOpNode(null,
                    new IntNode(null, 4),
                    new IntNode(null, 5),
                    MathOp.ADD),
                  MathOp.MULTIPLY),
                new IntNode(null, 10),
                MathOp.DIVIDE),
              MathOp.ADD),
            new MathOpNode(null,
              new MinusNode(null,
                new IntNode(null, 2)),
              new IntNode(null, 3),
              MathOp.MODULO),
            BoolOp.EQ),
          BoolOp.OR),
        secondAssignment.getValue());
    } catch (IrException ie) {
      assert(false);
    } catch (RecognitionException e) {
      assert(false);
    } catch (TokenStreamException e) {
      assert(false);
    }
  }

  private static String ARITHMETIC_PROGRAM = "class Program {\n" +
  "  int a;\n" +
  "  boolean b[10];\n" +
  "  void main() {\n" +
  "    a = 1 + 2 + 3 * 4 + 5 / 10 - 2 % 3;\n" +
  "    b[1] = !true || 1 + 2 + 3 * (4 + 5) / 10 == -2 % 3;\n" +
  "  }\n" +
  "}\n";

  private AST generateEmptyTerm() {
    AST ast = new CommonAST();
    ast.initialize(DecafParserTokenTypes.TERM, "Term");
    return ast;
  }

  private AST generateEmptyTermF() {
    AST ast = new CommonAST();
    ast.initialize(DecafParserTokenTypes.TERMF, "Term");
    return ast;
  }

  private AST generateEmptyTermPrime() {
    AST ast = new CommonAST();
    ast.initialize(DecafParserTokenTypes.TERM_PRIME, "Term'");
    return ast;
  }
}
