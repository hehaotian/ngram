import java.io.*;
import java.util.*;

public class ppl {

   public static Map<String, String[]> data = new HashMap<String, String[]>();
   public static Map<String, String[]> grams1 = new HashMap<String, String[]>();
   public static Map<String, String[]> grams2 = new HashMap<String, String[]>();
   public static Map<String, String[]> grams3 = new HashMap<String, String[]>();
   public static double l1;
   public static double l2;
   public static double l3;

   public static void main(String[] args) throws IOException {
   
      Scanner lm = new Scanner(new File(args[0]));
      l1 = Double.parseDouble(args[1]);
      l2 = Double.parseDouble(args[2]);
      l3 = Double.parseDouble(args[3]);
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
            System.out.println("1 sentence, " + sent_word + " words");
            System.out.println();
         }
      }
      System.out.println();
      System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
      System.out.println("sent_num=" + sent_num + " word_num=" + word_num);
   }
   
   public static void calculate(String[] tokens, int sent_num) {
      
      ArrayList<Double> probs = new ArrayList<Double>();
      ArrayList<Double> lgprobs = new ArrayList<Double>();
      
      String token = "";
      String check = "";
      
      double prob = 1.0;
      
      // First bigrams:
      token = tokens[0] + " " + tokens[1];
      if (grams2.get(token) != null && grams1.get(tokens[1]) != null)
         prob = l2 * Double.parseDouble(grams2.get(token)[2]) + l1 * Double.parseDouble(grams1.get(tokens[1])[2]);
      else prob = 100.0;
      probs.add(prob);
      lgprobs.add(Math.log10(prob));
      
      // Other trigrams:
      for (int i = 2; i < tokens.length; i ++) {
         token = tokens[i - 2] + " " + tokens[i - 1] + " " + tokens[i];
         if (grams3.get(token) != null && grams2.get(tokens[i - 1] + " " + tokens[i]) != null && grams1.get(tokens[i]) != null)
            prob = l3 * Double.parseDouble(grams3.get(token)[2]) + l2 * Double.parseDouble(grams2.get(tokens[i - 1] + " " + tokens[i])[2]) + l1 * Double.parseDouble(grams1.get(tokens[i])[2]);
         else prob = 100.0;
         probs.add(prob);
         lgprobs.add(Math.log10(prob));
      }
      
      print(probs, tokens);
   }
   
   public static void print(ArrayList<Double> lgprobs, String[] tokens) {
      
      for (int i = 1; i < tokens.length; i ++) {
         if (i == 1) {
            System.out.print(i + ": lg P(" + tokens[i] + " | " + tokens[i - 1] + ") = ");
            System.out.println(lgprobs.get(0));
         } else {
            System.out.print(i + ": lg P(" + tokens[i] + " | " + tokens[i - 2] +  " " + tokens[i - 1] + ") = ");
            System.out.println(lgprobs.get(i - 1));
         }
      }
      
   }
}