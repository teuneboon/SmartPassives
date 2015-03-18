package gd.leet.smartpassives.model;

import java.util.HashMap;
import java.util.List;

public abstract class Tree {
    protected HashMap<String, List<Node>> startNodes = new HashMap<String, List<Node>>();

    public List<Node> getStartNodesForClass(String _class) {
        return this.startNodes.get(_class);
    }

    protected void setStartNodesForClass(String _class, List<Node> nodes) {
        this.startNodes.put(_class, nodes);
    }

    public abstract void fill();
}
