import java.io.*;
import java.util.*;

public class Ngram { 

   private Map<String, Integer> tally1 = new HashMap<String, Integer>();
   private Map<String, Integer> tally2 = new HashMap<String, Integer>();
   private Map<String, Integer> tally3 = new HashMap<String, Integer>();
   private String line;
   private String token;  
   private int token1;
   private int token2;
   private int token3;

   public Ngram(Scanner file) { 
      
      this.tally1 = tally1;
      this.tally2 = tally2;
      this.tally3 = tally3;
      
      line = "";
      token = "";
      token1 = 0;
      token2 = 0;
      token3 = 0;
      
      while (file.hasNextLine()) {
         if (file.hasNextLine()) {
            line = "<s> " + file.nextLine() + " </s>";
            tally1 = wordCount(line, tally1, 1);
            tally2 = wordCount(line, tally2, 2);
            tally3 = wordCount(line, tally3, 3);
         }
      }
   }
   
   public Map<String, Integer> wordCount(String line, Map<String,Integer> tally, int n) {
      
      ArrayList<String> words = new ArrayList<String>();
      String temp = "";
      
      for (String word : line.split(" "))
         words.add(word);
         
      if (n == 1) {
         token1 += words.size();
         for (int i = 0; i < words.size(); i ++) {
            if (tally.get(words.get(i)) != null) tally.put(words.get(i), tally.get(words.get(i)) + 1);
            else tally.put(words.get(i), 1);
         }
      } else if (n == 2) {
         token2 += words.size() - 1;
         for (int i = 0; i < words.size() - 1; i ++) {
            temp = words.get(i) + " " + words.get(i + 1);
            if (tally.get(temp) != null) {
               tally.put(temp, tally.get(temp) + 1);
            } else {
               tally.put(temp, 1);
            }
         }
      } else {
         token3 += words.size() - 2;
         for (int i = 0; i < words.size() - 2; i ++) {
            temp = words.get(i) + " " + words.get(i + 1) + " " + words.get(i + 2);
            if (tally.get(temp) != null) {
               tally.put(temp, tally.get(temp) + 1);
            } else {
               tally.put(temp, 1);
            }
         }
      }
      return tally;
   }
   
   public void print_calc(PrintStream ps) {
      ps.println("\\data\\");
      ps.println("ngram 1: type=" + tally1.size() + " token=" + token1);
      ps.println("ngram 2: type=" + tally2.size() + " token=" + token2);
      ps.println("ngram 3: type=" + tally3.size() + " token=" + token3);
   }
   
   public void print_count(PrintStream ps) {
      printHelper(ps, tally1);
      printHelper(ps, tally2);
      printHelper(ps, tally3);
   }
   
   public void printHelper(PrintStream ps, Map<String, Integer> tally) {
   
      List<Map.Entry<String, Integer>> entryList = new ArrayList<Map.Entry<String, Integer>>();
      entryList.addAll(tally.entrySet());
      
      Collections.sort(entryList, new Comparator<Map.Entry<String, Integer>>() {
         public int compare(Map.Entry<String, Integer> a, Map.Entry<String, Integer> b) {
            return b.getValue() - a.getValue();
         }
      }
      );
      
      for (Map.Entry<String, Integer> a: entryList) {
         ps.println(a.getValue() + "\t" + a.getKey());
      }
      
   }

}