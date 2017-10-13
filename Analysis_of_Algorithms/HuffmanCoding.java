package class12;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.PriorityQueue;
import java.util.Set;

/*
 * Given a set C of characters, and a frequency f[c] for each character.
 * Encode each character c by a binary string code(c) so that no codeword
 * is a prefix of another codeword. The encoding minimizes sum(f[c]*len(code(c)))
 */
public class HuffmanCoding {

	public static void main(String[] args) {
		Map<Character, Integer> table = new HashMap<>();
		table.put('a', 14);
		table.put('b', 6);
		table.put('c', 7);
		table.put('d', 8);
		table.put('e', 12);
		encode(table);
	}
	/*
	 * Greedy algorithm:
	 * Intuition: Least frequent character should be deep in the tree
	 * Select the two least frequent characters and merge them into a new node.
	 * Ignored corner cases.
	 */
	public static void encode(Map<Character, Integer> table){
		Map<Integer, TreeNode> treemap = new HashMap<>();
		PriorityQueue<Integer> pq = new PriorityQueue<>();
		Set<Entry<Character, Integer>> set = table.entrySet();
		Iterator<Entry<Character, Integer>> ite = set.iterator();
		while(ite.hasNext()){
			Entry<Character, Integer> entry = ite.next();
			// Add the frequency into priority queue
			pq.add(entry.getValue());
			// Create a tree node for each character
			treemap.put(entry.getValue(), new TreeNode(entry.getKey()));		
		}
		TreeNode root = null;
		while(pq.size() > 1){
			// pop out the two nodes with minimum frequency
			int min1 = pq.poll();
			int min2 = pq.poll();
			TreeNode left = treemap.get(min1);
			TreeNode right = treemap.get(min2);
			TreeNode dummy = new TreeNode((char)0);
			dummy.left = left;
			dummy.right = right;
			pq.add(min1 + min2);	// the synthesized node has added frequency
			treemap.put(min1 + min2, dummy);
			root = dummy;
		}
		preorder(root, "");
	}
	
	public static void preorder(TreeNode root, String s){
		if (root == null)
			return;
		if (root.c != (char)0)
			System.out.println("" + root.c + " code is:" + s);
		preorder(root.left, s + "0");
		preorder(root.right, s + "1");
	}
}

class TreeNode{
	TreeNode left;
	TreeNode right;
	char c;
	public TreeNode(char c){
		this.c = c;
	}
}
