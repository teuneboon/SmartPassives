package gd.leet.smartpassives.model;

import java.util.HashMap;
import java.util.List;

public class Node {
    private List<Node> connections;
    private HashMap<String, Integer> stats;

    public Node() {
        this.stats = new HashMap<String, Integer>();
    }

    public Node(HashMap<String, Integer> stats) {
        this.stats = stats;
    }

    public List<Node> getConnections() {
        return connections;
    }

    public void setConnections(List<Node> connections) {
        this.connections = connections;
    }

    public HashMap<String, Integer> getStats() {
        return stats;
    }

    public void setStats(HashMap<String, Integer> stats) {
        this.stats = stats;
    }

    public void connect(Node node) {
        this.connections.add(node);
        node.getConnections().add(this);
    }
}
