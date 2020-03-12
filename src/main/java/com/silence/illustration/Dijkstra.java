package com.silence.illustration;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author 李晓冰
 * @date 2020年03月02日
 */
public class Dijkstra {
    private Map<String, Map<String, Integer>> graph;
    private Map<String, Integer> cost;
    private Map<String, String> parents;
    private Set<String> processed;

    public Dijkstra() {
        initGraph();
        initCost();
        initParents();
        processed = new HashSet<>();
    }

    private void initGraph() {
        this.graph = new HashMap<>();
        this.graph.put("lopo", new HashMap() {{
            this.put("disk", 5);
            this.put("pictorial", 0);
        }});
        this.graph.put("disk", new HashMap() {{
            this.put("guitar", 15);
            this.put("drum", 20);
        }});
        this.graph.put("pictorial", new HashMap() {{
            this.put("guitar", 30);
            this.put("drum", 35);
        }});
        this.graph.put("guitar", new HashMap() {{
            this.put("piano", 35);
        }});
        this.graph.put("drum", new HashMap() {{
            this.put("piano", 10);
        }});
        this.graph.put("piano", new HashMap<>());
    }

    private void initCost() {
        cost = new HashMap<>();
        cost.put("disk", 5);
        cost.put("pictorial", 0);
        cost.put("guitar", Integer.MAX_VALUE);
        cost.put("drum", Integer.MAX_VALUE);
        cost.put("piano", Integer.MAX_VALUE);
    }

    private void initParents() {
        parents = new HashMap<>();
        parents.put("disk", "lopo");
        parents.put("pictorial", "lopo");
    }

    private String findLowest() {
        Integer minCost = Integer.MAX_VALUE;
        String minNode = null;
        Set<String> sets = cost.keySet();
        for (String key : sets) {
            Integer val = cost.get(key);
            if (val < minCost && !processed.contains(key)) {
                minCost = val;
                minNode = key;
            }
        }
        return minNode;
    }


    public void calculate(){
        String minNode = findLowest();
        while (minNode != null) {
            Integer baseCost = cost.get(minNode);
            Map<String, Integer> neighbors = graph.get(minNode);
            for (String neighborKey : neighbors.keySet()) {
                Integer neighborCost = neighbors.get(neighborKey);
                if (baseCost + neighborCost < cost.get(neighborKey)) {
                    cost.put(neighborKey, baseCost + neighborCost);
                    parents.put(neighborKey, minNode);
                }
            }
            processed.add(minNode);
            minNode = findLowest();
        }
    }

    public void printResult(){
        System.out.println("min cost:" + cost.get("piano"));
        Stack<String> pathStack = new Stack<>();
        String target = "piano";
        while (target != null) {
            pathStack.push(target);
            target = parents.get(target);
        }
        StringBuilder sb = new StringBuilder();
        while (pathStack.size() != 0) {
            sb.append(pathStack.pop())
            .append(",");
        }
        System.out.println(sb.toString());
    }

    public static void main(String[] args) {
        Dijkstra dijkstra = new Dijkstra();
        dijkstra.calculate();
        dijkstra.printResult();
    }
}

