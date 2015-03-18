package gd.leet.smartpassives.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Node {
    private List<Node> connections;
    private HashMap<String, Integer> stats;
    private String name;

    private void __init_stats() {
        this.stats = new HashMap<String, Integer>();
    }

    private void __init_connections() {
        this.connections = new ArrayList<Node>();
    }

    public Node(String name) {
        this.name = name;
    }

    public List<Node> getConnections() {
        if (this.connections == null) {
            this.__init_connections();
        }
        return this.connections;
    }

    public void setConnections(List<Node> connections) {
        this.connections = connections;
    }

    public HashMap<String, Integer> getStats() {
        if (this.stats == null) {
            this.__init_stats();
        }
        return this.stats;
    }

    public void setStats(HashMap<String, Integer> stats) {
        this.stats = stats;
    }

    public void connect(Node node) {
        this.getConnections().add(node);
        node.getConnections().add(this);
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String toString() {
        return this.getName();
    }
}
