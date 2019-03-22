package friends;
import structures.Queue;
import structures.Stack;
import java.util.*;

public class Friends {

	/**
	 * Finds the shortest chain of people from p1 to p2.
	 * Chain is returned as a sequence of names starting with p1,
	 * and ending with p2. Each pair (n1,n2) of consecutive names in
	 * the returned chain is an edge in the graph.
	 * 
	 * @param g Graph for which shortest chain is to be found.
	 * @param p1 Person with whom the chain originates
	 * @param p2 Person at whom the chain terminates
	 * @return The shortest chain from p1 to p2. Null if there is no
	 *         path from p1 to p2
	 */
	public static ArrayList<String> shortestChain(Graph g, String p1, String p2) 
	{
		ArrayList<String> path = new ArrayList<String>(); //for the return 
		if (p1 == null || p2 == null)
		{
			return path;
		}
		if (!g.map.containsKey(p1) || !g.map.containsKey(p2)) 
		{			
			return path;
		}
		if (p1.equals(p2)) 
		{
			return path;
		}
		
		HashMap<String, String> prev = new HashMap<String, String>();
		HashMap<String, Boolean> visited = new HashMap<String, Boolean>();
		Queue<String> queue = new Queue<String>();

		for(String s: g.map.keySet())
		{
			visited.put(s, false);
		}
		queue.enqueue(p1);
		visited.put(p1, true);
		prev.put(p1, p1);
		
		while(!queue.isEmpty())
		{
			String a = queue.dequeue();
			Friend current = getPerson(g,a).first; 
			
			while(current != null) 
			{
				String fname = getName(g, current);
				if (visited.get(fname) == false)
				{
					visited.put(fname, true);
					prev.put(fname, a);
					queue.enqueue(fname);
				}
				if (fname.equals(p2)) 
				{
					path.add(p2);
					String next = prev.get(p2);
					
					while (true)
					{
						path.add(0, next);
						if (next.equals(prev.get(next)))
						{
							break;
						}
						next = prev.get(next);
					}
					return path; 
				}
				current = current.next;				
			}
			
		}		
		return path;
	}
	
	
	private static Person getPerson(Graph g, String name)
	{
		return g.members[g.map.get(name)];
	}
	
	private static String getName (Graph g, Friend f) 
	{
		return g.members[f.fnum].name;
	}
	
    
	/**
	 * Finds all cliques of students in a given school.
	 * 
	 * Returns an array list of array lists - each constituent array list contains
	 * the names of all students in a clique.
	 * 
	 * @param g Graph for which cliques are to be found.
	 * @param school Name of school
	 * @return Array list of clique array lists. Null if there is no student in the
	 *         given school
	 */
	public static ArrayList<ArrayList<String>> cliques(Graph g, String school) 
	{
		
		//case sensitivity
			school = school.toLowerCase();
			ArrayList<ArrayList<String>> cliques = new ArrayList<ArrayList<String>>();
			if (school == null) return cliques;
			
			int size = g.members.length;
			ArrayList<Person> unvisited = new ArrayList<Person>();
			ArrayList<Person> q = new ArrayList<Person>();
			
			if (school != null)
			{
				for(int i=0; i < size; i++)
				{
					if (school.equals(g.members[i].school)) 
					{
						unvisited.add(g.members[i]);
					}
				}				
			}
			 Person current = null;
			 while (!unvisited.isEmpty())
				{
				ArrayList<String> clique = new ArrayList<String>();
				q.add(unvisited.remove(0));
				clique.add(q.get(0).name);

				while (!q.isEmpty())
				{
					if (!q.isEmpty())
						current = q.remove(0);
						Friend f = current.first;	
					do {
							Person p = g.members[f.fnum];
							if (unvisited.contains(p))
							{
								unvisited.remove(p);
								clique.add(p.name);
								q.add(p);
							}	
					   } while((f = f.next) != null);
				}
				cliques.add(clique);
				}
				return cliques;	
	}
				
	/**
	 * Finds and returns all connectors in the graph.
	 * 
	 * @param g Graph for which connectors needs to be found.
	 * @return Names of all connectors. Null if there are no connectors.
	 */
	public static ArrayList<String> connectors(Graph g)
	{
		ArrayList<String> connectors = new ArrayList<String>();
		for (String person : g.map.keySet()){
			int count = 1;
			DFS(g, connectors, person, new HashMap<String, Boolean>(), new HashMap<String, Integer>(), new HashMap<String, Integer> (), count);
		}
		return connectors;
	}
	
	private static void DFS(Graph g, ArrayList<String> connectors, String name, HashMap<String,Boolean> visited, HashMap<String,Integer> num, HashMap<String, Integer> back, int count)
	{
		//count is for giving the dfsnum and back
		//this is normal dfs being done
		visited.put(name, true);
		num.put(name, count);
		back.put(name, count);
		count++;
		Person p = g.members[g.map.get(name)];
		Friend current = p.first;
		
		while(current != null)
		{
		 // if we didnt visit
		  if(visited.get(getName(g, current)) == null) 
		  {
		  	  DFS(g, connectors, getName(g, current), visited, num, back, count);
		  	  if(num.get(name) != 1) 
		  	  {
		  	  	  if(back.get(name) > back.get(getName(g, current))) 
		  	  	  {
		  	  	  	  back.put(name, back.get(getName(g, current)));
		  	  	  } 
		  	  	  if(!connectors.contains(name) && num.get(name) <= back.get(getName(g, current)))
		  	  	  {
		  	  	  	  connectors.add(name);
		  	  	  }
		  	  }
		  } 
		  else
		  { //if we did visit
		  	  if(back.get(name) > num.get(getName(g, current))) 
		  	  {  //want back(v) = min(back(v),dfsnum(w))
		  	  	  back.put(name, num.get(getName(g, current)));
		  	  }
		  }
		  current = current.next;
		}
		if(num.get(name) == 1)
		{
			return;
		}
		
	}
	
}
