package ru.clevertec.cashreceipt.cache.impl;

import lombok.Getter;
import lombok.Setter;
import ru.clevertec.cashreceipt.cache.Cache;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Getter
public class LRUCache<T> implements Cache<T> {

    private final int capacity;
    private final Node head;
    private final Node tail;
    private final Map<Long, Node> nodeMap;

    public LRUCache(int capacity) {
        this.head = new Node();
        this.tail = new Node();
        this.head.setNext(tail);
        this.tail.setPrev(head);
        this.capacity = capacity;
        this.nodeMap = new HashMap<>(capacity);
    }

    @Override
    public void put(Long key, T value) {
        Node currentNode = nodeMap.get(key);
        if (currentNode != null) {
            removeNode(currentNode);
            currentNode.setValue(value);
            addNode(currentNode);
        }
        if (capacity == nodeMap.size()) {
            Node tailPreviousNode = tail.getPrev();
            Long tailKey = tailPreviousNode.getKey();
            nodeMap.remove(tailKey);
            removeNode(tailPreviousNode);
        }

        Node newNode = new Node();
        newNode.setKey(key);
        newNode.setValue(value);
        nodeMap.put(key, newNode);
        addNode(newNode);
    }

    @Override
    public Optional<T> get(Long key) {
        Node node = nodeMap.get(key);
        Optional<T> valueOptional = Optional.empty();
        if (node != null) {
            T value = node.getValue();
            valueOptional = Optional.of(value);
            removeNode(node);
            addNode(node);
        }
        return valueOptional;
    }

    @Override
    public void remove(Long key) {

        nodeMap.remove(key);
    }

    public void addNode(Node node) {
        Node headNext = this.head.getNext();
        this.head.setNext(node);
        node.setPrev(this.head);
        node.setNext(headNext);
        headNext.setPrev(node);
    }

    public void removeNode(Node node) {
        Node nextNode = node.getNext();
        Node prevNode = node.getPrev();

        nextNode.setPrev(prevNode);
        prevNode.setNext(nextNode);
    }

    @Setter
    @Getter
    private class Node {
        private Long key;
        private T value;
        private Node next;
        private Node prev;
    }
}
