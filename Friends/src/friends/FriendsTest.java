package friends;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;
import org.junit.Test;


public class FriendsTest 
{
	
	void printGraph(Graph g) 
	{
		System.out.println(g.members.length);
		
		for (int i=0; i < g.members.length; i++) 
		{
			Person p = g.members[i];
			if (p.student) 
			{
				System.out.println(String.format("%s|y|%s", g.members[i].name,  g.members[i].school));	
			} else 
			{
				System.out.println(String.format("%s|n", g.members[i].name));	
			}	
		}
		
		for(int i=0; i < g.members.length; i++)
		{
			Person person = g.members[i];
			Friend friend = g.members[i].first;
			while(friend != null)
			{
				if( i < friend.fnum)
				{
					System.out.println(person.name + "|" + g.members[friend.fnum].name);
				}
				friend = friend.next;
			}
		}		
	}
	
	@Test
	public void testShortestChain()
	{

		Graph graph;
		try
		{
			graph = new Graph(new Scanner(new File("g1.txt")));
			//printGraph(graph);
			
			ArrayList<String> result = Friends.shortestChain(graph, "sam", "aparna");
			
			System.out.println("ShortestChain: sam -> aparna");
			
			System.out.println("result: " + result);
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	

	@Test
	public void testCliques() 
	{

		Graph graph;
		try
		{
			graph = new Graph(new Scanner(new File("g1.txt")));
			//printGraph(graph);
			
			ArrayList<ArrayList<String>> result = Friends.cliques(graph, "rutgers");
			
			System.out.println("Cliques: rutgers");
			System.out.println(result);
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
	
