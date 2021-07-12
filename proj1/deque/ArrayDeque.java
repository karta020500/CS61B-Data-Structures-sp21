package deque;

import java.util.Iterator;

public class ArrayDeque<Item> implements Deque<Item>, Iterable {
    private class ADequeIterator implements Iterator<Item> {
        ArrayDeque deque;
        int pointer;
        public ADequeIterator(ArrayDeque d)
        {
            deque = d;
            pointer = d.nextFirst;
        }

        @Override
        public boolean hasNext()
        {
            if (pointer == deque.nextLast-1) return false;
            return true;
        }

        @Override
        public Item next()
        {
            int first = pointer + 1;
            if (first == items.length) first = 0;
            Item data = items[first];
            pointer = first;
            return data;
        }
    }
    private Item[] items;
    private int nextFirst;
    private int nextLast;
    private int size;

    public ArrayDeque() {
        int capacity = 8;
        items = (Item[]) new Object[capacity];
        nextFirst = capacity / 2;
        nextLast = nextFirst + 1;
        size = 0;
    }

    private void resize(int capacity) {
        Item[] a = (Item[]) new Object[capacity];
        int first = nextFirst + 1;
        if (first >= size) first = 0;
        int count = 0;
        for (; count < size; count++){
            a[count] = items[first];
            first++;
            if (first == items.length) first = 0;
        }
        nextLast = count;
        nextFirst = a.length - 1;
        items = a;
    }

    @Override
    public void addFirst(Item i){
        if (size == items.length) {
            resize(size * 2);
        }
        items[nextFirst] = i;
        nextFirst--;
        if (nextFirst < 0) nextFirst = items.length - 1;
        size++;
    }

    @Override
    public void addLast(Item i){
        if (size == items.length) {
            resize(size * 2);
        }
        items[nextLast] = i;
        nextLast++;
        if (nextLast == items.length) nextLast = 0;
        size++;
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
    public void printDeque() {
        int first = nextFirst+1;
        for (int count = 0; count <= size; count++){
            System.out.println(items[first] + " ");
            first++;
            if (first == items.length) first = 0;
        }
        System.out.println("\n");
    }

    @Override
    public Item removeFirst() {
        if (size < 1) return null;
        if ((size < items.length / 2) && (size > 2)){
            resize(items.length / 2);
        }
        int first = nextFirst + 1;
        if (first == items.length) first = 0;
        Item i = items[first];
        nextFirst = first;
        size--;
        return i;
    }

    @Override
    public Item removeLast(){
        if (size < 1) return null;
        if ((size < items.length / 2) && (size > 2)){
           resize(items.length / 2);
        }
        int last = nextLast - 1;
        if (last < 0) last = items.length-1;
        Item i = items[last];
        nextLast = last;
        size--;
        return i;
    }

    @Override
    public Item get(int i){
        if (i >= size) return null;
        int first = nextFirst + 1 + i;
        if (first >= items.length) {
            int surpass = first - items.length;
            first = 0 + surpass;
        }
        return items[first];
    }

    @Override
    public Iterator<Item> iterator(){
        return new ADequeIterator (this);
    }

    public boolean equals(Object o){
        if (!(o instanceof ArrayDeque)) return false;
        if (((ArrayDeque<Item>) o).size != size) return false;
        for (int i = 0; i < size; i++){
            if (((ArrayDeque<Item>) o).get(i) != this.get(i)) return false;
        }
        return true;
    }
}
