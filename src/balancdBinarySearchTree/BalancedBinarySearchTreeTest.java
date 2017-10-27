package balancdBinarySearchTree;

import java.util.Comparator;
import java.util.Scanner;

public class BalancedBinarySearchTreeTest{
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        
        Comparator<Integer> intCmp = new Comparator<Integer>() {
            public int compare(Integer a, Integer b){
                if (a == null && b == null){
                    return 0;
                } else if (a == null){
                    return -1;
                } else if (b == null){
                    return 1;
        } else {
            return a.compareTo(b);
                }
            }
        };
        
        // Creating object of BalancedBinarySearchTree
        BalancedBinarySearchTree<Integer> bbst = new BalancedBinarySearchTree<Integer>(intCmp); 
        System.out.println("SelfBalancingBinarySearchTree Test\n");
        char ch;
        // Perform tree operations
        do {
            System.out.println("\nSelfBalancingBinarySearchTree Options:");
            System.out.println("1. Insert");
            System.out.println("2. Search for node");
            System.out.println("3. Count nodes");
            System.out.println("4. Check if empty");
            System.out.println("5. Clear tree");
            System.out.println("6. Delete node");
 
            int choice = scan.nextInt();
            switch (choice) {
            case 1: 
                System.out.println("Enter integer element to insert");
                bbst.insert( scan.nextInt() );
                break;
            case 2: 
                System.out.println("Enter integer element to search");
                System.out.println("Found? : "+ bbst.contains( scan.nextInt() ));
                break;                                          
            case 3: 
                System.out.println("Nodes = "+ bbst.countNodes());
                break;
            case 4: 
                System.out.println("Empty? : "+ bbst.isEmpty());
                break;
            case 5: 
                System.out.println("\nTree Cleared");
                bbst.clear();
                break;
            case 6: 
                System.out.println("Enter integer element to delete");
                System.out.println("Deleted? : "+ bbst.delete( scan.nextInt() ));
                break;
            default: 
                System.out.println("Wrong Entry \n "); 
                break;   
            }
            // Display tree
            System.out.print("\nIn order : ");
            System.out.println(bbst.inorder());
            System.out.print("Heights  : ");
            System.out.println(bbst.inorderHeight());
            for(Integer t : bbst) {
                System.out.print(t + " ");
            }
            System.out.println("");
 
            System.out.println("Do you want to continue (Type y or n)");
            ch = scan.next().charAt(0);                        
        } while (ch != 'N'|| ch != 'n');               
    }
}
