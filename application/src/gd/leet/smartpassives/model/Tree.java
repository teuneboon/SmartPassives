package gd.leet.smartpassives.model;

import java.util.HashMap;
import java.util.List;

public abstract class Tree {
    protected HashMap<String, List<Node>> startNodes = new HashMap<String, List<Node>>();
    protected HashMap<Integer, Node> nodeMap = new HashMap<Integer, Node>();

    public List<Node> getStartNodesForClass(String _class) {
        return this.startNodes.get(_class);
    }

    public void setStartNodesForClass(String _class, List<Node> nodes) {
        this.startNodes.put(_class, nodes);
    }

    public abstract void fill();

    public HashMap<Integer, Node> getNodeMap() {
        return nodeMap;
    }

    public void setNodeMap(HashMap<Integer, Node> nodeMap) {
        this.nodeMap = nodeMap;
    }

    public void connect(int id1, int id2) {
        this.getNodeMap().get(id1).connect(this.getNodeMap().get(id2));
    }

    public int getLowerBound() {
        return 0;
    }

    public int getUpperBound() {
        return this.getNodeMap().size();
    }
}
