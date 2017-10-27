package balancdBinarySearchTree;

import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
/**
 * @author Spencer Collins
 * Date: 4/18/17
 *
 * @param <T>
 */
public class BalancedBinarySearchTree<T> implements Iterable<T> {

    // Attributes
    private BalancedBinarySearchTreeNode<T> root;
    private final Comparator<T> comparator;
    
    // Constructor
    public BalancedBinarySearchTree(Comparator<T> comparator){
        this.comparator = comparator;
        this.clear();
    }
    
    // Access Methods
    /**
     * Insert data into and balance the binary tree
     * @param data
     * @return true if data was added to the tree
     */
    public boolean insert(T data) {
        // Create a new node with the data that is to be inserted
        BalancedBinarySearchTreeNode<T> newNode = new BalancedBinarySearchTreeNode<T>(data);
        
        // If this is the first element in the tree, set it as the root
        if (this.root == null) {
            this.root = newNode;
            this.root.setHeight(0);
            return true;
        }
        
        // Create a node at the root that will be the current node as we walk the tree
        BalancedBinarySearchTreeNode<T> node = this.root;
        
        // Walk down the tree to find the correct spot to place the node
        while (node != null) {
            
            // If the new data is supposed to be left of the current node
            if (this.comparator.compare(newNode.getData(), node.getData()) < 0) {
                
                // If there is a node to the left of the current node, traverse to it
                if (node.getLeftNode() != null) {
                    node = node.getLeftNode();
                }
                // Or place the current node
                else {
                    node.setLeftNode(newNode);
                    newNode.setParentNode(node);
                    break;
                }
            }
            // If the new data is supposed to be right of the current node
            else if (this.comparator.compare(newNode.getData(), node.getData()) > 0) {
                
                // If there is a node to the right of the current node
                if (node.getRightNode() != null) {
                    node = node.getRightNode();
                }
                // Or place the current node
                else {
                    node.setRightNode(newNode);
                    newNode.setParentNode(node);
                    break;
                }
            }
            // If the new node has the same value as the current node, it's a duplicate, so do nothing
            else if (this.comparator.compare(newNode.getData(), node.getData()) == 0) {
                return false;
            }
        }
        
        // Work back up the tree, adjusting heights and re-balancing if necessary
        this.balance(node);
        
        return true;
    }
    
    /**
     * Function to find data in the tree and delete it, balancing out the tree appropriately
     * @param data
     * @return true if element was found and deleted, false otherwise
     */
    public boolean delete(T data) {
        // Get the node with the data that is to be deleted
        BalancedBinarySearchTreeNode<T> deadNode = searchForNode(data);
        
        // If the data isn't in the tree, return false
        if (deadNode == null) {
            return false;
        }
        
        // One or zero children
        if (!deadNode.hasLeftNode() || !deadNode.hasRightNode()) {
            if (deadNode.hasLeftNode()) {
                this.swapParentsChild(deadNode, deadNode.getLeftNode());
                this.balance(deadNode.getLeftNode());
            } else if (deadNode.hasRightNode()) {
                this.swapParentsChild(deadNode, deadNode.getRightNode());
                this.balance(deadNode.getRightNode());
            } else {
                this.swapParentsChild(deadNode, null);
                this.balance(deadNode);
            }
            
        }
        // Two children
        else {
            // Get the inorder successor
            BalancedBinarySearchTreeNode<T> inorderSuccessorNode = this.getLeftmostNode(deadNode.getRightNode());
            
            // If the successor is directly next to the killed node
            if (inorderSuccessorNode.getParentNode().equals(deadNode)) {
                // No need to remove references to the old inorder successor, simply move it up and
                //   replace the deadNode with the inorder successor
                this.swapParentsChild(deadNode, inorderSuccessorNode);
                inorderSuccessorNode.setLeftNode(deadNode.getLeftNode());
                if (inorderSuccessorNode.hasLeftNode()) {
                    inorderSuccessorNode.getLeftNode().setParentNode(inorderSuccessorNode);
                }
                this.balance(inorderSuccessorNode);
            }
            // If the successor is farther down the tree, more adjustments need to be made
            else {
                BalancedBinarySearchTreeNode<T> inorderSuccessorNodeParent = inorderSuccessorNode.getParentNode();
                
                // Remove references to the old inorder successor
                if (inorderSuccessorNode.hasRightNode()) {
                    inorderSuccessorNodeParent.setLeftNode(inorderSuccessorNode.getRightNode());
                    inorderSuccessorNode.getRightNode().setParentNode(inorderSuccessorNodeParent);
                } else {
                    inorderSuccessorNodeParent.setLeftNode(null);
                }
                
                // Replace the deadNode with the inorder successor
                this.swapParentsChild(deadNode, inorderSuccessorNode);
                inorderSuccessorNode.setLeftNode(deadNode.getLeftNode());
                if (inorderSuccessorNode.hasLeftNode()) {
                    inorderSuccessorNode.getLeftNode().setParentNode(inorderSuccessorNode);
                }
                inorderSuccessorNode.setRightNode(deadNode.getRightNode());
                if (inorderSuccessorNode.hasRightNode()) {
                    inorderSuccessorNode.getRightNode().setParentNode(inorderSuccessorNode);
                }
                
                this.balance(inorderSuccessorNodeParent);
            }
        }
        deadNode = null;
        
        return true;
    }
    
    /**
     * Function to work up the tree from specified node, balancing it and adjusting heights
     * @param node
     */
    private void balance(BalancedBinarySearchTreeNode<T> node) {
        while (node != null) {
            
            // If the left subtree is taller than the right subtree by more than 1
            if (height( node.getLeftNode() ) - height( node.getRightNode() ) == 2) {
                if (node.getLeftNode().hasRightNode()) {
                    node = doubleRotateWithLeftChild( node );
                } else {
                    node = rotateWithLeftChild( node );
                }
            }
            // If the right subtree is taller than the left subtree by more than 1
            else if (height( node.getLeftNode() ) - height( node.getRightNode() ) == -2) {
                if (node.getRightNode().hasLeftNode()) {
                    node = doubleRotateWithRightChild( node );
                } else {
                    node = rotateWithRightChild( node );
                }
            }
            
            // Set the height of the current node to be one more than the height of its tallest child
            node.setHeight(max(height(node.getLeftNode()), height(node.getRightNode())) + 1);
            
            // 
            if(node.hasParent()){
                node = node.getParentNode();
            } else {
                this.root = node;
                break;
            }
        }
        return;
    }
    
    /**
     * Helper function to easily copy the parent references from one node to another; 
     *   parents are also updated to refer to the new child
     * @param previousNode
     * @param newNode
     */
    private void swapParentsChild(BalancedBinarySearchTreeNode<T> previousNode, BalancedBinarySearchTreeNode<T> newNode) {
        if (previousNode.hasParent()) {
            if (newNode == null) {
                if (this.comparator.compare(previousNode.getData(), previousNode.getParentNode().getData()) < 0) {
                    previousNode.getParentNode().setLeftNode(newNode);
                } else {
                    previousNode.getParentNode().setRightNode(newNode);
                }
                return;
            }
            
            if (this.comparator.compare(newNode.getData(), previousNode.getParentNode().getData()) < 0) {
                previousNode.getParentNode().setLeftNode(newNode);
            } else {
                previousNode.getParentNode().setRightNode(newNode);
            }
        }
        newNode.setParentNode(previousNode.getParentNode());
    }
    
    /**
     * Rotate binary tree node with left child
     * <br><pre>
     *      3           
     *     /        2   
     *    2   ==>  / \  
     *   /        1   3 
     *  1               
     * </pre>
     * @param oldParent (3)
     * @return newParent (2)
     */
    private BalancedBinarySearchTreeNode<T> rotateWithLeftChild(BalancedBinarySearchTreeNode<T> oldParent) {
        BalancedBinarySearchTreeNode<T> newParent = oldParent.getLeftNode();
        oldParent.setLeftNode(newParent.getRightNode());
        newParent.setRightNode(oldParent);
        
        this.swapParentsChild(oldParent, newParent);
        oldParent.setParentNode(newParent);
        
        oldParent.setHeight(max( height(oldParent.getLeftNode()), height(oldParent.getRightNode()) ) + 1);
        newParent.setHeight(max( height(newParent.getLeftNode()), oldParent.getHeight() ) + 1);
        return newParent;
    }

    /**
     * Rotate binary tree node with right child
     * <br><pre>
     *  1               
     *   \          2   
     *    2   ==>  / \  
     *     \      1   3 
     *      3           
     * </pre>
     * @param oldParent (1)
     * @return newParent (2)
     */
    private BalancedBinarySearchTreeNode<T> rotateWithRightChild(BalancedBinarySearchTreeNode<T> oldParent) {
        BalancedBinarySearchTreeNode<T> newParent = oldParent.getRightNode();
        oldParent.setRightNode(newParent.getLeftNode());
        newParent.setLeftNode(oldParent);
        
        this.swapParentsChild(oldParent, newParent);
        oldParent.setParentNode(newParent);
        
        oldParent.setHeight(max( height(oldParent.getLeftNode()), height(oldParent.getRightNode()) ) + 1);
        newParent.setHeight(max( height(newParent.getRightNode()), oldParent.getHeight() ) + 1);
        return newParent;
    }
    
    /**
     * Double rotate binary tree node: first left child
     * with its right child, then parent with new left child
     * <br><pre>
     *    3         3           
     *   /         /        2   
     *  1   ==>   2   ==>  / \  
     *   \       /        1   3 
     *    2     1               
     * </pre>
     * @param parent (3)
     * @return newParent (2)
     */
    private BalancedBinarySearchTreeNode<T> doubleRotateWithLeftChild(BalancedBinarySearchTreeNode<T> parent) {
        parent.setLeftNode(rotateWithRightChild( parent.getLeftNode() ));
        return rotateWithLeftChild( parent );
    }
    
    /**
     * Double rotate binary tree node: first right child
     * with its left child, then parent with new right child
     * <br><pre>
     *  1       1               
     *   \       \          2   
     *    3 ==>   2   ==>  / \  
     *   /         \      1   3 
     *  2           3           
     * </pre>
     * @param parent (1)
     * @return newParent (2)
     */
    private BalancedBinarySearchTreeNode<T> doubleRotateWithRightChild(BalancedBinarySearchTreeNode<T> parent) {
        parent.setRightNode(rotateWithLeftChild( parent.getRightNode() ));
        return rotateWithRightChild( parent );
    }
    
    /**
     * Function to get the height of a node
     * @param node
     * @return height of node; -1 if null
     */
    public int height(BalancedBinarySearchTreeNode<T> node) {
        return node == null ? -1 : node.getHeight();
    }
    
    /**
     * Function to get the max height of the left and right nodes
     * @param lhs (Left Hand Side)
     * @param rhs (Right Hand Side)
     * @return Either lhs or rhs, whichever is larger; default to rhs on tie
     */
    private int max(int lhs, int rhs) {
        return lhs > rhs ? lhs : rhs;
    }
    
    public int countNodes() {
        return countNodes(this.root);
    }
    private int countNodes(BalancedBinarySearchTreeNode<T> node) {
        if (node == null)
            return 0;
        else {
            int n = 1;
            n += countNodes(node.getLeftNode());
            n += countNodes(node.getRightNode());
            return n;
        }
    }
    
    public boolean contains(T data) {
        return search(data) != null;
    }
    public T search(T data) {
        BalancedBinarySearchTreeNode<T> node = this.searchForNode(data);
        return node != null ? node.getData() : null;
    }
    private BalancedBinarySearchTreeNode<T> searchForNode(T data) {
        BalancedBinarySearchTreeNode<T> node = this.root;
        while (node != null) {
            T nodeData = node.getData();
            if (this.comparator.compare(data, nodeData) < 0) {
                node = node.getLeftNode();
            } else if (this.comparator.compare(data, nodeData) > 0) {
                node = node.getRightNode();
            } else {
                break;
            }
        }
        return node;
    }
    
    /**
     * Function to retrieve the data from the elements in the tree through an in-order traversal
     * @return LinkedList of the data of all elements in the tree, in order
     */
    public List<T> inorder() {
        List<T> sorted = new LinkedList<T>();
        inorder(this.root, sorted);
        return sorted;
    }
    /**
     * Recursively walk the tree to generate an in-order traversal of tree
     * @param node
     * @param sorted data
     */
    private void inorder(BalancedBinarySearchTreeNode<T> node, List<T> sorted) {
        if (node != null) {
            inorder(node.getLeftNode(), sorted);
            sorted.add(node.getData());
            inorder(node.getRightNode(), sorted);
        }
    }
    
    /**
     * Function to retrieve the height of the elements in the tree through an in-order traversal
     * @return LinkedList of the height of all elements in the tree, in order by data
     */
    public List<Integer> inorderHeight() {
        List<Integer> sorted = new LinkedList<Integer>();
        inorderHeight(this.root, sorted);
        return sorted;
    }
    /**
     * Recursively walk the tree to generate an in-order traversal of tree
     * @param node
     * @param sorted heights by data
     */
    private void inorderHeight(BalancedBinarySearchTreeNode<T> node, List<Integer> sorted) {
        if (node != null) {
            inorderHeight(node.getLeftNode(), sorted);
            sorted.add(node.getHeight());
            inorderHeight(node.getRightNode(), sorted);
        }
    }
    
    // Convenience Methods
    /**
     * @return true if the BalanceBinarySearchTree is empty
     */
    public boolean isEmpty() {
        return this.root == null;
    }
    
    /**
     * Clears the BalanceBinarySearchTree of all its members
     */
    public void clear() {
        this.root = null;
    }
    
    /**
     * @return the leftmost node in the binary tree
     */
    public BalancedBinarySearchTreeNode<T> getLeftmostNode(){
        return getLeftmostNode(this.root);
    }
    
    /**
     * @param node starting location for traversal
     * @return the leftmost node in the binary tree
     */
    public BalancedBinarySearchTreeNode<T> getLeftmostNode(BalancedBinarySearchTreeNode<T> node){
        if (node == null) return null;
        while (node.hasLeftNode()) {
            node = node.getLeftNode();
        }
        return node;
    }
    
    /**
     * @return the rightmost node in the binary tree
     */
    public BalancedBinarySearchTreeNode<T> getRightmostNode(){
        return getRightmostNode(this.root);
    }
    
    /**
     * @param node starting location for traversal
     * @return the rightmost node in the binary tree
     */
    public BalancedBinarySearchTreeNode<T> getRightmostNode(BalancedBinarySearchTreeNode<T> node){
        if (node == null) return null;
        while (node.hasRightNode()) {
            node = node.getRightNode();
        }
        return node;
    }

    @Override
    public Iterator<T> iterator(){
        Iterator<T> iterator = new Iterator<T>(){
            private BalancedBinarySearchTreeNode<T> previousNode = null;
            private BalancedBinarySearchTreeNode<T> currentNode = getLeftmostNode();
            private BalancedBinarySearchTreeNode<T> finalNode = getRightmostNode();
            
            @Override
            public boolean hasNext(){
                if (currentNode == null) {
                    return false;
                }
                if (comparator.compare(currentNode.getData(), finalNode.getData()) <= 0) {
                    return true;
                }
                return false;
            }

            @Override
            public T next(){
                if (!this.hasNext()) {
                    throw new NoSuchElementException();
                }
                
                // Store the node who's data is to be returned
                previousNode = currentNode;
                
                // Find what the next node would be
                if (currentNode.hasRightNode()) {
                    currentNode = getLeftmostNode( currentNode.getRightNode() );
                } else {
                    // If there are no more children, work up the tree to the next node
                    // NOTE: use <= to account for the last element or a tree of size 1
                    while (comparator.compare(currentNode.getData(), previousNode.getData()) <= 0) {
                        if (currentNode.hasParent()) {
                            currentNode = currentNode.getParentNode();
                        } else {
                            currentNode = null;
                            break;
                        }
                    }
                }
                
                return previousNode.getData();
            }
        };
        
        return iterator;
    }
    
}
