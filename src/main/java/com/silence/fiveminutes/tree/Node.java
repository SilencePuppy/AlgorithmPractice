package com.silence.fiveminutes.tree;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 李晓冰
 * @date 2020年03月15日
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Node<T extends Comparable<T>> {
    private T data;
    private Node<T> left;
    private Node<T> right;
}
