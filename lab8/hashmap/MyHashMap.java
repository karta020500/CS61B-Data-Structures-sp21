package hashmap;

import java.util.*;

/**
 *  A hash table-backed Map implementation. Provides amortized constant time
 *  access to elements via get(), remove(), and put() in the best case.
 *
 *  Assumes null keys will never be inserted, and does not resize down upon remove().
 *  @author YOUR NAME HERE
 */
public class MyHashMap<K, V> implements Map61B<K, V> {
    /**
     * Protected helper class to store key/value pairs
     * The protected qualifier allows subclass access
     */
    protected class Node {
        K key;
        V value;

        Node(K k, V v) {
            key = k;
            value = v;
        }

        public void setValue(V value){
            this.value = value;
        }
    }

    /* Instance Variables */
    private Collection<Node>[] buckets;
    private int size;
    private int bucketSize = 16;
    private double maxLoadFactor = 0.75;
    // You should probably define some more!

    /** Constructors */
    public MyHashMap() {
        this.buckets = createTable(bucketSize);
    }

    public MyHashMap(int initialSize) {
        this.bucketSize = initialSize;
        this.buckets = createTable(initialSize);
    }

    /**
     * MyHashMap constructor that creates a backing array of initialSize.
     * The load factor (# items / # buckets) should always be <= loadFactor
     *
     * @param initialSize initial size of backing array
     * @param maxLoad maximum load factor
     */
    public MyHashMap(int initialSize, double maxLoad) {
        this.bucketSize = initialSize;
        this.maxLoadFactor = maxLoad;
        this.buckets = createTable(initialSize);
    }

    /**
     * Returns a new node to be placed in a hash table bucket
     */
    private Node createNode(K key, V value) {
        return new Node(key, value);
    }

    /**
     * Returns a data structure to be a hash table bucket
     *
     * The only requirements of a hash table bucket are that we can:
     *  1. Insert items (`add` method)
     *  2. Remove items (`remove` method)
     *  3. Iterate through items (`iterator` method)
     *
     * Each of these methods is supported by java.util.Collection,
     * Most data structures in Java inherit from Collection, so we
     * can use almost any data structure as our buckets.
     *
     * Override this method to use different data structures as
     * the underlying bucket type
     *
     * BE SURE TO CALL THIS FACTORY METHOD INSTEAD OF CREATING YOUR
     * OWN BUCKET DATA STRUCTURES WITH THE NEW OPERATOR!
     */
    protected Collection<Node> createBucket() {
        return new LinkedList<>();
    }

    /**
     * Returns a table to back our hash table. As per the comment
     * above, this table can be an array of Collection objects
     *
     * BE SURE TO CALL THIS FACTORY METHOD WHEN CREATING A TABLE SO
     * THAT ALL BUCKET TYPES ARE OF JAVA.UTIL.COLLECTION
     *
     * @param tableSize the size of the table to create
     */
    private Collection<Node>[] createTable(int tableSize) {
        return new Collection[tableSize];
    }

    // TODO: Implement the methods of the Map61B Interface below
    // Your code won't compile until you do so!

    /** Removes all of the mappings from this map. */
    public void clear() {
        buckets = createTable(bucketSize);
        this.size = 0;
    };

    /** Returns true if this map contains a mapping for the specified key. */
    public boolean containsKey(K key) {
        Node node = searchHelper(key);
        if (node != null) return true;
        return false;
    };

    private Node searchHelper(K key) {
        int bucketIndex = Math.floorMod(key.hashCode(), this.bucketSize);
        if (buckets[bucketIndex] == null) return null;
        Iterator iterator = buckets[bucketIndex].iterator();
        while (iterator.hasNext()){
            Node node = (Node)iterator.next();
            if (node.key.equals(key)){
                return node;
            }
        }
        return null;
    }

    /**
     * Returns the value to which the specified key is mapped, or null if this
     * map contains no mapping for the key.
     */
    public V get(K key) {
        Node node = searchHelper(key);
        if (node != null) return node.value;
        return null;
    };

    /** Returns the number of key-value mappings in this map. */
    public int size() {
        return this.size;
    };

    /**
     * Associates the specified value with the specified key in this map.
     * If the map previously contained a mapping for the key,
     * the old value is replaced.
     */
    @Override
    public void put(K key, V value) {
        Node node = searchHelper(key);
        if (node != null) {
            node.setValue(value);
        } else {
            node = createNode(key, value);
            int bucketIndex = Math.floorMod(key.hashCode(), this.bucketSize);
            if (buckets[bucketIndex] == null) buckets[bucketIndex] = createBucket();
            buckets[bucketIndex].add(node);
            this.size++;
            if ((size/bucketSize) > maxLoadFactor) resize();
        }
    };

    private void resize(){
        int newBucketSize = bucketSize*2;
        Collection<Node>[] newBuckets = createTable(newBucketSize);
        for (int i = 0; i < bucketSize; i++){
            if (buckets[i] == null) continue;
            Iterator iterator = buckets[i].iterator();
            while (iterator.hasNext()){
                Node node = (Node)iterator.next();
                int bucketIndex = Math.floorMod(node.key.hashCode(), newBucketSize);
                if (newBuckets[bucketIndex] == null) newBuckets[bucketIndex] = createBucket();
                newBuckets[bucketIndex].add(node);
            }
        }
        this.bucketSize = newBucketSize;
        this.buckets = newBuckets;
    }

    /** Returns a Set view of the keys contained in this map. */
    public Set<K> keySet() {
        Set<K> set = new HashSet<>();
        for (int i = 0; i < bucketSize; i++){
            if (buckets[i] == null) continue;
            Iterator iterator = buckets[i].iterator();
            while (iterator.hasNext()){
                Node node = (Node)iterator.next();
                set.add(node.key);
            }
        }
        return set;
    };

    /**
     * Removes the mapping for the specified key from this map if present.
     * Not required for Lab 8. If you don't implement this, throw an
     * UnsupportedOperationException.
     */
    public V remove(K key) {
        throw new UnsupportedOperationException();
    };

    /**
     * Removes the entry for the specified key only if it is currently mapped to
     * the specified value. Not required for Lab 8. If you don't implement this,
     * throw an UnsupportedOperationException.
     */
    public V remove(K key, V value) {
        throw new UnsupportedOperationException();
    };

    @Override
    public Iterator<K> iterator() {
        return null;
    }
}
