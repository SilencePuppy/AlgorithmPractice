package com.silence.illustration;


import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * leetcode: 20,155,232,844,224,682,496
 * 1.模拟浏览器前进后退
 * 2.模拟实现字符串的+-/*
 * 3.判断括号匹配
 * @author 李晓冰
 * @date 2020年02月20日
 */
public class Stack<T> {
    Object[] data;
    /*The counter*/
    int count = 0;
    /*capacity of data*/
    int n = 10;

    static final int DEFAULT_CAPACITY = 10;

    public Stack(int n) {
        data = new Object[n];
        this.n = n;
    }

    public Stack() {
        this(DEFAULT_CAPACITY);
    }

    public void push(T e) {
        ensureCapacity();
        data[count++] = e;
    }

    @SuppressWarnings("unchecked")
    public T pop() {
        if (count == 0) {
            return null;
        }
        T t = (T) data[--count];
        return t;
    }

    @SuppressWarnings("unchecked")
    public T get() {
        if (count == 0) {
            return null;
        }
        return (T) data[count - 1];
    }

    public int size() {
        return count;
    }

    public void clear() {
        data = new Object[DEFAULT_CAPACITY];
        n = DEFAULT_CAPACITY;
        count = 0;
    }

    private void ensureCapacity() {
        if (count < n) {
            return;
        }
        Object[] newData = new Object[n * 2];
        for (int i = 0; i < n; i++) {
            newData[i] = data[i];
        }
        data = newData;
        n *= 2;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String s = scanner.nextLine();
        List<Node<?>> nodeList = Calculator.getNodeList(s);
        Calculator calculator = new Calculator();
        for (Node<?> node : nodeList) {
            calculator.add(node);
        }
        System.out.println(calculator.executeVal());
    }
}


class Calculator {
    Stack<NumberNode> stack1 = new Stack<>();
    Stack<OperateNode> stack2 = new Stack<>();

    public static List<Node<?>> getNodeList(String target) {
        List<Node<?>> returnList = new ArrayList<>();
        char[] chars = target.toCharArray();
        int i = 0, j = 0;
        for (; i < chars.length; i++) {
            if (isOperator(chars[i])) {
                Integer val = Integer.parseInt(new String(chars, j, i - j));
                Node<Integer> dataNode = DataFactory.createNode(val);
                returnList.add(dataNode);
                j = i + 1;
                Node<String> operateNode = DataFactory.createNode(new String(chars, i, 1));
                returnList.add(operateNode);
            }
        }
        Node<Integer> node = DataFactory.createNode(Integer.parseInt(new String(chars, j, i - j)));
        returnList.add(node);
        System.out.println(returnList);
        return returnList;
    }

    public static boolean isOperator(char c) {
        if ('*' == c || '/' == c || '+' == c || '-' == c) {
            return true;
        } else {
            return false;
        }
    }

    public <T> void add(Node<T> node) {
        if (node instanceof NumberNode) {
            stack1.push((NumberNode) node);
            return;
        }

        if (stack2.size() == 0) {
            stack2.push((OperateNode) node);
            return;
        }

        OperateNode operateNode = (OperateNode) node;
        OperateNode topNode = stack2.get();
        if (operateNode.compareTo(topNode) <= 0) {
            Integer val = executeTopOperator();
            add(DataFactory.createNode(val));
            add(operateNode);
        } else {
            stack2.push(operateNode);
        }
    }

    public Integer executeVal() {
        if (stack2.size() == 0) {
            throw new RuntimeException("wrong");
        }

        while (stack2.size() != 0) {
            Integer integer = executeTopOperator();
            Node<Integer> node = DataFactory.createNode(integer);
            stack1.push((NumberNode) node);
        }
        return stack1.pop().data;
    }

    private Integer executeTopOperator() {
        OperateNode operateNode = stack2.pop();
        NumberNode p1 = stack1.pop();
        NumberNode p2 = stack1.pop();
        return operateNode.operate(p2.data, p1.data);
    }
}


class DataFactory {
    @SuppressWarnings("unchecked")
    public static <T> Node<T> createNode(T data) {
        Node<?> node;
        if (data instanceof Integer) {
            node = new NumberNode((Integer) data);
        } else {
            node = new OperateNode((String) data);
        }
        return (Node<T>) node;
    }
}

class Node<T> {
    T data;

    @Override
    public String toString() {
        return "Node{" +
                "data=" + data +
                '}';
    }
}

class NumberNode extends Node<Integer> {
    public NumberNode(Integer data) {
        this.data = data;
    }
}

class OperateNode extends Node<String> {
    byte level;

    public OperateNode(String data) {
        this.data = data;
        switch (data) {
            case "*":
            case "/":
                level = 2;
                break;
            case "-":
            case "+":
                level = 0;
                break;
            default:
                throw new RuntimeException("wrong data");
        }
    }

    public int compareTo(OperateNode other) {
        return level - other.level;
    }

    public Integer operate(Integer p1, Integer p2) {
        int result;
        switch (data) {
            case "*":
                result = p1 * p2;
                break;
            case "/":
                result = p1 / p2;
                break;
            case "-":
                result = p1 - p2;
                break;
            case "+":
                result = p1 + p2;
                break;
            default:
                throw new RuntimeException("wrong data");
        }
        return result;
    }
}


class Browser {
    private Stack<String> stack1 = new Stack<>();
    private Stack<String> stack2 = new Stack<>();
    private static String defaultBlank = "www.baidu.com";

    /*click the browser icon and open the broswer*/
    public void open() {
        look(defaultBlank);
    }

    public void look(String url) {
        stack1.push(url);
        System.out.println("打开页面：" + url);
        if (stack2.size() > 0) {
            stack2.clear();
        }
    }

    public String back() {
        if (stack1.size() == 1) {
            System.out.println("打开页面：" + defaultBlank);
            return defaultBlank;
        }
        String url = stack1.pop();
        stack2.push(url);
        String s = stack1.get();
        System.out.println("打开页面：" + s);
        return s;
    }

    public String next() {
        if (stack2.size() == 0) {
            String url = stack1.get();
            System.out.println("打开页面：" + url);
            return url;
        }
        String url = stack2.pop();
        stack1.push(url);
        System.out.println("打开页面：" + url);
        return url;
    }
}
