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

    private class LDequeIterator implements Iterator<Item> {
        private Node sentinel;
        private Node node;

        public LDequeIterator(Node s)
        {
            sentinel = s;
            if (sentinel != sentinel.next){
                node = sentinel.next;
            } else node = null;
        }

        @Override
        public boolean hasNext()
        {
            return node.next != sentinel;
        }

        @Override
        public Item next()
        {
            Item data = (Item) node.item;
            node = node.next;
            return data;
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
        sentinel.next = sentinel;
        sentinel.prev = sentinel;
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
        if (size == 0) {
            sentinel.next.next = sentinel;
            sentinel.prev = sentinel.next;
        }
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
        Item i = sentinel.next.item;
        sentinel.next = sentinel.next.next;
        sentinel.next.prev = sentinel;
        size -= 1;
        return i;
    }

    @Override
    public Item removeLast(){
        if (size < 1) return null;
        Item i = sentinel.prev.item;
        sentinel.prev = sentinel.prev.prev;
        sentinel.prev.next = sentinel;
        size -= 1;
        return i;
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

    @Override
    public Iterator<Item> iterator(){
        return new LDequeIterator (sentinel);
    }

    public boolean equals(Object o){
        if (!(o instanceof LinkedListDeque)) return false;
        if (((LinkedListDeque<Item>) o).size != size) return false;
            for (int i = 0; i < size; i++){
                if (((LinkedListDeque<Item>) o).get(i) != this.get(i)) return false;
            }
        return true;
    }
}
