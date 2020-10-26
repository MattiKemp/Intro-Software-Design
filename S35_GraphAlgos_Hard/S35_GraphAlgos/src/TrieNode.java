import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
/**
 * A class implementing a basic prefix tree (trie) node.
 * Has two instance variables to represent the val of the node and 
 * the children of the node.
 * @author Matthew Kemp
 *
 */
public class TrieNode{
	private char val;
	private HashMap<Character, TrieNode> children;
	/**
	 * No parameter constructor for a Trie Node.
	 * Initializes the value of the Trie Node to an black space char.
	 * The blank space char is used to represent the root node of a Trie and
	 * where a word ends in a Trie. For example when a word is added to a Trie
	 * the final node created will be a default Trie Node to represent a leaf of
	 * where the word stopped.
	 * Initializes the children HashMap to an empty HashMap.
	 */
	public TrieNode() {
		val = ' ';
		children = new HashMap<Character, TrieNode>();
	}
	/**
	 * The constructor for a Trie Node with a specific value.
	 * Initializes value of the TrieNode to the inputted char.
	 * Initializes the children of the node to an empty HashMap.
	 * @param val
	 */
	public TrieNode(char val) {
		this.val = val;
		children = new HashMap<Character, TrieNode>();
	}
	/**
	 * The getter method for the val instance variable.
	 * Returns the current value of this Node.
	 * @return the current char value of the Trie Node
	 */
	public char getVal() {
		return val;
	}
	/**
	 * Get the specific child of this Node with the specified input value.
	 * If this node has no children of that value return null. 
	 * @param n Value of child you want to retrieve.
	 * @return The child TrieNode you specified or null.
	 */
	public TrieNode getChild(char n) {
		return children.get(n);
	}
	/**
	 * Get all of the children of this TrieNode
	 * @return A collection of all the children TrieNodes, not ordered.
	 */
	public Collection<TrieNode> getChildren() {
		return children.values();
	}
	/**
	 * Public setter method for the value instance variable of this TrieNode
	 * @param val The value you want to change this node's value to.
	 */
	public void setVal(char val) {
		this.val = val;
	}
	/**
	 * Add a child to this node.
	 * @param n The TrieNode you want add as a child.
	 */
	public void addChild(TrieNode n) {
		children.put(n.getVal(),n);
	}
}
