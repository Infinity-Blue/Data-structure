package trie;

import java.util.ArrayList;

/*
 * This class implements a Trie. 
 */
public class Trie 
{
	
	// prevent instantiation
	private Trie() { }
	
	/**
	 * Builds a trie by inserting all words in the input array, one at a time,
	 * in sequence FROM FIRST TO LAST. (The sequence is IMPORTANT!)
	 * The words in the input array are all lower case.
	 * 
	 * @param allWords Input array of words (lowercase) to be inserted.
	 * @return Root of trie with all words inserted from the input array
	 */
	
	public static TrieNode buildTrie(String[] allWords) 
	{
		/** COMPLETE THIS METHOD **/

		TrieNode root = new TrieNode(null, null, null);
		if( root.firstChild == null ) 
		{
			root.firstChild = new TrieNode(new Indexes(0, (short)0, (short)(allWords[0].length()-1)), null, null); // First child of root
		} 

		TrieNode node = root.firstChild;
		for (int wordIndex = 1; wordIndex < allWords.length; wordIndex++) 
		{
			String word = allWords[wordIndex]; //"CAT"
			char[] chArray = word.toCharArray(); //['c', 'a', 't']

			boolean bfExist = false;
			break_label: 
			for (int index = 0; index < chArray.length; index++) 
			{
				char ch = chArray[index]; //'c'
				do {
				boolean isExist = false;
//			if( node == null ) {
//				isExist = false;
//				} 
			
//			else {//nodes exists 
				String nodeWord = allWords[node.substr.wordIndex].substring(0, node.substr.endIndex+1); //node substring
				if( node.substr.endIndex < index )
				{ // check whether we can compare char of existing node and inserting node. 
					isExist = false; 
				} 
				else 
				{ // we can compare char of existing node and inserting node.
					char nodeWordCh = nodeWord.charAt(index); 
					if( nodeWordCh == ch ) 
					{ 
						isExist = true;
					}  

				}
					
			if( !isExist )
			{ // common prefix NOT exists 
			if( !bfExist ) 
			{ // common prefix NOT exists 
			if( node.sibling == null ) 
			{
			node.sibling = new TrieNode(new Indexes(wordIndex, (short)index, (short)(chArray.length - 1)), null, null);
				node = root.firstChild; 
					break break_label;  // break label to iterate over next item in allwords[]
			}
			else 
			{ 
				node = node.sibling; // compare node.sibiling with current inserting node. 
			}
			} 
			 // Common prefix exist at previous char but NOT found at current index. "po ttery", "po ssible"
			  else 
			  {
				bfExist = false;
					if( node.firstChild == null ) 
					{
				node.substr.endIndex = (short)(index-1); // change endIndex of existing node.
				node.firstChild = new TrieNode(new Indexes(node.substr.wordIndex, (short)index, (short)(allWords[node.substr.wordIndex].length() - 1)), null, null);
				node.firstChild.sibling = new TrieNode(new Indexes(wordIndex, (short)index, (short)(chArray.length - 1)), null, null);
				node = root.firstChild;
				break break_label;  // wordindex = wordindex + 1; 
					}
			
				else 
				{ 
					if( node.substr.endIndex < index ) 
					{
						node = node.firstChild;
					} 
					else 
					{ 
						node.firstChild = new TrieNode(new Indexes(node.substr.wordIndex, (short)index, (short)(node.substr.endIndex)), node.firstChild, null);
						node.substr.endIndex = (short)(index-1);
						node = node.firstChild;	
					}
				 }
			   }
			  }
	        // isExist
	        else
	        {
				bfExist = true;
				break; // index = index +1; 
			}
				} while (node != null);
		 }
	  } 		
				
     return root;
	}
	
	
	/**
	 * Given a trie, returns the "completion list" for a prefix, i.e. all the leaf nodes in the 
	 * trie whose words start with this prefix. 
	 * For instance, if the trie had the words "bear", "bull", "stock", and "bell",
	 * the completion list for prefix "b" would be the leaf nodes that hold "bear", "bull", and "bell"; 
	 * for prefix "be", the completion would be the leaf nodes that hold "bear" and "bell", 
	 * and for prefix "bell", completion would be the leaf node that holds "bell". 
	 * (The last example shows that an input prefix can be an entire word.) 
	 * The order of returned leaf nodes DOES NOT MATTER. So, for prefix "be",
	 * the returned list of leaf nodes can be either hold [bear,bell] or [bell,bear].
	 *
	 * @param root Root of Trie that stores all words to search on for completion lists
	 * @param allWords Array of words that have been inserted into the trie
	 * @param prefix Prefix to be completed with words in trie
	 * @return List of all leaf nodes in trie that hold words that start with the prefix, 
	 * 			order of leaf nodes does not matter.
	 *         If there is no word in the tree that has this prefix, null is returned.
	 */
	public static ArrayList<TrieNode> completionList(TrieNode root,
										String[] allWords, String prefix) 
	{
		ArrayList<TrieNode> resultList = new ArrayList<>(); // for return 
		TrieNode node = root.firstChild;
        int beforeWordIndex = -1; 
		
		while ( node != null ) 
		{
			String word = allWords[node.substr.wordIndex]; // content in allWords[]
			String wordSubstr = word.substring(0, node.substr.endIndex+1); //substring of word
			String searchText = prefix; 
			
			if( wordSubstr.length() < searchText.length() ) 
			{
				searchText = searchText.substring(0, node.substr.endIndex+1);	
			}
			
			if( wordSubstr.indexOf(searchText) != 0 )
			{//prefix NOT exist. go to next sibling
				node = node.sibling;
				continue; 
			}
			
			if( word.indexOf(prefix) == 0 && beforeWordIndex != node.substr.wordIndex) 
			{
				beforeWordIndex = node.substr.wordIndex;
				resultList.add(node);
			}
			
			// prefix length is SMALLER than or EQUAL to wordsubstr.length. (prefix p, node is pot) 
			if( node.firstChild != null ) 
			{
				node = node.firstChild;	
			} 
			else if( node.sibling != null )
			{
				node = node.sibling; 
			} 
			else 
			{ // firstchild and sibling of node NOT exist. 
				node = null; 
			}
		}
		
		return resultList.size() > 0 ? resultList : null;
	}
	
	
		
	public static void print(TrieNode root, String[] allWords) 
	{
		System.out.println("\nTRIE\n");
		print(root, 1, allWords);
	}
	
	private static void print(TrieNode root, int indent, String[] words) 
	{
		if (root == null)
		{
			return;
		}
		for (int i=0; i < indent-1; i++)
		{
			System.out.print("    ");
		}
		
		if (root.substr != null)
		{
			String pre = words[root.substr.wordIndex]
							.substring(0, root.substr.endIndex+1);
			System.out.println("      " + pre);
		}
		
		for (int i=0; i < indent-1; i++) 
		{
			System.out.print("    ");
		}
		System.out.print(" ---");
		if (root.substr == null) 
		{
			System.out.println("root");
		} 
		else
		{
			System.out.println(root.substr);
		}
		
		for (TrieNode ptr=root.firstChild; ptr != null; ptr=ptr.sibling) 
		{
			for (int i=0; i < indent-1; i++)
			{
				System.out.print("    ");
			}
			System.out.println("     |");
			print(ptr, indent+1, words);
		}
	}
 }