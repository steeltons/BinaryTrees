package com.mycompany.binarytrees;

import com.mycompany.circleList.CircleList;

public class VirtAVLTree<E extends Comparable<E>> implements BinTree<E>{

    private Node root;

    private class Node implements TreePrinter.PrintableNode{
        int balanceFactor;
        Node left, right;
        E elem;
        Node(E elem){
            left = null;
            right = null;
            this.elem = elem;
            balanceFactor = 0;
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
            return elem.toString();
        }
    }
    @Override
    public boolean add(E elem) {
        add(elem, root, false);
        return false;
    }

    private boolean add(E elem, Node currentNode, boolean h){
        if(currentNode == null){
            currentNode = new Node(elem);
            h = true;
        }
        else if(currentNode.elem.compareTo(elem) >= 1) {
            add(elem, currentNode.right, h);
            if (h) {
                if (currentNode.balanceFactor == 1) {
                    currentNode.balanceFactor = 0;
                    h = false;
                } else {
                    if (currentNode.balanceFactor == 0) currentNode.balanceFactor = -1;
                    else {
                        if (currentNode.left.balanceFactor == -1) {
                            currentNode = leftTurn(currentNode);
                        } else {
                            currentNode = leftAndRightTurn(currentNode);
                        }
                        currentNode.balanceFactor = 0;
                        h = false;
                    }
                }
            }
        }
        else{
            if(currentNode.elem.compareTo(elem) < 0)
                add(elem, currentNode.left, h);
            if(h){
                if(currentNode.balanceFactor == -1){
                    currentNode.balanceFactor = 0;
                    h = false;
                }
                else{
                    if(currentNode.balanceFactor == 0)
                        currentNode.balanceFactor = 1;
                    else {
                        if(currentNode.right.balanceFactor == 1){
                            currentNode = rightTurn(currentNode);
                        }
                        else{
                            currentNode = rightAndLeftTurn(currentNode);
                        }
                        currentNode.balanceFactor = 0;
                        h = false;
                    }
                }
            }
        }

        return false;
    }

    @Override
    public boolean remove(E elem) {
        return false;
    }

    private Node leftTurn(Node currentNode){
        Node leftChild = currentNode.right;
        currentNode.left = leftChild.right;
        leftChild.right = currentNode;
        currentNode.balanceFactor = 0;
        return leftChild;
    }

    private Node leftAndRightTurn(Node currentNode){
        Node leftChild = currentNode.left;
        Node rightLeftChild = leftChild.right;
        leftChild.right = rightLeftChild;
        rightLeftChild.left = leftChild;
        currentNode.left = rightLeftChild.right;
        rightLeftChild.right = currentNode;
        if(rightLeftChild.balanceFactor == -1)
            currentNode.balanceFactor = 1;
        else
            currentNode.balanceFactor = 0;
        if(rightLeftChild.balanceFactor == 1)
            leftChild.balanceFactor = 1;
        else
            leftChild.balanceFactor = 0;
        return rightLeftChild;
    }

    private Node rightTurn(Node currentNode){
        Node rightChild = currentNode.right;
        currentNode.right = rightChild.left;
        rightChild.left = currentNode;
        currentNode.balanceFactor = 0;
        return rightChild;
    }

    private Node rightAndLeftTurn(Node currentNode){
        Node rightChild = currentNode.right;
        Node leftRightChild = rightChild.left;
        rightChild.left = leftRightChild.right;
        leftRightChild.right = rightChild;
        currentNode.right = leftRightChild.left;
        leftRightChild.left = currentNode;
        if(leftRightChild.balanceFactor == 1)
            currentNode.balanceFactor = -1;
        else
            currentNode.balanceFactor = 0;
        if(leftRightChild.balanceFactor == -1)
            rightChild.balanceFactor = 1;
        else
            rightChild.balanceFactor = 0;
        return leftRightChild;
    }

    @Override
    public boolean isEmpty() {
        return root == null;
    }

    @Override
    public void preOrder() {
        preOrder(root);
        System.out.println();
    }

    private void preOrder(Node currentNode){
        if(currentNode == null)
            return;
        System.out.print(currentNode.elem);
        preOrder(currentNode.left);
        preOrder(currentNode.right);
    }

    @Override
    public void postOrder() {

    }

    @Override
    public void inOrder() {

    }
}
