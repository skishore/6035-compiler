package edu.mit.compilers.le02;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Util {

  public static <T> List<T> makeList(T... args) {
    ArrayList<T> list = new ArrayList<T>();
    for (T t : args) {
      list.add(t);
    }
    return list;
  }

  public static <T> List<T> emptyList(Class<T> T) {
    List<T> list = Collections.emptyList(); 
    return list;
  }
}
