package unit3.binarysorttree;

import java.util.Random;

/**  
 * This program makes a random binary sort tree containing 1023 random
 * real numbers.  It then computes the height of the tree and the
 * average depth of the leaves of the tree.  Hopefully, the average
 * depth will tend to be close to 9, which is what it would be
 * if the tree were perfectly balanced.  The height of the tree,
 * which is the same as the maximum depth of any leaf, can be
 * significantly larger.
 * 
 * This non-graded Learning Journal Exercise is adapted from
 * David Eck's sample solution found here:
 * 
 * http://math.hws.edu/eck/cs124/javanotes7/c9/ex5-ans.html
 * 
 * @author Ryan Coon
 *
 */
public class BinarySortTreeTest {

	/**
	 * An object of type TreeNode represents one node in a binary tree of doubles.
	 */
	private static class TreeNode {
		double number;     // The data in this node.
		TreeNode left;     // Pointer to the left subtree.
		TreeNode right;    // Pointer to the right subtree.
		
		// Constructor.  Make a node containing the specified number.
		// Left and right pointers are initially null.
		TreeNode(double num) {
			number = num;
		}
	}  // end nested class TreeNode
	
	// Pointer to the root node in a binary tree.
    // This tree is used in this program as a 
    // binary sort tree.  When the tree is empty, 
    // root is null (as it is initially).
	private static TreeNode root;
	
	// Instantiate a Random() object.
	private static Random rGen = new Random();
	
	/**
	 * Return the number of leaves in the tree to which node points.
	 * 
	 * @param node TreeNode for counting leaves.
	 * @return The number of leaves in the tree.
	 */
	private static int countLeaves(TreeNode node) {
		if (node == null) {
			return 0;
		} else {
			// node is a leaf.
			if (node.left == null && node.right == null) {
				return 1;
			} else {
				return countLeaves(node.left) + countLeaves(node.right); 
			}
		}
	}
	
	/**
	 * When called as sumLeafDepths(root,0), this will compute the
	 * sum of the depths of all the leaves in the tree to which root
	 * points.  When called recursively, the depth parameter gives
	 * the depth of the node, and the routine returns the sum of the
	 * depths of the leaves in the subtree to which node points.
	 * In each recursive call to this routine, depth goes up by one.
	 */
	private static int sumLeafDepths(TreeNode node, int depth) {
		if (node == null) {
			return 0;
		} else if (node.left == null && node.right == null) {
			return depth;
		} else {
			return sumLeafDepths(node.left, depth + 1)
				+ sumLeafDepths(node.right, depth + 1);
		}
	}
	
   /**
    * When called as maxDepth(root,0), this will compute the
    * max of the depths of all the leaves in the tree to which root
    * points.  When called recursively, the depth parameter gives
    * the depth of the node, and the routine returns the max of the
    * depths of the leaves in the subtree to which node points.
    * In each recursive call to this routine, depth goes up by one.
    */
	private static int maxDepth(TreeNode node, int depth) {
		if (node == null) {
			return 0;
		} else if (node.left == null && node.right == null) {
			return depth;
		} else {
			int maxLeft = maxDepth(node.left, depth + 1);
			int maxRight = maxDepth(node.right, depth + 1);
			if (maxLeft > maxRight) {
				return maxLeft;
			} else {
				return maxRight;
			}
		}
	}
	
	 /**
     * Add the item to the binary sort tree to which the global variable 
     * "root" refers.  (Note that root can't be passed as a parameter to 
     * this routine because the value of root might change, and a change 
     * in the value of a formal parameter does not change the actual parameter.)
     */
    private static void treeInsert(double num) {
        if ( root == null ) {
                // The tree is empty.  Set root to point to a new node containing
                // the new item.  This becomes the only node in the tree.
            root = new TreeNode( num );
            return;
        }
        TreeNode runner;  // Runs down the tree to find a place for newItem.
        runner = root;   // Start at the root.
        while (true) {
            if ( num < runner.number ) {
                    // Since the new item is less than the item in runner,
                    // it belongs in the left subtree of runner.  If there
                    // is an open space at runner.left, add a new node there.
                    // Otherwise, advance runner down one level to the left.
                if ( runner.left == null ) {
                    runner.left = new TreeNode( num );
                    return;  // New item has been added to the tree.
                }
                else
                    runner = runner.left;
            }
            else {
                    // Since the new item is greater than or equal to the item in
                    // runner it belongs in the right subtree of runner.  If there
                    // is an open space at runner.right, add a new node there.
                    // Otherwise, advance runner down one level to the right.
                if ( runner.right == null ) {
                    runner.right = new TreeNode( num );
                    return;  // New item has been added to the tree.
                }
                else
                    runner = runner.right;
            }
        } // end while
    }  // end treeInsert()
    
    /**
     * Create the random tree and print the data.
     * 
     * @param args Command line args (not used).
     */
    public static void main(String[] args) {
		
    	// Start with an empty tree.  Root is a global
        // variable, defined at the top of the class.
    	root = null;
    	
    	for (int i = 0; i < 1023; i++) {
    		treeInsert(rGen.nextDouble());
    	}
    	
    	int leafCount = countLeaves(root);
    	int sumOfDepths = sumLeafDepths(root, 0);
    	int depthMax = maxDepth(root, 0);
    	double averageDepth = ((double) sumOfDepths / leafCount);
    	
    	System.out.println("Number of leaves: " + leafCount);
    	System.out.println("Average depth of leaves: " + averageDepth);
    	System.out.println("Maximum depth of leaves: " + depthMax);
	}
}