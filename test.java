import java.io.*;
import java.util.*;

public class test {

   public static void main(String[] args) {
   
      String sent = "<s> This is a sentence . </s>";
      
      String[] test = sent.split(" ");
      
      for (int i = 0; i < test.length - 1; i ++)
         System.out.println(test[i] + " " + test[i + 1]);
      
   }

}