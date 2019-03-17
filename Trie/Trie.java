package trie;

import java.util.ArrayList;

/**
 * This class implements a Trie. 
 * 
 * @author Sesh Venugopal
 *
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
		/*ALGORITHM
		 *1.Create root node and insert the first word as firstChild of root
		 *2.Traverse the node and compare each character
		 *3. -If no match at the first character, traverse the sibling until no sibling exists.
		 *4.  If sibling no longer exists, insert the node to be inserted. Repeat from 2 with next word in array(traverse from root.firstchild)
		 *5. -If match, compare character up to length of word to be inserted.
		 *6.  If no longer character matches, endIndex of node to be compared is the last index where character matches(lastMatch)
		 *7.Traverse to the firstChild of current node
		 *8. -If no firstChild, create the firstChild(startindex:lastMatch+1; firstChild is the substring of node to be compared). 
		 *9. Create the sibling of the firstChild(startindex: lastMatch+1; sibling is the substring of node to be inserted).
		 *10.Repeat from 2 with next word in array(traverse from root.firstchild).
		 *11.-If fistChild exists, repeat from 2(traverse from currentNode)
		 */
		TrieNode root = new TrieNode(null, null, null);   
		boolean isExist,bfExist=false;
		if( root.firstChild == null ) 
		{   
			root.firstChild = new TrieNode(new Indexes(0, (short)0, (short)(allWords[0].length()-1)), null, null); // First child of root
		}
	    
		for (int arrayIndex=1; arrayIndex<allWords.length; arrayIndex++)
		{
			int i=0;
			String word_array=allWords[arrayIndex];
			TrieNode currentNode=root.firstChild;
			isExist=false; bfExist=false;
		  
			INNER:
			while(i<word_array.length())
			{
				String word_Trie=allWords[currentNode.substr.wordIndex];
				if (currentNode.substr.endIndex<i)
				{
					currentNode=currentNode.firstChild;
					isExist=false; bfExist=false; 
				}
				else 
				{
					if(word_array.charAt(i)==word_Trie.charAt(i))
					{
						isExist=true;	
						bfExist=true;
						i++;
						continue;
					}
					else if ((word_array.charAt(i)!=word_Trie.charAt(i)) && bfExist==false)
					{
					
						if (currentNode.sibling==null)
						{
							currentNode.sibling=new TrieNode(new Indexes(arrayIndex,(short)i,(short)(allWords[arrayIndex].length()-1)),null,null);
							break INNER;
						}
					
						currentNode=currentNode.sibling;
					}
					else 
					{
						currentNode.substr.endIndex=(short)(i-1);
						if (currentNode.firstChild==null)
						{
							currentNode.firstChild= new TrieNode(new Indexes(currentNode.substr.wordIndex, (short)i, (short)(word_Trie.length()-1)), null,null);
							currentNode.firstChild.sibling=new TrieNode(new Indexes(arrayIndex,(short)i,(short)(allWords[arrayIndex].length()-1)),null,null);
							currentNode=root.firstChild;
							break INNER;
						}
						else 
						{	
						currentNode=currentNode.firstChild;
						}
					}
				}
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
	public static ArrayList<TrieNode> completionList(TrieNode root, String[] allWords, String prefix) 
	{
		ArrayList<TrieNode> resultList = new ArrayList<>(); // for return 
		TrieNode node = root.firstChild;
       		int beforeWordIndex = -1; 
		
		while ( node != null ) 
		{
			String word = allWords[node.substr.wordIndex]; 
			String wordSubstr = word.substring(0, node.substr.endIndex+1); 
			String searchText = prefix; 
			
			if( wordSubstr.length() < searchText.length() ) 
			{
				searchText = searchText.substring(0, node.substr.endIndex+1);	
			}
			
			if( wordSubstr.indexOf(searchText) != 0 ) 
			{
				node = node.sibling;
				continue; 
			}
			if( word.indexOf(prefix) == 0 && beforeWordIndex != node.substr.wordIndex) 
			{
				beforeWordIndex = node.substr.wordIndex;
				resultList.add(node);
			}
			
			if( node.firstChild != null ) 
			{
				node = node.firstChild;	
			} 
			else if( node.sibling != null ) 
			{
				node = node.sibling; 
			} 
			else 
			{ 
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
