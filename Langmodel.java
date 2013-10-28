import java.io.*;
import java.util.*;

public class Langmodel { 
   
   private Map<String, Integer> tally1 = new HashMap<String, Integer>();
   private Map<String, Integer> tally2 = new HashMap<String, Integer>();
   private Map<String, Integer> tally3 = new HashMap<String, Integer>();
    
   private int token1;
   private int token2;
   private int token3;
   
   public Langmodel(Scanner file) {
      
      this.tally1 = tally1;
      this.tally2 = tally2;
      this.tally3 = tally3;
      
      String line = "";
      int count = 0;
      String token = "";
      String num = "";
      
      while (file.hasNextLine()) {
         if (file.hasNextLine()) {
            line = file.nextLine();
            num = line.replaceAll("([0-9]+)(\\t)(.*)", "$1");
            count = Integer.parseInt(num);
            token = line.replaceAll("([0-9]+)(\\t)(.*)", "$3");
            if (token.split(" ").length == 1) {
               token1 += count;
               tally1.put(token, count);
            } else if (token.split(" ").length == 2) {
               token2 += count;
               tally2.put(token, count);
            } else if (token.split(" ").length == 3) {
               token3 += count;
               tally3.put(token, count);
            } else {
               System.out.println("WRONG NGRAM COUNT!");
            } 
         }
      }
   }
   
   public void print_data(PrintStream ps) {
      ps.println("\\data\\");
      ps.println("ngram 1: type=" + tally1.size() + " token=" + token1);
      ps.println("ngram 2: type=" + tally2.size() + " token=" + token2);
      ps.println("ngram 3: type=" + tally3.size() + " token=" + token3);
      ps.println();
   }
   
   public void print_ngram(PrintStream ps) {
      printHelper(ps, tally1, 1);
      ps.println();
      printHelper(ps, tally2, 2);
      ps.println();
      printHelper(ps, tally3, 3);
      ps.println();
   }
   
   
   public void print_end(PrintStream ps) {
      ps.println("\\end\\");
   }
   
   public void printHelper(PrintStream ps, Map<String, Integer> tally, int n) {
      
      int cc = 0;
      double prob = 1.0;
      double lgprob = 1.0;
      String temp = "";
      
      List<Map.Entry<String, Integer>> entryList = new ArrayList<Map.Entry<String, Integer>>();
      entryList.addAll(tally.entrySet());
      
      Collections.sort(entryList, new Comparator<Map.Entry<String, Integer>>() {
         public int compare(Map.Entry<String, Integer> a, Map.Entry<String, Integer> b) {
            return b.getValue() - a.getValue();
         }
      }
      );
      
      if (n == 1) {
         ps.println("\\1-grams:");
         for (Map.Entry<String, Integer> a: entryList) {
            cc = a.getValue();
            prob = cc * 1.0 / token1;
            lgprob = Math.log10(prob);
            temp = cc + " " + prob + " " + lgprob;
            ps.println(temp + " " + a.getKey());
         }
      } else if (n == 2) {
         ps.println("\\2-grams:");
         for (Map.Entry<String, Integer> a: entryList) {
            cc = a.getValue();
            prob = cc * 1.0 / tally1.get(a.getKey().split(" ")[0]);
            lgprob = Math.log10(prob);
            temp = cc + " " + prob + " " + lgprob;
            ps.println(temp + " " + a.getKey());
         }
      } else if (n == 3) {
         ps.println("\\3-grams:");
         for (Map.Entry<String, Integer> a: entryList) {
            cc = a.getValue();
            prob = cc * 1.0 / tally2.get(a.getKey().split(" ")[0] + " " + a.getKey().split(" ")[1]);
            lgprob = Math.log10(prob);
            temp = cc + " " + prob + " " + lgprob;
            ps.println(temp + " " + a.getKey());
         }
      } else {
         ps.println("NGRAM WRONG!");
      }
   }

}