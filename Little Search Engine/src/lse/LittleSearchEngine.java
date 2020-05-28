package lse;

import java.io.*;
import java.util.*;

/**
 * This class builds an index of keywords. Each keyword maps to a set of pages in
 * which it occurs, with frequency of occurrence in each page.
 *
 */
public class LittleSearchEngine {
	
	/**
	 * This is a hash table of all keywords. The key is the actual keyword, and the associated value is
	 * an array list of all occurrences of the keyword in documents. The array list is maintained in 
	 * DESCENDING order of frequencies.
	 */
	HashMap<String,ArrayList<Occurrence>> keywordsIndex;
	
	/**
	 * The hash set of all noise words.
	 */
	HashSet<String> noiseWords;
	
 //FOR TESTING	...
	public static void main(String[] args) {
		LittleSearchEngine lse = new LittleSearchEngine();
		try {
			lse.makeIndex("docs.txt", "noisewords.txt");
		} catch(FileNotFoundException fnfe) {
		System.err.println("Fil not found.-[makeIndex]");
		}
	
	ArrayList<String> result = lse.top5search("deep", "world");
		System.out.println(result);
    }
	
	/**
	 * Creates the keyWordsIndex and noiseWords hash tables.
	 */
	public LittleSearchEngine() {
		keywordsIndex = new HashMap<String,ArrayList<Occurrence>>(1000,2.0f);
		noiseWords = new HashSet<String>(100,2.0f);
	}
	
	/**
	 * Scans a document, and loads all keywords found into a hash table of keyword occurrences
	 * in the document. Uses the getKeyWord method to separate keywords from other words.
	 * 
	 * @param docFile Name of the document file to be scanned and loaded
	 * @return Hash table of keywords in the given document, each associated with an Occurrence object
	 * @throws FileNotFoundException If the document file is not found on disk
	 */
	public HashMap<String,Occurrence> loadKeywordsFromDocument(String docFile)
	throws FileNotFoundException 
	{
	HashMap<String,Occurrence>ret_hash=new HashMap<String,Occurrence>();
	try {//open the file for reading
		
		BufferedReader rd = new BufferedReader(new FileReader(docFile));
		String line="";
		//read contents of the file
		while((line =rd.readLine())!=null) { 
		StringTokenizer st = new StringTokenizer(line, " ");
		
		while(st.hasMoreTokens()) { 
		String inWord = st.nextToken();
		String outWord = getKeyword(inWord);
		
		if(outWord!=null) {
		 if(ret_hash.containsKey(outWord)) { 
			Occurrence oldOccur = (Occurrence)ret_hash.get(outWord);
			int freq = oldOccur.frequency;
			Occurrence newOccur = new Occurrence(docFile, freq+1);  
			ret_hash.put(outWord, newOccur);
			}
		 else {
	      Occurrence occur = new Occurrence(docFile,1);
		  ret_hash.put(outWord, occur);
		 }
		 }
		}
		}
		rd.close();
	return ret_hash;
	}
	catch(IOException e) {
	//System.err.println("An IOException was caught!"); 
	return null;
	}
	}
	/**
	 * Merges the keywords from a single document into the master keywordsIndex
	 * hash table. For each keyword, its Occurrence in the current document
	 * must be inserted in the correct place (according to descending order of
	 * frequency) in the same keyword's Occurrence list in the master hash table. 
	 * This is done by calling the insertLastOccurrence method.
	 * 
	 * @param kws Keywords hash table for a document
	 */
	public void mergeKeywords(HashMap<String,Occurrence> kws) {
		Set<String> wordsSet = kws.keySet();	
		for(String word : wordsSet) {
		Occurrence occur = (Occurrence)kws.get(word);
		ArrayList<Occurrence> occurList;
				
	   // check if keywordsIndex has corresponding word 
		if( keywordsIndex.containsKey(word)) {
			occurList = keywordsIndex.get(word);
			occurList.add(occur);

			if(word.equalsIgnoreCase("deep")) {
			System.out.println("prev:"+occurList);
			}
			
     		ArrayList<Integer> retIndex = insertLastOccurrence(occurList);
     		
			if(word.equalsIgnoreCase("deep")) {
			System.out.println("idx:"+retIndex);
			System.out.println("next:"+occurList);
			}
		    } else {// if word NOT exists, then create new ArrayList and add
			  occurList = new ArrayList<Occurrence>();
			  occurList.add(occur);
			  }
			 keywordsIndex.put(word, occurList);
			}
		}

	
	/**
	 * Given a word, returns it as a keyword if it passes the keyword test,
	 * otherwise returns null. A keyword is any word that, after being stripped of any
	 * trailing punctuation, consists only of alphabetic letters, and is not
	 * a noise word. All words are treated in a case-INsensitive manner.
	 * 
	 * Punctuation characters are the following: '.', ',', '?', ':', ';' and '!'
	 * 
	 * @param word Candidate word
	 * @return Keyword (word without trailing punctuation, LOWER CASE)
	 */
	public String getKeyword(String word) {
		word = word.toLowerCase();
		boolean isWord=false;
		char[]wrd=word.toCharArray();
		for (int i=wrd.length-1; i>=0; i--){
			char c=wrd[i];
		 if (c== '.' || c==',' || c=='?' || c==':' || c==';' || c=='!'){
			wrd[i]='\0';
		 }
		 else {
		 break;
		 }
		}
		word=String.valueOf(wrd).trim();
		
		for(int i=0; i<word.length(); i++){
			char ch=word.charAt(i);
		 if(ch>=97 && ch<=122){
		 isWord=true;
		 } else {
		isWord=false;
		break;
		 }
		}
		
		if (isWord==false) {
			return null;
		}
		for(String noise: noiseWords) {
			if(word.equals(noise)){
				return null;
			}
		}
		return word;
		
	}
	
	/**
	 * Inserts the last occurrence in the parameter list in the correct position in the
	 * list, based on ordering occurrences on descending frequencies. The elements
	 * 0..n-2 in the list are already in the correct order. Insertion is done by
	 * first finding the correct spot using binary search, then inserting at that spot.
	 * 
	 * @param occs List of Occurrences
	 * @return Sequence of mid point indexes in the input list checked by the binary search process,
	 *         null if the size of the input list is 1. This returned array list is only used to test
	 *         your code - it is not used elsewhere in the program.
	 */
	public ArrayList<Integer> insertLastOccurrence(ArrayList<Occurrence> occs) {
		/** COMPLETE THIS METHOD **/
		// search for insert index 
	ArrayList<Integer>retIndex=new ArrayList<Integer>();
	int insertIndex=occs.size()-1;
	int insertFreq=occs.get(insertIndex).frequency;
	int right=occs.size()-1;
	int left=0;
	int mid=0;
	
    if(occs.size()==2){
    if(occs.get(0).frequency<insertFreq){
    occs.add(0,occs.get(insertIndex));
    occs.remove(occs.size()-1);
    }    
    retIndex.add(0);
    return retIndex;
    }

   
    while(true){
	    if(((right+left)%2==0) && left!=right){
	      mid=(left+right/2)-1;	
	    	} else {
	    mid=(left+right)/2;
	    	}
	    int compareFreq=occs.get(mid).frequency;
	  
	   
 //System.out.println(left+" "+mid+" "+right);
 
  if(occs.size()==3){
   if(compareFreq<=insertFreq){
	  occs.add(0,occs.get(insertIndex));
		occs.remove(occs.size()-1);  
		return retIndex;
	  }
	else {
		if(occs.get(1).frequency<insertFreq){
		occs.add(1,occs.get(insertIndex));
		occs.remove(occs.size()-1);
		return retIndex;
   }
		return retIndex;
   }
    }
  
  if(mid != (occs.size()-1)) 
		retIndex.add(mid);
	  
  if(left>=right){
	 if(occs.get(mid).frequency<insertFreq){
		occs.add(mid,occs.get(insertIndex));
		occs.remove(occs.size()-1);
		return retIndex;
	 }
  }	  
  if(mid==0){
	if(occs.get(mid).frequency<insertFreq){
	  occs.add(0,occs.get(insertIndex));
	  occs.remove(occs.size()-1);
	  return retIndex;
	}
  }
	  //System.out.println(mid); System.out.println(left); 
  if((compareFreq<=insertFreq)&&(occs.get(mid-1).frequency>=insertFreq)){
  occs.add(mid,occs.get(insertIndex));
  occs.remove(occs.size()-1);
  return retIndex;  
  }
  
  if(compareFreq<insertFreq){
	    right=mid-1;
	    }
	    if(compareFreq>insertFreq){
	    left=mid+1;
	    }
  
    }
	}

	/**
	 * This method indexes all keywords found in all the input documents. When this
	 * method is done, the keywordsIndex hash table will be filled with all keywords,
	 * each of which is associated with an array list of Occurrence objects, arranged
	 * in decreasing frequencies of occurrence.
	 * 
	 * @param docsFile Name of file that has a list of all the document file names, one name per line
	 * @param noiseWordsFile Name of file that has a list of noise words, one noise word per line
	 * @throws FileNotFoundException If there is a problem locating any of the input files on disk
	 */
	public void makeIndex(String docsFile, String noiseWordsFile) 
	throws FileNotFoundException {
		// load noise words to hash table
		Scanner sc = new Scanner(new File(noiseWordsFile));
		while (sc.hasNext()) {
			String word = sc.next();
			noiseWords.add(word);
		}
		
		// index all keywords
		sc = new Scanner(new File(docsFile));
		while (sc.hasNext()) {
			String docFile = sc.next(); 
			HashMap<String,Occurrence> kws = loadKeywordsFromDocument(docFile);
			mergeKeywords(kws);
		}
		sc.close();
	}
	
	
	/**
	 * Search result for "kw1 or kw2". A document is in the result set if kw1 or kw2 occurs in that
	 * document. Result set is arranged in descending order of document frequencies. (Note that a
	 * matching document will only appear once in the result.) Ties in frequency values are broken
	 * in favor of the first keyword. (That is, if kw1 is in doc1 with frequency f1, and kw2 is in doc2
	 * also with the same frequency f1, then doc1 will take precedence over doc2 in the result. 
	 * The result set is limited to 5 entries. If there are no matches at all, result is null.
	 * 
	 * @param kw1 First keyword
	 * @param kw1 Second keyword
	 * @return List of documents in which either kw1 or kw2 occurs, arranged in descending order of
	 *         frequencies. The result size is limited to 5 documents. If there are no matches, returns null.
	 */
	public ArrayList<String> top5search(String kw1, String kw2) {
		/** COMPLETE THIS METHOD **/
		Set<String> set = keywordsIndex.keySet();
		ArrayList<Occurrence> kw1ar, kw2ar;
		ArrayList<String> retArrList = new ArrayList<String>();
		
		kw1ar = keywordsIndex.get(kw1);
		kw2ar = keywordsIndex.get(kw2);
		
		if(kw1ar == null && kw2ar == null){
			return null;
		}
		if(kw1ar != null && kw2ar != null) {
		for(int i=0; i<kw2ar.size(); i++){
			Occurrence firstOccur = (Occurrence)kw2ar.get(i);
			for(int j = 0; j < kw1ar.size(); j++) {
				Occurrence secondOccur = (Occurrence)kw1ar.get(j);
				if(firstOccur.frequency >= secondOccur.frequency) {
					kw1ar.add(j, firstOccur);
					break;
				} else {
					if(j == kw1ar.size()-1) {
						kw1ar.add(firstOccur);
					}
				}
			}
		}
	}  
	    retArrList.add(kw1ar.get(0).document);
		for(int k=1; k<kw1ar.size(); k++){
		     if(!retArrList.contains(kw1ar.get(k).document)){
		    	 if(retArrList.size() < 5) {
						retArrList.add(kw1ar.get(k).document);
			}
		     }}	
		return retArrList;
	}
}