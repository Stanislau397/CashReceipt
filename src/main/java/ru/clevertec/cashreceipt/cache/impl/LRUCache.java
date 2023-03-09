package ru.clevertec.cashreceipt.cache.impl;

import lombok.Getter;
import lombok.Setter;
import ru.clevertec.cashreceipt.cache.Cache;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * The LRUCache represents an algorithm
 * that takes least recently used value
 * and removes it form cache
 *
 * @param <K> represents key
 * @param <V> represents value
 */
@Getter
public class LRUCache<K, V> implements Cache<K, V> {

    private final int capacity;
    private final Node head;
    private final Node tail;
    private final Map<K, Node> nodeMap;

    public LRUCache(int capacity) {
        this.head = new Node();
        this.tail = new Node();
        this.head.setNext(tail);
        this.tail.setPrev(head);
        this.capacity = capacity;
        this.nodeMap = new HashMap<>(capacity);
    }

    /**
     * Puts Node into the {@link LRUCache#nodeMap}
     * <p>
     * If Node is Present in {@link LRUCache#nodeMap} then remove it from DoublyLinkedList,
     * update the value and put it after {@link LRUCache#head}
     * <p>
     * If Node doesn't exist then check if capacity
     * is reached find node before {@link LRUCache#tail},
     * remove that node from {@link LRUCache#nodeMap},
     * remove it from DoublyLinkedList and add a new Node
     * after {@link LRUCache#head}
     */
    @Override
    public void put(K key, V value) {
        Node currentNode = nodeMap.get(key);
        if (currentNode != null) {
            removeNode(currentNode);
            currentNode.setValue(value);
            addNode(currentNode);
        }
        if (capacity == nodeMap.size()) {
            Node tailPreviousNode = tail.getPrev();
            var tailKey = tailPreviousNode.getKey();
            nodeMap.remove(tailKey);
            removeNode(tailPreviousNode);
        }
        Node newNode = new Node();
        newNode.setKey(key);
        newNode.setValue(value);
        nodeMap.put(key, newNode);
        addNode(newNode);
    }

    /**
     * Gets Value from nodeMap by provided key
     * <p>
     * If Node is null then return empty optional
     * <p>
     * If Node is not null then remove node from the
     * DoublyLinkedList and put after{@link LRUCache#head}
     */
    @Override
    public Optional<V> get(K key) {
        Node node = nodeMap.get(key);
        Optional<V> valueOptional = Optional.empty();
        if (node != null) {
            V value = node.getValue();
            valueOptional = Optional.of(value);
            removeNode(node);
            addNode(node);
        }
        return valueOptional;
    }

    /**
     * Removes Value from nodeMap by provided key
     */
    @Override
    public void remove(K key) {
        nodeMap.remove(key);
    }

    /**
     * Adds Node after {@link LRUCache#head}
     */
    public void addNode(Node node) {
        Node headNext = head.getNext();
        head.setNext(node);
        node.setPrev(head);
        node.setNext(headNext);
        headNext.setPrev(node);
    }

    /**
     * Removes Node from DoublyLinkedList
     */
    public void removeNode(Node node) {
        Node nextNode = node.getNext();
        Node prevNode = node.getPrev();

        nextNode.setPrev(prevNode);
        prevNode.setNext(nextNode);
    }

    /**
     * @author Stanislau Kachan
     * Class that represents DoublyLinkedList Node
     */
    @Setter
    @Getter
    private class Node {
        private K key;
        private V value;
        private Node next;
        private Node prev;
    }
}
