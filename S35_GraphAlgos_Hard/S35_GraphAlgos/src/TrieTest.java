import java.util.Set;
/**
 * The main driver class for our Trie data structure.
 * @author Matthew Kemp
 *
 */
public class TrieTest {
	/**
	 * The main method to test the Trie data structure.
	 * Creates a trie using the GraphAlgo method createTrie with a easy
	 * to test data set of strings. It then prints out the Trie in level order
	 * to show the validity of the logic in the TrieNode and Trie class.
	 * @param args
	 */
	public static void main(String[] args) {
		//TrieNode n = new TrieNode();
		//n.addChild(new TrieNode('a'));
		//TrieNode child = n.getChild('a');
		//System.out.println(child.getVal());
		
		Trie tree = GraphAlgos.createTrie(new String[]{"abc","acb","bca","bbc","abd","acc"});
		tree.print();
	}
}
