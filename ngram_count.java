import java.io.*;
import java.util.*;

public class ngram_count {
   
   public static void main(String[] args) throws IOException {
      
      Scanner file = new Scanner(new File(args[0]));
      PrintStream ps = new PrintStream(args[1]);
      
      Ngram ng = new Ngram(file);
      ng.print_count(ps);
      
   }     
}