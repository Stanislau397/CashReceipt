package ru.clevertec.cashreceipt.cache;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Node<K, V> {
    private K key;
    private V value;
    private Node<K, V> next;
    private Node<K, V> prev;
}
