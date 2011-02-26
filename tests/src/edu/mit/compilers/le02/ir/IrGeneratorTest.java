package edu.mit.compilers.le02.ir;

import java.io.DataInputStream;

import edu.mit.compilers.le02.DecafType;
import edu.mit.compilers.le02.ErrorReporting;
import edu.mit.compilers.le02.SourceLocation;
import edu.mit.compilers.le02.StreamUtil;
import edu.mit.compilers.le02.Util;
import edu.mit.compilers.le02.ast.ASTNode;
import edu.mit.compilers.le02.ast.ArrayDeclNode;
import edu.mit.compilers.le02.ast.ArrayLocationNode;
import edu.mit.compilers.le02.ast.AssignNode;
import edu.mit.compilers.le02.ast.BlockNode;
import edu.mit.compilers.le02.ast.BoolOpNode;
import edu.mit.compilers.le02.ast.BooleanNode;
import edu.mit.compilers.le02.ast.CharNode;
import edu.mit.compilers.le02.ast.ClassNode;
import edu.mit.compilers.le02.ast.IntNode;
import edu.mit.compilers.le02.ast.MathOpNode;
import edu.mit.compilers.le02.ast.MethodCallNode;
import edu.mit.compilers.le02.ast.NotNode;
import edu.mit.compilers.le02.ast.ScalarLocationNode;
import edu.mit.compilers.le02.ast.StringNode;
import edu.mit.compilers.le02.ast.SyscallArgNode;
import edu.mit.compilers.le02.ast.SystemCallNode;
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
   * Test that integer range checking is working correctly. (max)
   */
  public void testIntegerRangeCheckingMaxInt() {
    ErrorReporting.clearErrors();

    AST termf = generateEmptyTermF();
    AST literal = new CommonAST();
    literal.initialize(DecafParserTokenTypes.INTEGER_LITERAL, "Int");
    AST maxint = new CommonAST();
    maxint.initialize(DecafParserTokenTypes.INT, "2147483647");
    literal.addChild(maxint);
    termf.addChild(literal);

    IrGenerator gen = IrGenerator.getInstance();
    ASTNode node = gen.processTermF(termf, new SourceLocation(termf));
    assertNotNull(node);
    assertTrue(node instanceof IntNode);
    assertEquals(2147483647, ((IntNode)node).getValue());
    node.accept(new IntRangeChecker());
    assertTrue(ErrorReporting.noErrors());
  }

  /**
   * Test that integer range checking is working correctly. (min)
   */
  public void testIntegerRangeCheckingMinInt() {
    ErrorReporting.clearErrors();

    AST termf = generateEmptyTermF();
    AST minus = new CommonAST();
    minus.initialize(DecafParserTokenTypes.MINUS, "-");
    AST subtermf = generateEmptyTermF();
    AST literal = new CommonAST();
    literal.initialize(DecafParserTokenTypes.INTEGER_LITERAL, "Int");
    AST minint = new CommonAST();
    minint.initialize(DecafParserTokenTypes.INT, "2147483648");
    literal.addChild(minint);
    subtermf.addChild(literal);
    termf.addChild(minus);
    termf.addChild(subtermf);

    IrGenerator gen = IrGenerator.getInstance();
    ASTNode node = gen.processTermF(termf, new SourceLocation(minus));
    assertTrue(node instanceof IntNode);
    assertEquals(-2147483648, ((IntNode)node).getValue());
    node.accept(new IntRangeChecker());
    assertTrue(ErrorReporting.noErrors());
  }

  /**
   * Test that integer range checking is working correctly. (one above max)
   */
  public void testIntegerRangeCheckingOneAboveMaxInt() {
    ErrorReporting.clearErrors();

    AST termf = generateEmptyTermF();
    AST literal = new CommonAST();
    literal.initialize(DecafParserTokenTypes.INTEGER_LITERAL, "Int");
    AST maxint = new CommonAST();
    maxint.initialize(DecafParserTokenTypes.INT, "2147483648");
    literal.addChild(maxint);
    termf.addChild(literal);

    IrGenerator gen = IrGenerator.getInstance();
    ASTNode node = gen.processTermF(termf, new SourceLocation(termf));
    assertNotNull(node);
    assertTrue(node instanceof IntNode);
    // We cannot catch this case here; we rely upon catching it downstream
    // by looking for positive getValue() results from inverted nodes.
    assertEquals(-(-2147483648), ((IntNode)node).getValue());
    assertTrue(ErrorReporting.noErrors());
    node.accept(new IntRangeChecker());
    assertFalse(ErrorReporting.noErrors());
  }

  /**
   * Test that integer range checking is working correctly. (far above max)
   */
  public void testIntegerRangeCheckingFarAboveMaxInt() {
    ErrorReporting.clearErrors();

    AST termf = generateEmptyTermF();
    AST literal = new CommonAST();
    literal.initialize(DecafParserTokenTypes.INTEGER_LITERAL, "Int");
    AST maxint = new CommonAST();
    maxint.initialize(DecafParserTokenTypes.INT, "9147483648");
    literal.addChild(maxint);
    termf.addChild(literal);

    IrGenerator gen = IrGenerator.getInstance();
    ASTNode node = gen.processTermF(termf, new SourceLocation(termf));
    assertNotNull(node);
    assertTrue(node instanceof IntNode);
    assertEquals(Integer.MAX_VALUE, ((IntNode)node).getValue());
    assertFalse(ErrorReporting.noErrors());
    ErrorReporting.clearErrors();
    node.accept(new IntRangeChecker());
    assertTrue(ErrorReporting.noErrors());
  }

  /**
   * Test that integer range checking is working correctly. (far below min)
   */
  public void testIntegerRangeCheckingFarBelowMinInt() {
    ErrorReporting.clearErrors();

    AST termf = generateEmptyTermF();
    AST minus = new CommonAST();
    minus.initialize(DecafParserTokenTypes.MINUS, "-");
    AST subtermf = generateEmptyTermF();
    AST literal = new CommonAST();
    literal.initialize(DecafParserTokenTypes.INTEGER_LITERAL, "Int");
    AST minint = new CommonAST();
    minint.initialize(DecafParserTokenTypes.INT, "9147483648");
    literal.addChild(minint);
    subtermf.addChild(literal);
    termf.addChild(minus);
    termf.addChild(subtermf);

    IrGenerator gen = IrGenerator.getInstance();
    ASTNode node = gen.processTermF(termf, new SourceLocation(minus));
    assertNotNull(node);
    assertTrue(node instanceof IntNode);
    assertEquals(-Integer.MAX_VALUE, ((IntNode)node).getValue());
    assertFalse(ErrorReporting.noErrors());
    ErrorReporting.clearErrors();
    node.accept(new IntRangeChecker());
    assertTrue(ErrorReporting.noErrors());
  }

  /**
   * Test that escaping/unescaping of char/string literals is working.
   */
  public void testEscapeUnescape() {
    IrGenerator gen = IrGenerator.getInstance();
    ErrorReporting.clearErrors();

    System.out.println("\"foo bar \\t \\n \\\" \\\' \\\\ ack\"");
    AST escapedString = generateString(
      "\"foo bar \\t \\n \\\" \\\' \\\\ ack\"");
    StringNode stringNode = (StringNode)gen.visit(escapedString);
    assertEquals("\"foo bar \\t \\n \\\" \\' \\\\ ack\"",
      stringNode.toString());
    assertEquals("foo bar \t \n \" \' \\ ack", stringNode.getValue());

    AST tabChar = generateChar("'\\t'");
    AST newlineChar = generateChar("'\\n'");
    AST doublequoteChar = generateChar("'\\\"'");
    AST singlequoteChar = generateChar("'\\\''");
    AST slashChar = generateChar("'\\\\'");

    CharNode tabCharNode = (CharNode)gen.visit(tabChar);
    CharNode newlineCharNode = (CharNode)gen.visit(newlineChar);
    CharNode doublequoteCharNode = (CharNode)gen.visit(doublequoteChar);
    CharNode singlequoteCharNode = (CharNode)gen.visit(singlequoteChar);
    CharNode slashCharNode = (CharNode)gen.visit(slashChar);

    assertEquals('\t', tabCharNode.getValue());
    assertEquals('\n', newlineCharNode.getValue());
    assertEquals('\"', doublequoteCharNode.getValue());
    assertEquals('\'', singlequoteCharNode.getValue());
    assertEquals('\\', slashCharNode.getValue());

    assertEquals("'\\t'", tabCharNode.toString());
    assertEquals("'\\n'", newlineCharNode.toString());
    assertEquals("'\\\"'", doublequoteCharNode.toString());
    assertEquals("'\\\''", singlequoteCharNode.toString());
    assertEquals("'\\\\'", slashCharNode.toString());

    assertTrue(ErrorReporting.noErrors());
  }

  /**
   * Verifies that stripping of empty Term' nodes is happening correctly.
   */
  public void testProcessTermStripEmptyPrimes() {
    ErrorReporting.clearErrors();

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
    ASTNode node = gen.processTerm(parent, new SourceLocation(parent));
    assertNotNull(node);
    assert(node instanceof IntNode);
    assertEquals(42, ((IntNode)node).getValue());
    assert(ErrorReporting.noErrors());
  }

  /**
   * Verifies that SystemCallNodes have SyscallArgNodes as arguments but that
   * MethodCallNodes have ExpressionNodes attached.
   */
  public void testProcessCalloutAndCallArgs() {
    ErrorReporting.clearErrors();
    DecafScanner parse_lexer =
      new DecafScanner(new DataInputStream(
        StreamUtil.createInputStream(FUNCTION_PROGRAM)));
    final DecafParser parser = new DecafParser(parse_lexer);
    try {
      parser.program();
      ASTNode root = IrGenerator.generateIR(parser.getAST());

      // Take a nice walk through the tree.
      assert(root instanceof ClassNode);
      ClassNode classNode = (ClassNode)root;
      BlockNode body = classNode.getMethods().get(0).getBody();

      AssignNode firstAssignment = (AssignNode)body.getStatements().get(0);
      assertEquals(
        new SystemCallNode(null,
          new StringNode(null, "printf"), Util.makeList(
            new SyscallArgNode(null, new StringNode(null, "foo")),
            new SyscallArgNode(null, new IntNode(null, 5)),
            new SyscallArgNode(null, new BooleanNode(null, false)))),
        firstAssignment.getValue());
      AssignNode secondAssignment = (AssignNode)body.getStatements().get(1);
      assertEquals(
          new MethodCallNode(null,
            "foo", Util.makeList(
              new IntNode(null, 5),
              new BooleanNode(null, false))),
          secondAssignment.getValue());
    } catch (RecognitionException e) {
      assert(false);
    } catch (TokenStreamException e) {
      assert(false);
    }
    assert(ErrorReporting.noErrors());
  }

  /**
   * Verifies that arithmetic order of operations is happening correctly.
   */
  public void testOrderOfOperations() {
    ErrorReporting.clearErrors();
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
              new IntNode(null, -2),
              new IntNode(null, 3),
              MathOp.MODULO),
            BoolOp.EQ),
          BoolOp.OR),
        secondAssignment.getValue());
    } catch (RecognitionException e) {
      assert(false);
    } catch (TokenStreamException e) {
      assert(false);
    }
    assert(ErrorReporting.noErrors());
  }

  private static String ARITHMETIC_PROGRAM = "class Program {\n" +
  "  int a;\n" +
  "  boolean b[10];\n" +
  "  void main() {\n" +
  "    a = 1 + 2 + 3 * 4 + 5 / 10 - 2 % 3;\n" +
  "    b[1] = !true || 1 + 2 + 3 * (4 + 5) / 10 == -2 % 3;\n" +
  "  }\n" +
  "}\n";

  private static String FUNCTION_PROGRAM = "class Program {\n" +
  "  void main() {\n" +
  "    a = callout(\"printf\", \"foo\", 5, false);\n" +
  "    b = foo(5, false);\n" +
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

  private AST generateChar(String token) {
    AST container = new CommonAST();
    container.initialize(DecafParserTokenTypes.CHAR_LITERAL, "Char");
    AST ast = new CommonAST();
    ast.initialize(DecafParserTokenTypes.CHAR, token);
    container.setFirstChild(ast);
    return container;
  }

  private AST generateString(String token) {
    AST container = new CommonAST();
    container.initialize(DecafParserTokenTypes.STRING_LITERAL, "String");
    AST ast = new CommonAST();
    ast.initialize(DecafParserTokenTypes.STRING, token);
    container.setFirstChild(ast);
    return container;
  }
}
