import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;
/**
 * A class that contains several imlementations of graph algorithms.
 * @author Matthew Kemp
 */
public class GraphAlgos {
	/**
	 * Naive algorithm to create an Adjacency List from a String array where each index in the array 
	 * represents a vertex in our graph. Two vertexs are considered connected if they
	 * differ by only 1 character. Runs in O(n^2) time, where n is the size of data.
	 * @param data array of strings with each index representing a vertex
	 * @return A HashMap representing an adjacency list where the keys are the vertex and the value for each key is an ArrayList of vertices (Strings)
	 */
	public static HashMap<String, ArrayList<String>> createAdjList(String[] data){
		HashMap<String, ArrayList<String>> adjList = new HashMap<String, ArrayList<String>>();
		for(int i = 0; i < data.length; i++) {
			if(adjList.get(data[i])==null){
				adjList.put(data[i], new ArrayList<String>());
			}
			for(int j = i+1; j < data.length; j++) {
				if(DistanceFunctions.editDist(data[i], data[j])) {
					adjList.get(data[i]).add(data[j]);
					if(adjList.get(data[j])==null) {
						adjList.put(data[j], new ArrayList<String>());
					}
					adjList.get(data[j]).add(data[i]);
				}
			}
		}
		return adjList;
	}
	/**
	 * A function that performs a depth-first-search of a HashMap representation of an adjacency list.
	 * Returns the largest connected set of vertices.
	 * @param adjList A HashMap representation of a adjacency list.
	 * @return A set containing every vertex in the largest connected set in the adjacency list.
	 */
	public static Set<String> dfs(HashMap<String, ArrayList<String>> adjList) {
		Set<String> visited = new HashSet<String>();
		Set<String> maxContents = new HashSet<String>();
		Set<String> currentContents = new HashSet<String>();
		//visit every vertex in our graph
		for(String e : adjList.keySet()) {
			//if the vertex we are currently on hasn't been visited then we have a new connected
			//set of vertices we need to explore.
			if(!visited.contains(e)) {
				currentContents = new HashSet<String>();
				/*we use a stack to perform our dfs, we do this to avoid using recursion since recursion comes
				  with a lot of overhead. Recursion also has a limit to the depth it can search, depending on 
				  the amount of virtual memory allocated to the stack: https://stackoverflow.com/questions/4734108/what-is-the-maximum-depth-of-the-java-call-stack
				  this can result in recursion being limited to only thousands of calls in a typical program.
				*/
				Stack<String> stack = new Stack<String>();
				stack.push(e);
				visited.add(e);
				while(!stack.empty()) {
					String currVertex = stack.pop();
					currentContents.add(currVertex);
					ArrayList<String> adjacent = adjList.get(currVertex);
					for(String adj : adjacent) {
						if(!visited.contains(adj)) {
							stack.push(adj);
							visited.add(adj);
						}
					}
				}
				//if the current connected set is larger than our max, set max to the current connected set.
				if(currentContents.size() > maxContents.size()) {
					maxContents = currentContents;
				}
			}
		}
		return maxContents;
	}
	/**
	 * A function that performs a breadth-first-search of a HashMap representation of an adjacency list.
	 * Returns the largest connected set of vertices. 
	 * @param adjList A HashMap representation of a adjacency list.
	 * @return A set containing every vertex in the largest connected set in the adjacency list.
	 */
	public static Set<String> bfs(HashMap<String, ArrayList<String>> adjList){
		Set<String> visited = new HashSet<String>();
		Set<String> maxContents = new HashSet<String>();
		Set<String> currentContents = new HashSet<String>();
		//visit every vertex in our graph
		for(String e : adjList.keySet()) {
			if(!visited.contains(e)) {
				currentContents = new HashSet<String>();
				/*
				 * we use a queue to perform our bfs, we use a queue to avoid using recursion as
				 * recursion comes with a lot of overhead. Recursion also has a limit to the depth it can search, depending on 
				 * the amount of virtual memory allocated to the stack: https://stackoverflow.com/questions/4734108/what-is-the-maximum-depth-of-the-java-call-stack
				 * this can result in recursion being limited to only thousands of calls in a typical program.
				 */
				Queue<String> queue = new LinkedList<String>();
				visited.add(e);
				queue.add(e);
				while(!queue.isEmpty()) {
					String currVertex = queue.remove();
					currentContents.add(currVertex);
					ArrayList<String> adjacent = adjList.get(currVertex);
					for(String adj : adjacent) {
						if(!visited.contains(adj)) {
							queue.add(adj);
							visited.add(adj);
						}
					}
				}
				//if the current connected set is larger than our max, set max to the current connected set.
				if(currentContents.size() > maxContents.size()) {
					maxContents = currentContents;
				}
			}
		}
		return maxContents;
	}
	/**
	 * A function that performs a depth-first-search and prints and the order vertices are visited on a HashMap representation of an adjacency list.
	 * Returns the largest connected set of vertices.
	 * @param adjList A HashMap representation of a adjacency list.
	 * @return A set containing every vertex in the largest connected set in the adjacency list.
	 */
	public static Set<String> dfsPrint(HashMap<String, ArrayList<String>> adjList) {
		Set<String> visited = new HashSet<String>();
		Set<String> maxContents = new HashSet<String>();
		Set<String> currentContents = new HashSet<String>();
		//visit every vertex in our graph
		for(String e : adjList.keySet()) {
			//if the vertex we are currently on hasn't been visited then we have a new connected
			//set of vertices we need to explore.
			if(!visited.contains(e)) {
				currentContents = new HashSet<String>();
				/*we use a stack to perform our dfs, we do this to avoid using recursion since recursion comes
				  with a lot of overhead. Recursion also has a limit to the depth it can search, depending on 
				  the amount of virtual memory allocated to the stack: https://stackoverflow.com/questions/4734108/what-is-the-maximum-depth-of-the-java-call-stack
				  this can result in recursion being limited to only thousands of calls in a typical program.
				*/
				Stack<String> stack = new Stack<String>();
				stack.push(e);
				visited.add(e);
				while(!stack.empty()) {
					String currVertex = stack.pop();
					System.out.print(currVertex +"->");
					currentContents.add(currVertex);
					ArrayList<String> adjacent = adjList.get(currVertex);
					for(String adj : adjacent) {
						if(!visited.contains(adj)) {
							stack.push(adj);
							visited.add(adj);
						}
					}
				}
				//if the current connected set is larger than our max, set max to the current connected set.
				if(currentContents.size() > maxContents.size()) {
					maxContents = currentContents;
				}
			}
		}
		System.out.println();
		return maxContents;
	}
	/**
	 * A function that performs a breadth-first-search and prints the order vertices are visited on a HashMap representation of an adjacency list.
	 * Returns the largest connected set of vertices. 
	 * @param adjList A HashMap representation of a adjacency list.
	 * @return A set containing every vertex in the largest connected set in the adjacency list.
	 */
	public static Set<String> bfsPrint(HashMap<String, ArrayList<String>> adjList){
		Set<String> visited = new HashSet<String>();
		Set<String> maxContents = new HashSet<String>();
		Set<String> currentContents = new HashSet<String>();
		//visit every vertex in our graph
		for(String e : adjList.keySet()) {
			if(!visited.contains(e)) {
				currentContents = new HashSet<String>();
				/*
				 * we use a queue to perform our bfs, we use a queue to avoid using recursion as
				 * recursion comes with a lot of overhead. Recursion also has a limit to the depth it can search, depending on 
				 * the amount of virtual memory allocated to the stack: https://stackoverflow.com/questions/4734108/what-is-the-maximum-depth-of-the-java-call-stack
				 * this can result in recursion being limited to only thousands of calls in a typical program.
				 */
				Queue<String> queue = new LinkedList<String>();
				visited.add(e);
				queue.add(e);
				while(!queue.isEmpty()) {
					String currVertex = queue.remove();
					System.out.print(currVertex + "->");
					currentContents.add(currVertex);
					ArrayList<String> adjacent = adjList.get(currVertex);
					for(String adj : adjacent) {
						if(!visited.contains(adj)) {
							queue.add(adj);
							visited.add(adj);
						}
					}
				}
				//if the current connected set is larger than our max, set max to the current connected set.
				if(currentContents.size() > maxContents.size()) {
					maxContents = currentContents;
				}
			}
		}
		System.out.println();
		return maxContents;
	}
	/**
	 * A function that compares two sets to see if they are equal.
	 * @param set1 A string set
	 * @param set2 A string set
	 * @return whether the two sets are equal
	 */
	public static boolean setIsEqual(Set<String> set1, Set<String> set2) {
		/*
		 * In our implementation we only need to check if the sets are the
		 * same size and that they contain the same elements to check equallity.
		 */
		if(set1.size()!=set2.size()) {
			return false;
		}
		for(String e : set1) {
			if(!set2.contains(e)) {
				return false;
			}
		}
		return true;
	}
	/**
	 * Calculates the edge weight of two vertices by finding the minimum magnitude of the 
	 * two vertices. In this implementation the magnitude of a string is the sum of each of 
	 * characters numerical values.
	 * @param s1 A vertex represented by a string
	 * @param s2 A vertex represented by a string
	 * @return the edge weight of the two verticies
	 */
	public static int getEdgeWeight(String s1, String s2) {
		int weight1 = 0;
		int weight2 = 0;
		int size;
		/*we need to check which string is larger so that we 
		 * can functionally for a more general set of vertices.
		 * In our problem the size of vertices is limited but you could
		 * also have a problem with differn't length vertices.
		*/
		if(s1.length() >= s2.length()) {
			size = s1.length();
		}
		else {
			size = s2.length();
		}
		for(int i = 0; i < size; i++) {
			if(i < s1.length()) {
				weight1 += Character.getNumericValue(s1.charAt(i));
			}
			if(i < s2.length()) {
				weight2 += Character.getNumericValue(s2.charAt(i));
			}
		}
		if(weight1 < weight2) {
			return weight1;
		}
		return weight2;
	}
	/**
	 * A method that use a implementation of prim's algorithm to generate a 
	 * minimum spanning tree from a set of vertices and an adjacency list.
	 * Algorithm written from: https://en.wikipedia.org/wiki/Prim%27s_algorithm
	 * @param adjList adjacency list for the set of vertices we want to create a m.s.t. for
	 * @param conSet the set of vertices we want to create a m.s.t. for
	 * @return
	 */
	public static int prim(HashMap<String, ArrayList<String>> adjList, Set<String> conSet) {
		//just a sanity check to make sure the logic works
		int count = 0;
		int total = 0;
		//copy the largest connected set into unvisted so that we can modify unvisited without
		//effecting the largest connected set.
		Set<String> unvisited = new HashSet<String>();
		unvisited.addAll(conSet);
		HashMap<String, Integer> cheapestCost = new HashMap<String, Integer>();
		//technically we don't need this since we don't actually return the m.s.t.
		//but I left it in because it makes it easy to add that functionality in the future.
		//and all the logic is already set up for it.
		HashMap<String, String> cheapestEdge = new HashMap<String, String>();
		Iterator<String> tempIter = unvisited.iterator();
		String temp = tempIter.next();
		unvisited.remove(temp);
		cheapestCost.put(temp, Integer.MAX_VALUE);
		cheapestEdge.put(temp, null);
		for(String w : adjList.get(temp)) {
			int edgeWeight = getEdgeWeight(temp,w);
			cheapestCost.put(w, edgeWeight);
			cheapestEdge.put(w,temp);
		}
		//main logic of prim's algo
		while(!unvisited.isEmpty()) {
			int minWeight = Integer.MAX_VALUE;
			String minVertex = null;
			//find the lowest edgeWeight of unvisited nodes
			for(String e : unvisited) {
				if(cheapestCost.get(e)!=null) {
					if(cheapestCost.get(e) < minWeight) {
						minWeight = cheapestCost.get(e);
						minVertex = e;
					}
				}
			}
			unvisited.remove(minVertex);
			total += minWeight;
			count++;
			//search all neighbors of the closest current vertex, if they aren't in cheapest cost 
			//or their cheapest cost is less than the edge weight of it and the current closest vertex
			//add or update their cheapest cost to be the edge weight of it and the current closest vertex.
			for(String w : adjList.get(minVertex)) {
				int edgeWeight = getEdgeWeight(minVertex,w);
				if(cheapestCost.get(w)==null || cheapestCost.get(w) > edgeWeight) {
					cheapestCost.put(w,edgeWeight);
					cheapestEdge.put(w, minVertex);
				}
			}
		}
		System.out.println(count);
		return total;
	}
	//extra
	/**
	 * Generates a Trie from a set of data represented in a array of strings. does this in O(n) time for our problem set.
	 * @param data an array of strings representing our data
	 * @return a Trie representation of our data
	 */
	public static Trie createTrie(String[] data) {
		Trie root = new Trie();
		for(String e : data) {
			root.add(e);
		}
		return root;
	}
	/**
	 * Generates an Adjacency List from a trie and a array of strings representing our data. A bit jank due to time limit.
	 * Runtime: O(n)
	 * @param trie
	 * @param data
	 * @return
	 */
	public static HashMap<String,Set<String>> createAdjListTrie(Trie trie, String[] data){
		HashMap<String, Set<String>> adjList = new HashMap<String, Set<String>>();
		for(String e : data) {
			adjList.put(e,new HashSet<String>());
			TrieNode current = trie.getRoot();
			for(int i = 0; i < e.length(); i++) {
				for(TrieNode child : current.getChildren()) {
					if(child.getVal()!=' ') {
						if(Trie.searchNode(child, e.substring(i+1))) {
							String neighbor = e.substring(0, i) + child.getVal() + e.substring(i+1);
							if(!neighbor.equals(e)) {
								adjList.get(e).add(neighbor);
							}
						}
					}
				}
				current = current.getChild(e.charAt(i));
			}
		}
		return adjList;
	}
}
