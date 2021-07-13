package deque;

public interface Deque<Item> {
    public void addFirst(Item i);

    public void addLast(Item i);

    public default boolean isEmpty(){
        if (size() == 0) return true;
        return false;
    };

    public int size();

    public void printDeque();

    public Item removeFirst();

    public Item removeLast();

    public Item get(int i);
}
