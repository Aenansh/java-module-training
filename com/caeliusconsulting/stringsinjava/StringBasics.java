package com.caeliusconsulting.stringsinjava;

public class StringBasics {
  public static void main(String[] args) {
    String s1 = "hello"; 
    System.out.println(s1);

    /*
    Strings in java are immutable objects of the class String which is an
    extension of Object class and an implementation of CharSequence.
    String class can't be extended / inherited as it is declared as a final class.
    */

    String s2 = new String("world");
    String s3 = new String("world");
    System.out.println(s2);
    
    s2 = "Vasu";
    System.out.println(s2);
    System.out.println(s3);
  } 
}