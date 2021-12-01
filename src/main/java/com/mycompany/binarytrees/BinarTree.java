package com.mycompany.binarytrees;

public class BinarTree<E extends Comparable<E>> {

    protected Node root;
    protected Node addedRoot;

    public BinarTree(){
        root = null;
    }

    protected class Node{
        E elem;
        Node pred;
        Node left;
        Node right;

        Node(E elem){
            this.elem = elem;
        }
    }

    public void add(E elem){
        Node newNode = makeNode(elem);
        if(root == null)
            root = newNode;
        else {
            Node currentNode = root;
            Node parentNode;
            while (true){
                parentNode = currentNode;
                if(elem.equals(currentNode.elem))
                    return;
                if(elem.compareTo(currentNode.elem) >= 0){
                    currentNode = currentNode.right;
                    if(currentNode == null){
                        currentNode = newNode;
                        currentNode.pred = parentNode;
                        parentNode.right = currentNode;
                        addedRoot = newNode;
                    }
                } else {
                    currentNode = currentNode.left;
                    if(currentNode == null){
                        currentNode = newNode;
                        currentNode.pred = parentNode;
                        parentNode.left = currentNode;
                        addedRoot = newNode;
                    }
                }
            }
        }
    }

    public boolean delete(E elem){
        Node currentRoot = root;
        Node parentRoot = root;
        boolean isLeftChild = true;
        while(!currentRoot.elem.equals(elem)){
            parentRoot = currentRoot;
            if(elem.compareTo(currentRoot.elem) < 0){
                isLeftChild = true;
                currentRoot = currentRoot.left;
            } else{
                isLeftChild = false;
                currentRoot = currentRoot.right;
            }
            if(currentRoot == null){
                return false;
            }
        }

        if(currentRoot.left == null && currentRoot.right == null){
            if(currentRoot == root) {
                root = null;
                return true;
            }
            if(isLeftChild)
                parentRoot.left = null;
            else
                parentRoot.right = null;
            return true;
        }

        if(currentRoot.right == null){
            if(currentRoot == root) {
                root = currentRoot.right;
                return true;
            }
            if(isLeftChild)
                parentRoot.left = currentRoot.left;
            else
                parentRoot.right = currentRoot.left;
            return true;
        }

        if(currentRoot.left == null){
            if(currentRoot == root){
                root = currentRoot.right;
                return true;
            }
            if(isLeftChild)
                parentRoot.left = currentRoot;
            else
                parentRoot.right = currentRoot;
            return true;
        }

        Node pair = pair(currentRoot);
        if(currentRoot == root) {
            root = pair;
            return true;
        } else {
            if(isLeftChild)
                parentRoot.left = pair;
            else
                parentRoot.right = pair;
        }

        return true;
    }

    private Node makeNode(E elem){
        Node newNode = new Node(elem);
        newNode.pred = null;
        newNode.left = null;
        newNode.right = null;
        return newNode;
    }

    private Node pair(Node node){
        Node parentNode = node;
        Node heirNode = node;
        Node currentNode = node.right;
        while (currentNode != null){
            parentNode = heirNode;
            heirNode = currentNode;
            currentNode = currentNode.left;
        }
        if(heirNode != node.right){
            parentNode.left = heirNode.right;
            heirNode.right = node.right;
            heirNode.left = node.left;
        }
        return heirNode;
    }

    public void preOrder(){
        preOrder(root);
    }

    private void preOrder(Node currentNode){
        if(currentNode == null) return;
        System.out.print(currentNode.elem+" ");
        preOrder(currentNode.left);
        preOrder(currentNode.right);
    }
}
