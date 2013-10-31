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
   
   public static int sent_num;
   public static int word_num;
   public static int oov_num;
   public static double lgprob_sum;
   
   public static int gene_cnt;
   
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
      
      sent_num = 0;
      word_num = 0;
      oov_num = 0;
      gene_cnt = 0;
      
      while (test.hasNextLine()) {
         if (test.hasNextLine()) {
            sent_num ++;
            line = "<s> " + test.nextLine() + " </s>";
            String[] tokens = line.split(" ");
            word_num += tokens.length - 2;
            ps.println("Sent #" + sent_num + ": " + line);
            calculate(tokens, sent_num, ps);
         }
      }
      print_conc(ps);
   }
   
   public static void calculate(String[] tokens, int sent_num, PrintStream ps) {
      
      ArrayList<Double> probs = new ArrayList<Double>();
      ArrayList<Double> lgprobs = new ArrayList<Double>();
      ArrayList<Integer> mark = new ArrayList<Integer>();
      
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
            // regular ngrams
            mark.add(0);
            prob = l2 * Double.parseDouble(grams2.get(token)[1]) + l1 * Double.parseDouble(grams1.get(tokens[1])[1]);
         } else {
            // unseen ngrams to check unigrams
            mark.add(1);
            prob = l1 * Double.parseDouble(grams1.get(tokens[1])[1]);
         }
         sum += Math.log10(prob);
         cnt ++;
      } else {
         // unknown word
         mark.add(1);
         prob = 0.0;
         oov ++;
      }
      probs.add(prob);
      lgprobs.add(Math.log10(prob));
      
      // Other trigrams:
      for (int i = 2; i < tokens.length; i ++) {
         token = tokens[i - 2] + " " + tokens[i - 1] + " " + tokens[i];
         if (grams1.get(tokens[i]) != null) {
            if (grams3.get(token) != null) {
               // regular ngrams
               mark.add(0);
               prob = l3 * Double.parseDouble(grams3.get(token)[1]) + l2 * Double.parseDouble(grams2.get(tokens[i - 1] + " " + tokens[i])[1]) + l1 * Double.parseDouble(grams1.get(tokens[i])[1]);
            } else if (grams2.get(tokens[i - 1] + " " + tokens[i]) != null) {
               // unseen ngrams to check bigrams
               mark.add(1);
               prob = l2 * Double.parseDouble(grams2.get(tokens[i - 1] + " " + tokens[i])[1]) + l1 * Double.parseDouble(grams1.get(tokens[i])[1]);
            } else {
               // unseen ngrams to check unigrams
               mark.add(1);
               prob = l1 * Double.parseDouble(grams1.get(tokens[i])[1]);
            }
            sum += Math.log10(prob);
            cnt ++;
         } else {
            // unknown word
            mark.add(2);
            prob = 0.0;
            oov ++;
         }
         probs.add(prob);
         lgprobs.add(Math.log10(prob));
      }
      
      total = - sum / cnt;
      ppl = Math.pow(10, total);
      
      lgprob_sum += sum;
      gene_cnt += cnt;
      oov_num += oov;
      
      // unknown 3.0; unseen 2.0
      print(lgprobs, tokens, mark, ps);
      
      ps.println("1 sentence, " + (tokens.length - 2) + " words, " + oov + " OOVs");
      ps.println("lgprob=" + sum + " ppl=" + ppl);
      ps.println();
      ps.println();
      ps.println();
   }
   
   public static void print(ArrayList<Double> lgprobs, String[] tokens, ArrayList<Integer> mark, PrintStream ps) {
      
      for (int i = 1; i < tokens.length; i ++) {
         if (i == 1) {
            ps.print(i + ": lg P(" + tokens[i] + " | " + tokens[i - 1] + ") = ");
            if (mark.get(0).equals(2)) {
               ps.println("-inf (unknown word)");
            } else if (mark.get(0).equals(1)) {
               ps.print(lgprobs.get(0));
               ps.println(" (unseen ngrams)");
            } else {
               ps.println(lgprobs.get(0));
            }
         } else {
            ps.print(i + ": lg P(" + tokens[i] + " | " + tokens[i - 2] +  " " + tokens[i - 1] + ") = ");
            if (mark.get(i - 1).equals(2)) {
               ps.println("-inf (unknown word)");
            } else if (mark.get(i - 1).equals(1)) {
               ps.print(lgprobs.get(i - 1));
               ps.println(" (unseen ngrams)");
            } else {
               ps.println(lgprobs.get(i - 1));
            }
         }
      }
   }
   
   public static void print_conc(PrintStream ps) {

      double final_ppl = Math.pow(10, - lgprob_sum / gene_cnt);

      ps.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
      ps.println("sent_num=" + sent_num + " word_num=" + word_num + " oov_num=" + oov_num);
      ps.println("lgprob=" + lgprob_sum + " ave_lgprob=" + lgprob_sum / (word_num + sent_num - oov_num) + " ppl=" + final_ppl);
   }
}