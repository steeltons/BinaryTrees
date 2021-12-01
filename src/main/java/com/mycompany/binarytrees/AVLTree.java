package com.mycompany.binarytrees;

import com.mycompany.circleList.CircleList;

public class AVLTree{

    private Node root;

    protected class Node implements TreePrinter.PrintableNode {
        int balanceFactor;
        Node left;
        Node right;
        Flight key;
        CircleList<Flight> data;
        Node(Flight key){
            left = null;
            right = null;
            this.key = key;
        }

        void addToList(Flight flight){
            if(data == null)
                data = new CircleList<>();
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
            return "{ "+key+" "+((data != null) ? data.toString() : "")+" }";
        }
    }

    public boolean isEmpty(){
        return root == null;
    }

    public boolean contains(Flight key){
        if(key == null) return false;
        return contains(key,root);
    }

    private boolean contains(Flight key, Node currentNode){
        if(currentNode == null) return false;
        if(key.compareTo(currentNode.key) > 0)
            return contains(key, currentNode.right);
        else if(key.compareTo(currentNode.key) < 0)
                return contains(key, currentNode.left);
        return true;
    }

    public boolean add(Flight key){
        if(key == null) return false;
        Flag flag = new Flag(false);
        root = add(key,root, flag);
        return true;
    }

    private Node add(Flight key, Node currentNode, Flag movement){
        if(currentNode == null) {
            movement.flag = true;
            return new Node(key);
        }
        if(key.compareTo(currentNode.key) > 0) {
            currentNode.right = add(key, currentNode.right,movement);
            currentNode = balanceRight(currentNode, movement);
        }
        else if(key.compareTo(currentNode.key) < 0){
            currentNode.left = add(key, currentNode.left, movement);
            currentNode = balanceLeft(currentNode, movement);
        }
        else {
            currentNode.addToList(key);
        }

        return currentNode;
    }

    private Node balanceRight(Node currentNode, Flag flag){
        if(flag.isAdded()){
            switch (currentNode.balanceFactor){
                case(-1) : currentNode.balanceFactor = 0;
                    break;
                case (0) : currentNode.balanceFactor = 1;
                    break;
                default: {
                    if (currentNode.right.balanceFactor != 0) {
                        if (currentNode.right.balanceFactor == 1)
                            currentNode = leftTurn(currentNode);
                        else currentNode = rightAndLeftTurn(currentNode);
                        currentNode.balanceFactor = 0;
                        flag.flag = false;
                    }
                }
            }
        }
        return currentNode;
    }

    private Node balanceLeft(Node currentNode, Flag flag){
        if(flag.isAdded()){
            switch (currentNode.balanceFactor){
                case(1) : currentNode.balanceFactor = 0;
                    break;
                case (0) : currentNode.balanceFactor = -1;
                    break;
                default: {
                    if (currentNode.left.balanceFactor != 0) {
                        if (currentNode.left.balanceFactor == -1)
                            currentNode = rightTurn(currentNode);
                        else currentNode = leftAndRightTurn(currentNode);
                        currentNode.balanceFactor = 0;
                        flag.flag = false;
                    }
                }
            }
        }
        return currentNode;
    }

    private Node rightAndLeftTurn(Node currentNode) {
        Node p1 = currentNode.right;
        Node p2 = p1.left;
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
        Node newParent = currentNode.left;
        currentNode.left = newParent.right;
        newParent.right = currentNode;
        currentNode.balanceFactor = 0;
        return newParent;
    }

    private Node leftTurn(Node currentNode){
        Node newParent = currentNode.right;
        currentNode.right = newParent.left;
        newParent.left = currentNode;
        currentNode.balanceFactor = 0;
        return newParent;
    }

    private Node leftAndRightTurn(Node currentNode){
        Node p1 = currentNode.left;
        Node p2 = p1.right;
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

    public boolean remove(Flight key){
        if (key == null) return false;
        if(!contains(key)) return false;
        Flag flag = new Flag(false);
        root = remove(key, root, flag);
        return true;
    }


    private Node remove(Flight key, Node currentNode, Flag movement) throws NullPointerException{
        Node q;
        if(currentNode == null){
            throw  new NullPointerException();
        }

        if(key.compareTo(currentNode.key) < 0){
           currentNode.left = remove(key, currentNode.left, movement);
            if(movement.flag)
               currentNode = leftRemBalance(currentNode, movement);
        } else if(key.compareTo(currentNode.key) > 0){
            currentNode.right = remove(key, currentNode.right, movement);
            if(movement.flag)
               currentNode = rightRemBalance(currentNode, movement);
        } else {
            if(currentNode.data == null || currentNode.data.isEmpty()) {
                    q = currentNode;
                    if (q.left == null) {
                        currentNode = q.right;
                        movement.flag = true;
                    } else if (q.right == null) {
                        currentNode = q.left;
                        movement.flag = true;
                    } else {
                        currentNode.right = rem(q.right, movement, q);
                        if (movement.flag)
                            currentNode = rightRemBalance(currentNode, movement);
                    }
            } else
                currentNode.data.remove(key);
        }

        return currentNode;
    }

    private Node rem(Node currentNode, Flag movement, Node q){
        if(currentNode.left!= null){
            currentNode.left= rem(currentNode.left, movement, q);
            if(movement.flag)
                currentNode = leftRemBalance(currentNode, movement);
        } else {
            q.key = currentNode.key;
            q.data = currentNode.data;
            currentNode = currentNode.right;
            movement.flag = true;
        }
        return currentNode;
    }

    private Node leftRemBalance(Node currentNode, Flag flag){
        switch (currentNode.balanceFactor){
            case(-1) -> currentNode.balanceFactor = 0;
            case(0) -> {
                currentNode.balanceFactor = 1;
                flag.flag = false;
            }
            default -> {
                if (currentNode.right.balanceFactor >= 0) {
                    currentNode = leftTurn(currentNode);
                }
                else {
                    currentNode = rightAndLeftTurn(currentNode);
                }
                currentNode.balanceFactor = 0;
            }
        }
        return currentNode;
    }

    private Node rightRemBalance(Node currentNode, Flag flag){
        switch (currentNode.balanceFactor){
            case (1) -> currentNode.balanceFactor = 0;
            case (0) -> {
                currentNode.balanceFactor = -1;
                flag.flag = false;
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

    public Flight findMin(){
        Node currentNode = root;
        while (currentNode.left != null){
            currentNode = currentNode.left;
        }
        return currentNode.key;
    }

    public Flight findMax(){
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

    private void inOrder(Node currentNode){
        if(currentNode == null) return;
        preOrder(currentNode.left);
        System.out.print(currentNode.key+" : {"+currentNode.data.toString()+"} ");
        preOrder(currentNode.right);
    }

    public int nodeCount(){
        if(root == null) return 0;
        return nodeCount(root);
    }

    private int nodeCount(Node currentNode){
        int count = 0;
        if(currentNode == null) return 0;
        count+= nodeCount(currentNode.left);
        count+= nodeCount(currentNode.right);
        return count+1;
    }

    public void printTree(){
        System.out.println(TreePrinter.getTreeDisplay(root));
    }
}

class Flag{
    boolean flag;
    Flag(boolean flag){
        this.flag = flag;
    }

    boolean isAdded(){
        return flag;
    }
}
