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
      int oov_num = 0;
      
      while (test.hasNextLine()) {
         if (test.hasNextLine()) {
            sent_num ++;
            line = "<s> " + test.nextLine() + " </s>";
            String[] tokens = line.split(" ");
            word_num += tokens.length - 2;
            System.out.println("Sent #" + sent_num + ": " + line);
            calculate(tokens, sent_num);
         }
      }
      System.out.println();
      System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
      System.out.println("sent_num=" + sent_num + " word_num=" + word_num + " oov_num=" + oov_num);
   }
   
   public static void calculate(String[] tokens, int sent_num) {
      
      ArrayList<Double> probs = new ArrayList<Double>();
      ArrayList<Double> lgprobs = new ArrayList<Double>();
      
      String token = "";
      String check = "";
      
      int oov = 0;
      int cnt = 0;
      double sum = 0.0;
      double total;
      double ppl;
      
      double prob = 1.0;
      
      // First bigrams:
      token = tokens[0] + " " + tokens[1];
      if (grams1.get(tokens[1]) != null) {
         if (grams2.get(token) != null) {
            prob = l2 * Double.parseDouble(grams2.get(token)[2]) + l1 * Double.parseDouble(grams1.get(tokens[1])[2]);
            sum += Math.log10(prob);
            cnt ++;
         } else {
            prob = 100.0;
         }
      } else {
         prob = 1000.0;
         oov ++;
      }
      probs.add(prob);
      lgprobs.add(Math.log10(prob));
      
      // Other trigrams:
      for (int i = 2; i < tokens.length; i ++) {
         token = tokens[i - 2] + " " + tokens[i - 1] + " " + tokens[i];
         if (grams1.get(tokens[i]) != null) {
            if (grams3.get(token) != null && grams2.get(tokens[i - 1] + " " + tokens[i]) != null) {
               prob = l3 * Double.parseDouble(grams3.get(token)[2]) + l2 * Double.parseDouble(grams2.get(tokens[i - 1] + " " + tokens[i])[2]) + l1 * Double.parseDouble(grams1.get(tokens[i])[2]);
               sum += Math.log10(prob);
               cnt ++;
            } else {
               prob = 100.0;
            }
         } else {
            prob = 1000.0;
            oov ++;
         }
         probs.add(prob);
         lgprobs.add(Math.log10(prob));
      }
      
      total = - 1.0 * sum / cnt;
      ppl = Math.pow(10, total);
      
      // unknown 3.0; unseen 2.0
      print(lgprobs, tokens);
      
      System.out.println("1 sentence, " + (tokens.length - 2) + " words, " + oov + " OOVs");
      System.out.println("lgprob=" + total + " ppl=" + ppl);
      System.out.println();
   }
   
   public static void print(ArrayList<Double> lgprobs, String[] tokens) {
      
      for (int i = 1; i < tokens.length; i ++) {
         if (i == 1) {
            System.out.print(i + ": lg P(" + tokens[i] + " | " + tokens[i - 1] + ") = ");
            if (lgprobs.get(0).equals(3.0)) {
               System.out.println("-inf (unknown word)");
            } else if (lgprobs.get(0).equals(2.0)) {
               System.out.print(lgprobs.get(0));
               System.out.println(" (unseen ngrams)");
            } else {
               System.out.println(lgprobs.get(0));
            }
         } else {
            System.out.print(i + ": lg P(" + tokens[i] + " | " + tokens[i - 2] +  " " + tokens[i - 1] + ") = ");
            if (lgprobs.get(i - 1).equals(3.0)) {
               System.out.println("-inf (unknown word)");
            } else if (lgprobs.get(i - 1).equals(2.0)) {
               System.out.print(lgprobs.get(i - 1));
               System.out.println(" (unseen ngrams)");
            } else {
               System.out.println(lgprobs.get(i - 1));
            }
         }
      }
   }
}