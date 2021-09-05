package bstmap;

import java.util.Iterator;
import java.util.Set;

public class BSTMap<K extends Comparable, V> implements Map61B<K, V>{
    private class BSTNode {
        public K key;
        public V val;
        public BSTNode left;
        public BSTNode right;

        public BSTNode(K key, V val, BSTNode left, BSTNode right) {
            this.key = key;
            this.val = val;
            this.right = right;
            this.left = left;
        }
    }

    private int size;
    private BSTNode root;

    public BSTMap(){
        this.root = null;
        this.size = 0;
    }

    /** Removes all of the mappings from this map. */
    public void clear(){
        this.root = null;
        this.size = 0;
    };

    /* Returns true if this map contains a mapping for the specified key. */
    public boolean containsKey(K key){
        if (find(root, key) == null) return false;
        return true;
    };

    private BSTNode find(BSTNode root, K key){
        if (root == null) return null;
        if (root.key.equals(key)){
            return root;
        } else if (root.key < key){
            return find(root.left, key);
        } else {
            return find(root.right, key);
        }
    }

    /* Returns the value to which the specified key is mapped, or null if this
     * map contains no mapping for the key.
     */
    public V get(K key){
        if (this.root != null){

        }

    };

    /* Returns the number of key-value mappings in this map. */
    @Override
    public int size(){
        return this.size;
    };

    /* Associates the specified value with the specified key in this map. */
    public void put(K key, V value){
        if (this.root != null){

        } else {
            this.root = new BSTNode(key, value, null, null);
            this.size++;
        }


    };

    /* Returns a Set view of the keys contained in this map. Not required for Lab 7.
     * If you don't implement this, throw an UnsupportedOperationException. */
    public Set<K> keySet(){
        throw new UnsupportedOperationException();
    };

    /* Removes the mapping for the specified key from this map if present.
     * Not required for Lab 7. If you don't implement this, throw an
     * UnsupportedOperationException. */
    public V remove(K key){
        throw new UnsupportedOperationException();
    };

    /* Removes the entry for the specified key only if it is currently mapped to
     * the specified value. Not required for Lab 7. If you don't implement this,
     * throw an UnsupportedOperationException.*/
    public V remove(K key, V value){
        throw new UnsupportedOperationException();
    };

    @Override
    public Iterator<K> iterator() {
        return null;
    }
}
