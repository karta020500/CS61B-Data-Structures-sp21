package deque;

import java.util.Iterator;

public class LinkedListDeque<Item> implements Deque<Item>, Iterable {
    private class Node {
        public Item item;
        public Node next;
        public Node prev;

        public Node(Item i, Node p, Node n) {
            item = i;
            prev = p;
            next = n;
        }
    }

    class ListIterator<Item> implements Iterator {
        Node current;

        // initialize pointer to head of the list for iteration
        public ListIterator(Node n)
        {
            current = n;
        }

        // returns false if next element does not exist
        public boolean hasNext()
        {
            return current != null;
        }

        public Item next()
        {
            Item data = (Item) current.item;
            current = current.next;
            return data;
        }

        public void remove()
        {
            throw new UnsupportedOperationException();
        }
    }

    private Node sentinel;
    private int size;

    public LinkedListDeque(Item i) {
        sentinel = new Node(null, sentinel, sentinel);
        sentinel.next = new Node(i, sentinel, sentinel);
        sentinel.prev = sentinel.next;
        size += 1;
    }

    public LinkedListDeque() {
        sentinel = new Node(null, sentinel, sentinel);
        size = 0;
    }

    public Item getRecursive(int index) {
        if (index >= size) return null;
        return getRecursiveHelper(index, sentinel.next);
    }

    public Item getRecursiveHelper(int index, Node n) {
        if (index == 0) return n.item;
        return getRecursiveHelper(index-1, n.next);
    }

    @Override
    public void addFirst(Item i){
        sentinel.next = new Node(i, sentinel, sentinel.next);
        if (size == 0) sentinel.prev = sentinel.next;
        size += 1;
    }

    @Override
    public void addLast(Item i){
        sentinel.prev.next = new Node(i, sentinel.prev, sentinel);
        sentinel.prev = sentinel.prev.next;
        size += 1;
    }

    @Override
    public boolean isEmpty(){
        if (size == 0) return true;
        return false;
    }

    @Override
    public int size(){
        return size;
    }

    @Override
    public void printDeque(){
        Node pointer = sentinel.next;
        for (int i = 0; i < size; i++){
            System.out.println(pointer.item + " ");
            pointer = pointer.next;
        }
        System.out.println("\n");
    }

    @Override
    public Item removeFirst(){
        if (size < 1) return null;
        sentinel.next = sentinel.next.next;
        sentinel.next.prev = sentinel;
        return sentinel.next.item;
    }

    @Override
    public Item removeLast(){
        if (size == 0) return null;
        sentinel.prev = sentinel.prev.prev;
        sentinel.prev.next = sentinel;
        return sentinel.prev.item;
    }

    @Override
    public Item get(int index){
        if (index >= size) return null;
        Node pointer = sentinel;
        for (int i = 0; i <= index; i++){
            pointer = pointer.next;
        }
        return pointer.item;
    }

    public Iterator<Item> iterator(){
        return new ListIterator<Item> (sentinel.next);
    }

    public boolean equals(Object o){
        if (o instanceof LinkedListDeque){
            if (((LinkedListDeque<Item>) o).size != size) return false;
            for (int i = 0; i < size; i++){
                if (((LinkedListDeque<Item>) o).get(i) != this.get(i)) return false;
            }
        }
        return false;
    }
}
