import java.io.*;
import java.util.*;

public class Langmodel { 

   private Map<String, String[]> ngram1 = new HashMap<String, Integer>();
   private Map<String, String[]> ngram2 = new HashMap<String, Integer>();
   private Map<String, String[]> ngram3 = new HashMap<String, Integer>();
   private String line;
   private String token;  
   private int token1;
   private int token2;
   private int token3;
   
   public Langmodel(Scanner file) {
      
      this.ngram1 = ngram1;
      this.ngram2 = ngram2;
      this.ngram3 = ngram3;
            
      line = "";
      token = "";
      token1 = 0;
      token2 = 0;
      token3 = 0;
      
      while (file.hasNextLine()) {
         if (file.hasNextLine()) {
            
         }
      }
   }

}