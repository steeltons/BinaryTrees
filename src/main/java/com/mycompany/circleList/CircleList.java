package com.mycompany.circleList;

import java.lang.reflect.Array;
import java.util.*;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

public class CircleList<E extends Comparable<E>> implements Iterable<E>, Cloneable{

    private Node Head;

    private class Node{
        Node next;
        E elem;
        Node(){}
        Node(E elem){
            this.elem = elem;
        }
        void linkWithPred(Node pred){
            pred.next = this;
        }
    }

    public CircleList(){
        Head = null;
    }

    public CircleList(E[] array){
        for(E elem : array)
            add(elem);
    }

    public CircleList(Collection<E> collection){

    }
    public boolean isEmpty(){
        return Head == null;
    }

    public void add(E elem){
        if(isEmpty()){
            Head = new Node(elem);
            Head.next = Head;
            return;
        }
        Node jumper = Head;
        do{
            jumper = jumper.next;
        }while(!jumper.next.equals(Head));
        makeElem(jumper, elem);
    }

    public boolean addAll(Collection c){
        Node jumper = Head;
        Iterator<E> iterator = c.iterator();
        do{
            jumper = jumper.next;
        }while(!jumper.equals(Head));
        while (iterator.hasNext()){
            makeElem(jumper,iterator.next());
            jumper = jumper.next;
        }
        return true;
    }

    public E get(int index) throws NullPointerException{
        if(isEmpty()) throw new NullPointerException("Нет элемента по индексу");
        if(index < 0 | index > size()) throw new IndexOutOfBoundsException("Индекс выходит за границы размера списка");
        int counter = 0;
        Node jumper = Head;
        do{
            if(counter == index) return jumper.elem;
            jumper = jumper.next;
            counter++;
        }while(!jumper.equals(Head));
        throw new NullPointerException("Нет элемента по индексу");
    }

    public E remove(E elem){
        if(isEmpty()) throw new NullPointerException();
        Node jumper = Head;
        do{
            if(jumper.elem.equals(elem)){unlink(jumper); return jumper.elem;}
            jumper = jumper.next;
        }while(!jumper.equals(Head));
        throw new NullPointerException();
    }

    public E remove(int index){
        if(isEmpty() | index < 0 | index > size()) throw new NullPointerException();
        int counter = 0;
        Node jumper = Head;
        do{
            if(counter == index) {unlink(jumper); return jumper.elem;}
            jumper = jumper.next;
            counter++;
        }while(!jumper.equals(Head));
        throw new NullPointerException();
    }

    public int indexOf(E elem){
        if(isEmpty()) return -1;
        Node jumper = Head;
        int counter = 0;
        do{
            if(jumper.elem.equals(elem)) return counter;
            counter++;
            jumper = jumper.next;
        }while (!jumper.equals(Head));

        return -1;
    }

    private void unlink(Node remElem){
        if(remElem.equals(Head)) {
            if (!Head.next.equals(Head)){
                Node jumper = Head;
                do{
                    jumper = jumper.next;
                }while(!jumper.next.equals(Head));
                jumper.next = Head.next;
                Head = Head.next;
            }
            else
                Head = null;
            return;
        }
        Node predElem = Head;
        while(!predElem.next.equals(remElem))
            predElem = predElem.next;
        Node nextElem = remElem.next;
        predElem.next = nextElem;
    }

    public int size(){
        if(isEmpty()) return  0;
        int counter = 0;
        Node jumper = Head;
        do{
            jumper = jumper.next;
            counter++;
        }while(!jumper.equals(Head));
        return counter;
    }

    public boolean set(int index, E elem){
        if(isEmpty() | size()<index | index<-1) return false;
        Node jumper = Head;
        int counter =0;
        do{
            if(counter == index) {jumper.elem = elem; return true;}
            jumper = jumper.next;
            counter++;
        }while (!jumper.equals(Head));
        return false;
    }

    public boolean replace(E elem){
        Node jumper = Head;
        do{
            if(jumper.elem.equals(elem)) {
                jumper.elem = elem;
                return true;
            }
            jumper = jumper.next;
        } while (!jumper.next.equals(Head));

        return false;
    }

    public boolean contains(E elem){
        if(isEmpty()) return false;
        Node jumper = Head;
        do{
            if(jumper.elem.equals(elem)) return true;
            jumper = jumper.next;
        }while(!jumper.equals(Head));
        return false;
    }

    public void myAdd(E elem){
        if(isEmpty()) throw new NullPointerException();
        Node jumper = Head;
        do{
            jumper = jumper.next;
        }while(!jumper.next.equals(Head));
        makeElem(jumper, elem);
        Head = jumper.next;
        jumper = Head.next;
        int counter = 2;
        do{
            if(counter %2 != 0){ 
                makeElem(jumper, elem);
                jumper = jumper.next;
            }
            jumper = jumper.next;
            counter++;
        }while(!jumper.next.equals(Head));
    }
    
    private void makeElem(Node predElem, E elem){
       Node nextElem = predElem.next;
       Node newElem = new Node(elem);
       newElem.next = nextElem;
       predElem.next = newElem;
    }
    
    public void myRemove(){
        if(isEmpty()) throw new NullPointerException();
        Node jumper = Head.next;
        int counter = 2;
        do{
            if(counter % 2 != 0)
                unlink(jumper);
            jumper = jumper.next;
            counter++;
        }while(!jumper.equals(Head));
        unlink(jumper);
        Head = jumper.next;
    }

    public Iterator<E> iterator(){
        return new myIterator();
    }

    private class myIterator implements Iterator<E>{
        private Node jumper;
        private boolean firstNotSkipped = true;

        public myIterator(){
            jumper = Head;
        }
        @Override
        public boolean hasNext() {
            return !jumper.equals(Head)|firstNotSkipped;
        }

        @Override
        public E next() {
            if(jumper.equals(Head)) firstNotSkipped = false;
            E elem = jumper.elem;
            jumper = jumper.next;
            return elem;
        }
    }

    public E findMax(){
        if(isEmpty()) throw new NullPointerException();
        E max= Head.elem;
        Node jumper = Head;
        do{
            if(jumper.elem.compareTo(max) >=1)
                max = jumper.elem;
            jumper = jumper.next;
        }while(!jumper.equals(Head));
        return max;
    }

    public E findMin(){
        if(isEmpty()) throw new NullPointerException();
        E min= Head.elem;
        Node jumper = Head;
        do{
            if(jumper.elem.compareTo(min) <=1)
                min = jumper.elem;
            jumper = jumper.next;
        }while(!jumper.equals(Head));
        return min;
    }

    public void clear() {
        if (isEmpty()) return;
        Node jumper = Head.next;
        do {
            Node deletElem = jumper;
            jumper = jumper.next;
            deletElem = null;
        } while (!jumper.equals(Head));
        Head = null;
        System.gc();
    }

    public E[] toArray(E[] mas){
        if(isEmpty()) throw new NullPointerException("Список пуст");
        E[] tempArray;
        if(mas.length != size())
            tempArray = (E[]) Array.newInstance(get(0).getClass(),size());
        else tempArray = mas;
        Node jumper = Head;
        int masCounter = 0;
        do{
            mas[masCounter++] = jumper.elem;
        }while (!jumper.equals(Head));
        return tempArray;
    }

    public Object[] toArray() {
        if(isEmpty()) throw new NullPointerException("Список пуст");
        Object[] tempArray = new Object[size()];
        Node jumper = Head;
        int masCounter = 0;
        do{
            tempArray[masCounter++] = jumper.elem;
        }while (!jumper.equals(Head));
        return tempArray;
    }

    public void print(){
        System.out.println(toString());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null | o.getClass() != getClass()) return false;
        CircleList<E> comparableObject = (CircleList) o;
        if(size() != comparableObject.size()) return false;
        Node jumper1 = Head;
        Node jumper2 = (Node) ((CircleList<?>) o).Head;
        do {
            if(!jumper1.elem.equals(jumper2.elem)) return false;
        }while (!jumper1.equals(Head));
        return true;
    }

    @Override
    public CircleList<E> clone(){
        CircleList<E> copyCircleList = new CircleList<>();
        Iterator<E> iterator = iterator();
        while (iterator.hasNext())
            copyCircleList.add(iterator.next());
        return copyCircleList;
    }

    @Override
    public String toString(){
        if(isEmpty()) return "";
        Node jumper = Head;
        StringBuilder temp = new StringBuilder();
        int counter = 0;
        do{
            temp.append("["+(++counter)+"."+jumper.elem+"]->");
            if(counter % 20 == 0) temp.append("\n");
            jumper = jumper.next;
        }while(!jumper.equals(Head));
        return temp.toString();
    }
}
