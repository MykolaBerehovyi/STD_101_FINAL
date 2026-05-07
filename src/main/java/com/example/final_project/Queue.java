package com.example.final_project;

public class Queue<T> {
    private Node<T> first;   // first elem
    private Node<T> last;    // last elem
    private int size;
    private final int maxSize;  // max queue length

    // inner node class
    private static class Node<T> {
        T data;
        Node<T> next;

        Node(T data) {
            this.data = data;
            this.next = null;
        }
    }

    // constructor with maximum size
    public Queue(int maxSize) {
        this.first = null;
        this.last = null;
        this.size = 0;
        this.maxSize = maxSize;
    }

    // add element to a queue
    public void add(T element) {
        if (size >= maxSize) {
            // If the queue is full, remove the oldest element
            pop();
        }

        Node<T> newNode = new Node<>(element);

        if (last == null) {
            // Emptty queue
            first = newNode;
            last = newNode;
        } else {
            // adding to the end
            last.next = newNode;
            last = newNode;
        }
        size++;
    }

    // delete and return first elem from a queue
    public T pop() {
        if (first == null) {
            return null;  // queue empty
        }

        T data = first.data;
        first = first.next;
        size--;

        if (first == null) {
            last = null;  // queue empty
        }

        return data;
    }

    // peek first element without deleting
    public T peek() {
        if (first == null) {
            return null;
        } else {
            return first.data;
        }
    }
    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }
}