package gd.leet.smartpassives.model;

import org.jgap.Gene;
import org.jgap.IChromosome;
import org.jgap.InvalidConfigurationException;
import org.jgap.impl.IntegerGene;

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
            Node node = this.tree.getNodeMap().get((Integer) gene.getAllele());
            if (node != null) {
                this.takenNodes.add(node);
            }
        }
    }

    public void setGenes(IChromosome chromosome) throws InvalidConfigurationException {
        int i = 0;
        Gene[] genes = new Gene[this.takenNodes.size()];
        for (Node node : this.takenNodes) {
            Gene gene = new IntegerGene(chromosome.getConfiguration());
            gene.setAllele(tree.getIdToIndex().get(node.getId()));
            genes[i++] = gene;
        }
        chromosome.setGenes(genes);
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
        for (Node node : this.tree.getStartNodesForClass("witch")) {
            if (!this.takenNodes.contains(node)) {
                availableNodes.add(node);
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
