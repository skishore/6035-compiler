package edu.mit.compilers.le02;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

/**
 * Utility methods for constructing input streams from Strings to ease unit
 * testing.
 *
 * @author Liz Fong <lizfong@mit.edu>
 */
public class StreamUtil {
  public static InputStream createInputStream(String s) {
    return createInputStream(s, "US-ASCII");
  }
  private static InputStream createInputStream(String s, String charset) {
    try {
      return new ByteArrayInputStream(s.getBytes(charset));
    } catch (UnsupportedEncodingException uee) {
      // Guaranteed not to happen with US-ASCII.
      return new ByteArrayInputStream(new byte[] {});
    }
  }
}
