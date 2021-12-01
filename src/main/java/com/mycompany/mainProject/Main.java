package com.mycompany.mainProject;

import com.mycompany.binarytrees.AVLTree;
import com.mycompany.binarytrees.Flight;

public class Main {
    public static void main(String[] args){
        AVLTree<Flight> avlTree = new AVLTree<>();
        avlTree.add(new Flight("AA", 1));
        avlTree.add(new Flight("AA",13 ));
        avlTree.add(new Flight("AA",7));
        avlTree.add(new Flight("AA",10));
        avlTree.add(new Flight("AA",15));
        avlTree.add(new Flight("AA",6));
        avlTree.add(new Flight("AA",11));

        avlTree.printTree();
    }
    
}

