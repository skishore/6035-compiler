// Copyright (c) 2011 Liz Fong <lizfong@mit.edu>
// All rights reserved.

package edu.mit.compilers.le02;

import edu.mit.compilers.le02.Main;
import junit.framework.TestCase;

/**
 * Tests for the {@link Main} compiler class.
 * @author Liz Fong <lizfong@mit.edu>
 */
public class MainTest extends TestCase {

  /**
   * Smoketest for scanner.
   */
  public void testSmokeScanner() {
    assertTrue(Main.runScanner(StreamUtil.createInputStream(
      "foo bar if else ifelse")));
    assertFalse(Main.runScanner(StreamUtil.createInputStream(
      "'\\d'")));
    assertTrue(Main.runScanner(StreamUtil.createInputStream(
      "'\\n'")));
  }

  /**
   * Smoketest for parser.
   */
  public void testSmokeParser() {
    assertTrue(Main.runParser(StreamUtil.createInputStream(GOOD_PROGRAM)));
    assertFalse(Main.runParser(StreamUtil.createInputStream(
      "class Program {\n" +
      "  int a\n" +
      "  void main() {\n" +
      "    a = a + 1;\n" +
      "  }\n" +
      "}\n")));
  }

  /**
   * Smoketest for IR builder.
   */
  public void testSmokeIrBuilder() {
    assertTrue(Main.generateIR(StreamUtil.createInputStream(GOOD_PROGRAM)));
  }

  private static String GOOD_PROGRAM = "class Program {\n" +
    "  int a;\n" +
    "  void main() {\n" +
    "    a = a + 1;\n" +
    "  }\n" +
    "}\n";
}
