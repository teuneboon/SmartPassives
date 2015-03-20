package gd.leet.smartpassives.model;

import org.jgap.Gene;
import org.jgap.IChromosome;

import java.util.ArrayList;
import java.util.List;

public class Build {
    private List<Node> takenNodes;
    private Tree tree;

    public Build(Tree tree) {
        this.takenNodes = new ArrayList<Node>();
        this.tree = tree;
    }

    public Build(Tree tree, IChromosome chromosome) {
        // @TODO this method is bad for seperation of concerns
        this.tree = tree;
        this.takenNodes = new ArrayList<Node>();
        for (Gene gene : chromosome.getGenes()) {
            this.takenNodes.add(this.tree.getNodeMap().get((Integer) gene.getAllele()));
        }
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
