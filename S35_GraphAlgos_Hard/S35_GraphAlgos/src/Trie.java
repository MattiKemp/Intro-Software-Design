import java.util.Collection;
import java.util.LinkedList;
import java.util.Queue;
/**
 * A representation of a Trie data structure. 
 * Keeps track of the root of our Trie for ease of use.
 * @author Matthew Kemp
 */
public class Trie {
	private TrieNode root;
	/**
	 * No parameter constructor for our Trie data structure.
	 * initalizes the root instance variable to a default TrieNode.
	 */
	public Trie() {
		root = new TrieNode();
	}
	/**
	 * Public getter method for the root of the Trie
	 * @return root node
	 */
	public TrieNode getRoot() {
		return root;
	}
	/**
	 * Method to add a new string to our Trie data structure.
	 * @param n String being added to the Trie
	 */
	public void add(String n) {
		TrieNode current = root;
		//Compares each character in the string to the current TrieNode we are at's children.
		for(int i = 0; i < n.length(); i++) {
			 /* If it doesn't have a child
			 * with the same value, then we create a new child with the same value as the current character we are at and visit
			 * that new Node.
			 */
			if(current.getChild(n.charAt(i))==null) {
				current.addChild(new TrieNode(n.charAt(i)));
				current = current.getChild(n.charAt(i));
			}
			/*If the TrieNode we are at has a child with the same value as the current character we are examining
			 , then we visit the that child TrieNode and check the next character in the string.
			 */
			else {
				current = current.getChild(n.charAt(i));
			}
		}
		//Once we reach the end of the String add a default TrieNode to represent the end of a string in our Trie.
		current.addChild(new TrieNode());
	}
	/**
	 * Takes a String and return whether that string is represented in the Trie
	 * @param n String to search
	 * @return whether the String is in the Trie
	 */
	public boolean search(String n) {
		TrieNode current = root;
		for(int i = 0; i < n.length(); i++) {
			if(current.getChild(n.charAt(i))==null) {
				return false;
			}
			else {
				current = current.getChild(n.charAt(i));
			}
		}
		return true;
	}
	/**
	 * Takes a TrieNode and returns whether the string exists in node.
	 * @param node The node to search through
	 * @param n The string to search
	 * @return whether the string is represented in the node
	 */
	public static boolean searchNode(TrieNode node, String n) {
		for(int i = 0; i < n.length(); i++) {
			if(node.getChild(n.charAt(i))==null) {
				return false;
			}
			else {
				node = node.getChild(n.charAt(i));
			}
		}
		return true;
	}
	/**
	 * Print each level of the Trie.
	 */
	public void print() {
		Queue<TrieNode> queue = new LinkedList<TrieNode>();
		queue.add(root);
		int currentLevel = 1;
		int nextLevel = 0;
		while(!queue.isEmpty()) {
			TrieNode temp = queue.remove();
			System.out.print(" " + temp.getVal() + " ");
			currentLevel--;
			Collection<TrieNode> children = temp.getChildren();
			for(TrieNode e : children) {
				queue.add(e);
				nextLevel++;
			}
			if(currentLevel == 0) {
				System.out.println();
				System.out.println("======");
				currentLevel = nextLevel;
				nextLevel = 0;
			}
		}
	}
}
