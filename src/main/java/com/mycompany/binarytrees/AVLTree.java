package com.mycompany.binarytrees;

import com.mycompany.circleList.CircleList;

/**
 * Example of AVLTree, that collect main key in its Node and collect all repeated keys
 * in CircleList. AVLTree has been fully implemented based on Niklaus Wirth's balanced
 * tree algorithm without Node height. Removable element from tree replaced by lower element
 * in right branch.
 *
 * Especially thanks to William Fiset.
 * Class TreePrinter was borrowed from William Fiset's gitHub :
 * https://github.com/williamfiset/Algorithms/tree/master/src/main/java/com/williamfiset/algorithms/datastructures/utils
 * @param <E>
 */
public class AVLTree <E extends Comparable<E>>{
    // Main tree root
    private Node root;
    // Global condition of tree
    private boolean treeChanged = false;

    protected class Node implements TreePrinter.PrintableNode {
        int balanceFactor; // Take values: -1 - left side is overweight, 0 - balanced, 1 - right side is overweight
        Node left; // Left leaf
        Node right; // Right leaf
        E key; // Main key
        CircleList<E> data; // List of repeated keys
        Node(E key){
            left = null;
            right = null;
            data = new CircleList<>();
            this.key = key;
        }

        void addToList(E key){  // Method that pushes repeated keys into List
            data.add(key);
        }

        @Override
        public TreePrinter.PrintableNode getLeft() {
            return left;
        }

        @Override
        public TreePrinter.PrintableNode getRight() {
            return right;
        }

        @Override
        public String getText() {
            return "{ "+key+" "+((data != null) ? "List{"+data.toString()+"}" : "")+" }";
        }
    }

    public boolean isEmpty(){
        return root == null;
    }

    public boolean contains(E key){
        if(key == null) return false;
        return contains(key,root);
    }

    private boolean contains(E key, Node currentNode){
        if(currentNode == null) return false;
        if(key.compareTo(currentNode.key) > 0)
            return contains(key, currentNode.right);
        else if(key.compareTo(currentNode.key) < 0)
                return contains(key, currentNode.left);
        return true;
    }

    public boolean add(E key){
        if(key == null) return false;
        root = add(key,root);
        return true;
    }

    private Node add(E key, Node currentNode){
        if(currentNode == null) {
            treeChanged = true;
            return new Node(key);
        }
        if(key.compareTo(currentNode.key) > 0) {
            currentNode.right = add(key, currentNode.right);
            currentNode = rightAddBalance(currentNode);
        }
        else if(key.compareTo(currentNode.key) < 0){
            currentNode.left = add(key, currentNode.left);
            currentNode = leftAddBalance(currentNode);
        }
        else {
            currentNode.addToList(key);
        }

        return currentNode;
    }

    private Node rightAddBalance(Node currentNode){ // Balance Node's right branch after adding
        if(treeChanged){
            switch (currentNode.balanceFactor){
                case(-1) -> currentNode.balanceFactor = 0;
                case (0) -> currentNode.balanceFactor = 1;
                default -> {
                    if (currentNode.right.balanceFactor != 0) {
                        if (currentNode.right.balanceFactor == 1)
                            currentNode = leftTurn(currentNode);
                        else currentNode = rightLeftTurn(currentNode);
                        currentNode.balanceFactor = 0;
                        treeChanged = false;
                    }
                }
            }
        }
        return currentNode;
    }

    private Node leftAddBalance(Node currentNode){ // Balance Node's left branch after adding
        if(treeChanged){
            switch (currentNode.balanceFactor) {
                case (1) -> currentNode.balanceFactor = 0;
                case (0) -> currentNode.balanceFactor = -1;
                default -> {
                    if (currentNode.left.balanceFactor != 0) {
                        if (currentNode.left.balanceFactor == -1)
                            currentNode = rightTurn(currentNode);
                        else currentNode = leftAndRightTurn(currentNode);
                        currentNode.balanceFactor = 0;
                        treeChanged = false;
                    }
                }
            }
        }
        return currentNode;
    }

    private Node rightLeftTurn(Node currentNode) {
        Node p1 = currentNode.right; // Right Child of currentNode
        Node p2 = p1.left; // Left child of right child of currentNode
        p1.left = p2.right;
        p2.right = p1;
        currentNode.right = p2.left;
        p2.left = currentNode;
        if(p2.balanceFactor == 1)
            currentNode.balanceFactor = -1;
        else
            currentNode.balanceFactor = 0;
        if(p2.balanceFactor == -1)
            p1.balanceFactor = 1;
        else
            p1.balanceFactor = 0;

        return p2;
    }

    private Node rightTurn(Node currentNode) {
        Node p1 = currentNode.left;
        currentNode.left = p1.right;
        p1.right = currentNode;
        currentNode.balanceFactor = 0;
        return p1;
    }

    private Node leftTurn(Node currentNode){
        Node p1 = currentNode.right;
        currentNode.right = p1.left;
        p1.left = currentNode;
        currentNode.balanceFactor = 0;
        return p1;
    }

    private Node leftAndRightTurn(Node currentNode){
        Node p1 = currentNode.left; // Left child of currentNode
        Node p2 = p1.right; // Right child of left child of currentNode
        p1.right = p2.left;
        p2.left = p1;
        currentNode.left = p2.right;
        p2.right = currentNode;
        if(p2.balanceFactor == -1)
            currentNode.balanceFactor = 1;
        else
            currentNode.balanceFactor = 0;
        if(p2.balanceFactor == 1)
            p1.balanceFactor = -1;
        else
            p1.balanceFactor = 0;
        return p2;
    }

    public boolean remove(E key){
        if (key == null) return false;
        if(!contains(key)) return false;
        root = remove(key, root);
        return true;
    }


    private Node remove(E key, Node currentNode) throws NullPointerException{
        Node removableNode;

        if(key.compareTo(currentNode.key) < 0){
           currentNode.left = remove(key, currentNode.left);
            if(treeChanged)
               currentNode = leftRemBalance(currentNode);
        } else if(key.compareTo(currentNode.key) > 0){
            currentNode.right = remove(key, currentNode.right);
            if(treeChanged)
               currentNode = rightRemBalance(currentNode);
        } else {
            if(currentNode.data == null || currentNode.data.isEmpty()) {
                    removableNode = currentNode;
                    if (removableNode.left == null) {
                        currentNode = removableNode.right;
                        treeChanged = true;
                    } else if (removableNode.right == null) {
                        currentNode = removableNode.left;
                        treeChanged = true;
                    } else {
                        currentNode.right = rem(removableNode.right, removableNode);
                        if (treeChanged)
                            currentNode = rightRemBalance(currentNode);
                    }
            } else
                currentNode.data.remove(key);
        }

        return currentNode;
    }

    private Node rem(Node currentNode, Node removableNode){ // Method that replace removableNode by lower Node in right
                                                            // branch of removableNode
        if(currentNode.left!= null){
            currentNode.left= rem(currentNode.left, removableNode);
            if(treeChanged)
                currentNode = leftRemBalance(currentNode);
        } else {
            removableNode.key = currentNode.key;
            removableNode.data = currentNode.data;
            currentNode = currentNode.right;
            treeChanged = true;
        }
        return currentNode;
    }

    private Node leftRemBalance(Node currentNode){ // Balancing tree's left branch after removing
        switch (currentNode.balanceFactor){
            case(-1) -> currentNode.balanceFactor = 0;
            case(0) -> {
                currentNode.balanceFactor = 1;
                treeChanged = false;
            }
            default -> {
                if (currentNode.right.balanceFactor >= 0) {
                    currentNode = leftTurn(currentNode);
                }
                else {
                    currentNode = rightLeftTurn(currentNode);
                }
                currentNode.balanceFactor = 0;
            }
        }
        return currentNode;
    }

    private Node rightRemBalance(Node currentNode){ // Balancing tree's right branch after removing
        switch (currentNode.balanceFactor){
            case (1) -> currentNode.balanceFactor = 0;
            case (0) -> {
                currentNode.balanceFactor = -1;
                treeChanged = false;
            }
            default -> {
                if(currentNode.left.balanceFactor <= 0) {
                    currentNode = rightTurn(currentNode);
                }
                else {
                    currentNode = leftAndRightTurn(currentNode);
                }
                currentNode.balanceFactor = 0;
            }
        }
        return currentNode;
    }

    public boolean clear(){
        if(isEmpty()) return  false;
        root = clear(root);
        return true;
    }

    private Node clear(Node currentNode){
        if(currentNode.left == null && currentNode.right == null) {
            currentNode.data.clear();
            currentNode.data = null;
            System.gc();
            return null;
        }
        if(currentNode.left != null)
            currentNode.left = clear(currentNode.left);
        if(currentNode.right != null)
            currentNode.right = clear(currentNode.right);
        return null;
    }

    public E findMin(){
        Node currentNode = root;
        while (currentNode.left != null){
            currentNode = currentNode.left;
        }
        return currentNode.key;
    }

    public E findMax(){
        Node currentNode = root;
        while (currentNode.right != null){
            currentNode = currentNode.left;
        }
        return currentNode.key;
    }

    public void preOrder(){
        preOrder(root);
        System.out.println();
    }

    public void postOrder(){
        postOrder(root);
        System.out.println();
    }

    public void inOrder(){
        inOrder(root);
        System.out.println();
    }

    private void preOrder(Node currentNode){
        if(currentNode == null) return;
        System.out.print(currentNode.key+" : {"+currentNode.data.toString()+"} ");
        preOrder(currentNode.left);
        preOrder(currentNode.right);
    }

    private void postOrder(Node currentNode){
        if(currentNode == null) return;
        preOrder(currentNode.left);
        preOrder(currentNode.right);
        System.out.print(currentNode.key+" : {"+currentNode.data.toString()+"} ");
    }

    private void inOrder(Node currentNode) {
        if (currentNode == null) return;
        preOrder(currentNode.left);
        System.out.print(currentNode.key + " : {" + currentNode.data.toString() + "} ");
        preOrder(currentNode.right);
    }

    public void printTree(){
        if(!isEmpty())
            System.out.println(TreePrinter.getTreeDisplay(root));
    }
}
