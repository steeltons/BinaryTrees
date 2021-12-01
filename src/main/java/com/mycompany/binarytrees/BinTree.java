package com.mycompany.binarytrees;

public interface BinTree<E extends Comparable<E>> {

    public boolean add(E elem);

    public boolean remove(E elem);

    public boolean isEmpty();

    public void preOrder();

    public void postOrder();

    public void inOrder();

}
