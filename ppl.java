import java.io.*;
import java.util.*;

public class ppl {

   public static Map<String, String[]> data = new HashMap<String, String[]>();
   public static Map<String, String[]> grams1 = new HashMap<String, String[]>();
   public static Map<String, String[]> grams2 = new HashMap<String, String[]>();
   public static Map<String, String[]> grams3 = new HashMap<String, String[]>();

   public static void main(String[] args) throws IOException {
   
      Scanner lm = new Scanner(new File(args[0]));
      double l1 = Double.parseDouble(args[1]);
      double l2 = Double.parseDouble(args[2]);
      double l3 = Double.parseDouble(args[3]);
      Scanner test = new Scanner(new File(args[4]));
      PrintStream ps = new PrintStream(args[5]);
      
      String line = "";
      String keyname = "";
      int token1 = 0;
      int token2 = 0;
      int token3 = 0;
      
      while (lm.hasNextLine()) {
         if (lm.hasNextLine()) {
            line = lm.nextLine();
            if (line.equals("\\data\\")) {
               String gr1 = lm.nextLine();
               String gr2 = lm.nextLine();
               String gr3 = lm.nextLine();
               data.put("gr1", gr1.split(" "));
               data.put("gr2", gr2.split(" "));
               data.put("gr3", gr3.split(" "));
            } else if (line.split(" ").length == 4) {
               keyname = line.split(" ")[3];
               String[] value = {line.split(" ")[0], line.split(" ")[1], line.split(" ")[2]};
               grams1.put(keyname, value);
            } else if (line.split(" ").length == 5) {
               keyname = line.split(" ")[3] + " " + line.split(" ")[4];
               String[] value = {line.split(" ")[0], line.split(" ")[1], line.split(" ")[2]};
               grams2.put(keyname, value);
            } else if (line.split(" ").length == 6) {
               keyname = line.split(" ")[3] + " " + line.split(" ")[4] + " " + line.split(" ")[5];
               String[] value = {line.split(" ")[0], line.split(" ")[1], line.split(" ")[2]};
               grams3.put(keyname, value);
            }
         }
      }
      
      ps.println();
      
      int sent_num = 0;
      int word_num = 0;
      
      while (test.hasNextLine()) {
         if (test.hasNextLine()) {
            sent_num ++;
            line = "<s> " + test.nextLine() + " </s>";
            String[] tokens = line.split(" ");
            int sent_word = tokens.length - 2;
            word_num += sent_word;
            System.out.println("Sent #" + sent_num + ": " + line);
            calculate(tokens, sent_num);
            System.out.println("1 sentence, " + sent_word + "words");
            System.out.println();
         }
      }
      System.out.println();
      System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
      System.out.println("sent_num=" + sent_num + " word_num=" + word_num);
   }
   
   public static void calculate(String[] tokens, int sent_num) {
   
      
   
   }
}