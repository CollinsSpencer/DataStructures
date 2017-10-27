package balancdBinarySearchTree;

/**
 * @author Spencer Collins
 * Date: 4/18/17
 *
 * @param <T>
 */
public class BalancedBinarySearchTreeNode<T> {

    private BalancedBinarySearchTreeNode<T> left, right, parent;
    private int height;
    private final T data;
    
    public BalancedBinarySearchTreeNode() {
        this(null, null);
    }
    
    public BalancedBinarySearchTreeNode(T data) {
        this(data, null);
    }
    
    public BalancedBinarySearchTreeNode(T data, BalancedBinarySearchTreeNode<T> parent) {
        this.left = null;
        this.right = null;
        this.parent = parent;
        this.height = 0;
        this.data = data;
    }
    
    public BalancedBinarySearchTreeNode<T> getLeftNode() {
        return this.left;
    }
    
    public void setLeftNode(BalancedBinarySearchTreeNode<T> node) {
        this.left = node;
    }
    
    public boolean hasLeftNode() {
        return (this.left != null);
    }
    
    public BalancedBinarySearchTreeNode<T> getRightNode() {
        return this.right;
    }
    
    public void setRightNode(BalancedBinarySearchTreeNode<T> node) {
        this.right = node;
    }
    
    public boolean hasRightNode() {
        return (this.right != null);
    }
    
    public BalancedBinarySearchTreeNode<T> getParentNode() {
        return this.parent;
    }
    
    public void setParentNode(BalancedBinarySearchTreeNode<T> node) {
        this.parent = node;
    }
    
    public boolean hasParent() {
        return this.parent != null;
    }

    public T getData(){
        return this.data;
    }
    
    public int getHeight() {
        return this.height;
    }
    
    public void setHeight(int height) {
        this.height = height;
    }
    
    
    @Override
    public String toString() {
        return "(" + this.data + ")";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((left == null) ? 0 : left.hashCode());
        result = prime * result + ((parent == null) ? 0 : parent.hashCode());
        result = prime * result
                + ((right == null) ? 0 : right.hashCode());
        result = prime * result + ((data == null) ? 0 : data.hashCode());
        return result;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        BalancedBinarySearchTreeNode other = (BalancedBinarySearchTreeNode) obj;
//        if (left == null) {
//            if (other.left != null)
//                return false;
//        } else if (!left.equals(other.left))
//            return false;
//        if (parent == null) {
//            if (other.parent != null)
//                return false;
//        } else if (!parent.equals(other.parent))
//            return false;
//        if (right == null) {
//            if (other.right != null)
//                return false;
//        } else if (!right.equals(other.right))
//            return false;
        if (data == null) {
            if (other.data != null)
                return false;
        } else if (!data.equals(other.data))
            return false;
        return true;
    }
    
}
