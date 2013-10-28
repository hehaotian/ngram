import java.io.*;
import java.util.*;

public class ngram_count {

   public static Map<String, Integer> tally = new HashMap<String, Integer>();
   
   public static void main(String[] args) throws IOException {
      
      Scanner file = new Scanner(new File(args[0]));
      PrintStream ps = new PrintStream(args[1]);
      
      String line = null;
      String token = "";
      
      while (file.hasNextLine()) {
         if (file.hasNextLine()) {
            line = "<s> " + file.nextLine() + " </s>";
            tally = wordCount(line, tally);
         }
      }
      
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
   
   public static Map<String, Integer> wordCount(String line, Map<String,Integer> tally) {
      Scanner input = new Scanner(line);
      String word = "";
      while (input.hasNext()) {
         if(input.hasNext()) {
            word = input.next();
            if (tally.get(word) != null) {
               tally.put(word, tally.get(word) + 1);
            } else {
               tally.put(word, 1);
            }
         }
      }
      return tally;
   }
   
}