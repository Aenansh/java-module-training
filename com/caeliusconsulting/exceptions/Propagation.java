package com.caeliusconsulting.exceptions;

import java.io.FileNotFoundException;
import java.io.FileReader;

public class Propagation {
  public static FileReader func() throws FileNotFoundException {
    FileReader fr = new FileReader("abc.txt");
    return fr;
  }

  public static void main(String[] args) {
    try {
      FileReader fr = func();
      fr.close();
    } catch (FileNotFoundException e) {
      System.out.println(e.getMessage());
    }
  }
}