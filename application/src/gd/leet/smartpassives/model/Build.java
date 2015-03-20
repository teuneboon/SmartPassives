package gd.leet.smartpassives.model;

import java.util.ArrayList;
import java.util.List;

public class Build {
    private List<Node> takenNodes;

    public Build() {
        this.takenNodes = new ArrayList<Node>();
    }

    public List<Node> getAvailableNodes() {
        List<Node> availableNodes = new ArrayList<Node>();
        for (Node node : this.takenNodes) {
            for (Node potentialNode : node.getConnections()) {
                if (!this.takenNodes.contains(potentialNode)) {
                    availableNodes.add(potentialNode);
                }
            }
        }
        return availableNodes;
    }

    public List<Node> getRemoveableNodes() {
        List<Node> removeableNodes = new ArrayList<Node>();
        for (Node node : this.takenNodes) {
            if (node.getConnections().size() == 1) {
                removeableNodes.add(node);
            }
        }
        return removeableNodes;
    }

    public List<Node> getTakenNodes() {
        return takenNodes;
    }

    public void setTakenNodes(List<Node> takenNodes) {
        this.takenNodes = takenNodes;
    }
}
