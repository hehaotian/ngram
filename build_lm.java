import java.io.*;
import java.util.*;

public class build_lm {

   public static void main(String[] args) throws IOException {
   
      Scanner file = new Scanner(new File(args[0]));
      PrintStream ps = new PrintStream(args[1]);
      
      Langmodel lm = new Langmodel(file);
      lm.print_data(ps);
      lm.print_ngram(ps);
      lm.print_end(ps);
   }
}