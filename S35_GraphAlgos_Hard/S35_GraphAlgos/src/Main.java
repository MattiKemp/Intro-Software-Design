import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
/**
 * The main driver class for this problem.
 * @author Matthew Kemp
 *
 */
public class Main {
	/**
	 * main method for the entire program, Solves the easy, medium and hard problems at:
	 * https://uiowa.instructure.com/courses/156825/pages/s35-graphalgos
	 * It also tests a prefix tree data structure that I wrote for quicker generation of adjacency lists.
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("=====Easy=====");
		
		String[] data = ReadData.readData("words.dat");
		System.out.println("total data size:" + data.length);
		HashMap<String, ArrayList<String>> adjList = GraphAlgos.createAdjList(data);
		int totalEdges = 0;
		int noEdges = 0;
		int currentMax = 0;
		ArrayList<String> maxWords = new ArrayList<String>();
		for(String e : adjList.keySet()) {
			int size = adjList.get(e).size();
			if(size==0) {
				noEdges++;
			}
			totalEdges+=size;
			if(size > currentMax) {
				currentMax = size;
				maxWords = new ArrayList<String>();
			}
			if(size == currentMax) {
				maxWords.add(e);
			}
		}
		System.out.println("average amount of edges:" + totalEdges/adjList.size());
		System.out.println("number of vertices with no edges:" + noEdges);
		System.out.println("maximum amount of edges on any vertex:" + currentMax);
		
		System.out.println();
		System.out.println("=====Medium=====");
		System.out.println();

		Set<String> maxConSetDfs = GraphAlgos.dfs(adjList);
		System.out.println("number of vertices in the largest set of connected vertices found with dfs:" + maxConSetDfs.size());
		Set<String> maxConSetBfs = GraphAlgos.bfs(adjList);
		System.out.println("number of vertices in the largest set of connected vertices found with bfs:" + maxConSetBfs.size());
		System.out.println("did dfs and bfs return the same largest connected set:" + GraphAlgos.setIsEqual(maxConSetDfs, maxConSetBfs));
		System.out.println();
		System.out.println("our max connected set:");
		for(String e : maxConSetBfs) {
			System.out.print(" " + e + " ");
		}
		System.out.println();
		System.out.println();

		System.out.println("How does dfs look through a graph?");
		System.out.println("Dfs will visit a nodes neighbor until there are no more neigbors to visit, it will then back track.");
		System.out.println();

		HashMap<String,ArrayList<String>> testAdjList = new HashMap<String,ArrayList<String>>();
		ArrayList<String> tempNeighbors = new ArrayList<String>();
		tempNeighbors.add("b");
		tempNeighbors.add("c");
		testAdjList.put("a",tempNeighbors);
		tempNeighbors = new ArrayList<String>();
		tempNeighbors.add("d");
		tempNeighbors.add("e");
		testAdjList.put("b",tempNeighbors);
		tempNeighbors = new ArrayList<String>();
		tempNeighbors.add("f");
		tempNeighbors.add("g");
		testAdjList.put("c",tempNeighbors);
		tempNeighbors = new ArrayList<String>();
		testAdjList.put("d",tempNeighbors);
		testAdjList.put("e",tempNeighbors);
		testAdjList.put("f",tempNeighbors);
		testAdjList.put("g",tempNeighbors);
		
		System.out.println("here is simple adjacency list:");
		for(String e : testAdjList.keySet()) {
			System.out.print(e +":[");
			for(String neighbor : testAdjList.get(e)) {
				System.out.print(neighbor+",");
			}
			System.out.print("]");
			System.out.println();
		}
		System.out.println();
		System.out.println("Here is the order of the vertices dfs will visit:");
		GraphAlgos.dfsPrint(testAdjList);
		System.out.println("Notice how it backtracks to the previous set of neighbors when it runs out.");
		System.out.println("How does bfs look through a graph?");
		System.out.println("Here is the order of the vertices bfs will visit in the same Graph:");
		GraphAlgos.bfsPrint(testAdjList);
		System.out.println("Notice how all current neighbors are visited before it continues.");
		System.out.println();
		
		System.out.println("=====Hard=====");
		
		int mst = GraphAlgos.prim(adjList, maxConSetBfs);
		System.out.println("mst total weight:" + mst);
		
		//similar to the example in: https://en.wikipedia.org/wiki/Prim%27s_algorithm
		//but the result will be much different due to how our edges are calculated
		testAdjList = new HashMap<String,ArrayList<String>>();
		tempNeighbors = new ArrayList<String>();
		tempNeighbors.add("B");
		tempNeighbors.add("D");
		testAdjList.put("A", tempNeighbors);
		tempNeighbors = new ArrayList<String>();
		tempNeighbors.add("A");
		tempNeighbors.add("D");
		testAdjList.put("B", tempNeighbors);
		tempNeighbors = new ArrayList<String>();
		tempNeighbors.add("A");
		tempNeighbors.add("B");
		tempNeighbors.add("C");
		testAdjList.put("D", tempNeighbors);
		tempNeighbors = new ArrayList<String>();
		tempNeighbors.add("D");
		testAdjList.put("C", tempNeighbors);
		
		Set<String> testConSet = new HashSet<String>();
		testConSet.add("A");
		testConSet.add("B");
		testConSet.add("C");
		testConSet.add("D");

		mst = GraphAlgos.prim(testAdjList,testConSet);
		System.out.println("mst total weight:" + mst);
		System.out.println();
		System.out.println("=====Extra=====");
		System.out.println();

		/*creates a prefix tree (trie) with the data set, this allows us to find the neighbors much quicker: O(l*n^2) -> O((l^2)*n)?
		* where n is the number of verticies and l is the length of the string we are finding the neighbors of. Since in this 
		* example our verticies are of constant character size the neighbor lookup time is linear: O(25n) -> O(n) creation time for the 
		* Trie is only O(5*n) -> O(n), hypothetically you can also generate the Trie instead of the array of strings when reading from 
		* words.dat so it will use <= the space to store all of the strings in a array. If our vertexs can only hold a limited character set, such
		* as ascii, our storage will be O(1) for our data set since ascii can only hold 128 different characters and our length is limited so our space will
		* be at worst O(128^5) which is a constant. The naive string array solution will take up O(n*5) = O(n) */
		Trie trie = GraphAlgos.createTrie(data);
		trie = ReadData.readTrie("words.dat");
		//This code is a bit jank because I was very rushed
		//You can also generate the Adjacency list with just the trie data but I didn't have enough time to implement it.
		HashMap<String, Set<String>> adjListTrie = GraphAlgos.createAdjListTrie(trie, data);
		System.out.println("trie adjacency list size:" + adjListTrie.size());
		
		totalEdges = 0;
		noEdges = 0;
		currentMax = 0;
		maxWords = new ArrayList<String>();
		for(String e : adjListTrie.keySet()) {
			int size = adjListTrie.get(e).size();
			if(size==0) {
				noEdges++;
			}
			totalEdges+=size;
			if(size > currentMax) {
				currentMax = size;
				maxWords = new ArrayList<String>();
			}
			if(size == currentMax) {
				maxWords.add(e);
			}
		}
		System.out.println("average amount of edges:" + totalEdges/adjListTrie.size());
		System.out.println("number of vertices with no edges:" + noEdges);
		System.out.println("maximum amount of edges on any vertex:" + currentMax);
		
	}
}
