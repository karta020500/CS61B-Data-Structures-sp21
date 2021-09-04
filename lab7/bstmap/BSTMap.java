package bstmap;

import java.util.Iterator;
import java.util.Set;

public class BSTMap implements Map61B<K, V>{

    /** Removes all of the mappings from this map. */
    public void clear(){

    };

    /* Returns true if this map contains a mapping for the specified key. */
    public boolean containsKey(K key){
        return true;
    };

    /* Returns the value to which the specified key is mapped, or null if this
     * map contains no mapping for the key.
     */
    public V get(K key){

    };

    /* Returns the number of key-value mappings in this map. */
    public int size(){

    };

    /* Associates the specified value with the specified key in this map. */
    public void put(K key, V value){

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
