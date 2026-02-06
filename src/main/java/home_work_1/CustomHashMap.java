package home_work_1;

import java.util.Objects;

public final class CustomHashMap<K, V>{
    private static final int SIZE_MAP = 8;
    private int size;
    private Node<K, V>[] table;

    static class Node<K,V> {
        final K key;
        V value;
        Node<K, V> next;

        Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    public int size(){ return size; }

    public V put(K key, V value){
        checkingTypes(key, value);
        initTable();
        V previousValue = null;
        int i = getIndex(key);

        if(table[i] == null){
            table[i] = new Node<>(key, value);
            size++;
        }
        else{
            var node = table[i];
            while (node != null){
                if(keysIsEqual(node.key, key)){
                    previousValue = node.value;
                    node.value = value;
                    break;
                }
                else if(node.next == null){
                    node.next = new Node<>(key, value);
                    size++;
                    break;
                }
                node = node.next;
            }
        }
        return previousValue;
    }

    public V get(K key){
        if(keyIsNull(key))
            return null;

        var node = table[getIndex(key)];
        while (node != null){
            if(keysIsEqual(node.key, key))
                return node.value;
            node = node.next;
        }
        return null;
    }

    public V remove(K key){
        if(keyIsNull(key))
            return null;

        int i = getIndex(key);
        var node = table[i];
        if(keysIsEqual(node.key, key)){
            var value = node.value;
            table[i] = node.next;
            size--;
            return value;
        }
        var previousNode = node;
        node = node.next;
        while (node != null){
            if(keysIsEqual(node.key, key)) {
                var value = node.value;
                previousNode.next = node.next;
                size--;
                return value;
            }
            previousNode = node;
            node = node.next;
        }
        return null;
    }

    public void clear(){
        if(table != null && size > 0){
            size = 0;
            for(int i = 0; i < SIZE_MAP; i++)
                table[i] = null;
        }
    }

    private int getIndex(K key){
        return key.hashCode() & (SIZE_MAP - 1);
    }

    private boolean keysIsEqual(K key1, K key2){
        return key1.hashCode() == key2.hashCode() && key1.equals(key2);
    }

    private boolean keyIsNull(K key){
        return key == null || table == null || table[getIndex(key)] == null;
    }

    private void checkingTypes(K key, V value){
        Objects.requireNonNull(key,"Ключ не может быть null");
        Objects.requireNonNull(value, "Значение не может быть null");
    }

    @SuppressWarnings({"unchecked"})
    private void initTable(){
        if(table == null) {
            table = (Node<K, V>[]) new Node[SIZE_MAP];
        }
    }
}
