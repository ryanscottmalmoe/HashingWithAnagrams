//Ryan Malmoe | Prof: Tom Capaul | CSCD300 12:00

//Extra Credit Below:

//My hashFunction shown below eliminates 20 collisions
//that java's hashCode function would have had.
//My hashFunction() creates 1437 collisions while...
//java's hashCode method produces 1457 collisions.
//(Knowing java's hashCode method is way faster)

//My program is faster than binary search of 25000 * (log2 25000) because 
//amount of searches would be faster than 25000 * 15 searches. While my program
//completes in 25000 searches. 

import java.lang.Math.*;
import java.util.*;
import java.lang.*;
import java.io.*;

public class HashTester 
{
   private static Scanner fin = null;
   private static PrintStream fout;
   
   public static void main(String[] args)
   {      
      int coli = 0;
      int wordsPlaced = 0;
   
      if(args.length != 2)
      {
         System.out.println("Usage: java hashTester input_file output_file");
      }
      else //Open up the dictionary file that you have.
      {
         String inputFile = args[0];
         String outputFile = args[1];
         
         
         //Open input file
         try
         {
            fin = new Scanner(new File(inputFile));
         }
         catch(Exception e)
         {
            System.out.println("Connection failed to input file: " + inputFile);
         }
         
         //Open output file
         try
         {
            fout = new PrintStream(outputFile);
         }
         
         catch(Exception e)
         {
            System.out.println("Connection failed to output file: " + outputFile);
         }
      
         LinkedList<LinkedList<Anagram>>[] hashTable = new LinkedList[199999];
         while(fin.hasNext())
         {
            String dicWord = fin.nextLine();
            Anagram cur = new Anagram();
         
            if((dicWord.replace("\'", "")).length() == dicWord.length() && Character.isDigit(dicWord.charAt(0)) || dicWord.length() > 1)
            {
               dicWord = dicWord.toLowerCase();
               cur.word = dicWord;
               char[] wordArray = dicWord.toCharArray();
               Arrays.sort(wordArray);
               String sWord = new String(wordArray);
               cur.wordSorted = sWord;
               int hashIndex = hashFunction(cur.getWordSorted(), hashTable);
               
               //JAVA'S HASHCODE METHOD
               //-------------------------------------------------------
               
               //int hashIndex = Math.abs(cur.getWordSorted().hashCode() % hashTable.length);
               
               //-------------------------------------------------------
               
               if(hashTable[hashIndex] == null)
               {           
                  //Creates LinkedList for all anagrams (col)
                  hashTable[hashIndex] = new LinkedList();  
                  
                  //Creates new LinkedList for identical anagrams (row)
                  LinkedList<Anagram> one = new LinkedList();
                       
                  //Adds the first row to LinkedList
                  hashTable[hashIndex].add(one);
                  
                  //Adds cur to the first row of the LinkedList
                  hashTable[hashIndex].get(0).add(cur);
                  wordsPlaced++;
                  
               }
               else
               {
                  for(int i = 0; i < hashTable[hashIndex].size(); i++)
                  {
                     LinkedList<Anagram> row = hashTable[hashIndex].get(i);
                     if(!(row.get(0).getWordSorted().equals(cur.getWordSorted())))
                     {
                        coli++;
                     }
                     else if(row.get(0).getWordSorted().equals(cur.getWordSorted()))
                     {
                        row.add(cur);
                        wordsPlaced++;
                        break;
                     }
                     else if(i == hashTable[hashIndex].size() - 1)
                     {
                        LinkedList<Anagram> two = new LinkedList();
                        hashTable[hashIndex].addFirst(two);
                        hashTable[hashIndex].get(0).add(cur);
                        wordsPlaced++;
                     }
                  }
               }
            }
         }
         printHashTable(hashTable, fout);
         fin.close();
      } 
      fout.println("Collisions: " + coli);
      fout.println("Words Placed: " + wordsPlaced);
      fout.close();
   } //End of main.
   

   
   public static void printHashTable(LinkedList<LinkedList<Anagram>>[] hashTable, PrintStream fout)
   {
      int wordCount;
      
      for(int i = 0; i < hashTable.length; i++)
      {
         if(hashTable[i] == null);
         else
         {
            for(int k = 0; k < hashTable[i].size(); k++)
            {      
               wordCount = 0;
               String result = "";
               String result2 = "";
               String resultFinal = "";
               
               result = hashTable[i].get(k).get(0).getWordSorted() + " "; 
               for(int j = 0; j < hashTable[i].get(k).size(); j++)
               {               
                  String firstWord = hashTable[i].get(k).get(j).getWord();
                  result2 += " " + firstWord;
                  wordCount++;
               }
            
               resultFinal = result + " " + wordCount + result2;      
               fout.println(resultFinal);
            }
         }
      }
   }
   
   
   public static int hashFunction(String s, LinkedList<LinkedList<Anagram>>[] hashTable)
   {
      int result = 5;
      
      for(int i = 0; i < s.length(); i++)
      {
         int ascii = s.charAt(i);
         result +=  result * 11 + ascii;
      }
      result = Math.abs(result % hashTable.length);
      return result; 
   }

}