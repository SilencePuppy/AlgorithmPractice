package com.silence.illustration;

/**
 * @author 李晓冰
 * @date 2020年02月25日
 */
public class Queue<T> {
    Object[] elements;
    int n;
    int size;

    int head;
    int tail;

    public Queue(int capacity) {
        this.elements = new Object[capacity];
        this.n = capacity;
    }

    public Queue() {
        this(10);
    }

    public boolean enqueue(Object e) {
        if (tail == n) {
            if (size == n) {
                return false;
            } else {
                for (int i = head; i < tail; i++) {
                    elements[i - head] = elements[i];
                }
                tail = tail - head;
                head = 0;
            }
        }
        elements[tail++] = e;
        size++;
        return true;
    }

    public int size() {
        return size;
    }

    @SuppressWarnings("unchecked")
    public T dequeue() {
        if (head == tail) {
            return null;
        }
        Object e = elements[head--];
        size--;
        return (T) e;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Queue [");
        for (int i = head; i < tail; i++) {
            Object e = elements[i];
            sb.append(e.toString());
            if (i != tail - 1) {
                sb.append(",");
            }
        }
        sb.append("]");
        return sb.toString();
    }

    public static void main(String[] args) {
        Queue<String> queue = new CircleQueue<>();
        queue.enqueue("123");
        queue.enqueue("124");
        queue.enqueue("125");
        queue.enqueue("126");
        System.out.println(queue.toString());
    }
}

class CircleQueue<T> extends Queue<T> {
    @Override
    public boolean enqueue(Object e) {
        if ((tail + 1) % n == head) {
            return false;
        }
        elements[tail] = e;
        tail = (tail + 1) % n;
        size++;
        return true;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T dequeue() {
        if (head == tail) {
            return null;
        }
        Object e = elements[head];
        head = (head + 1) % n;
        size--;
        return (T) e;
    }
}