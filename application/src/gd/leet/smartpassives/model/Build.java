package gd.leet.smartpassives.model;

import org.apache.commons.codec.binary.Base64;
import org.jgap.Gene;
import org.jgap.IChromosome;
import org.jgap.InvalidConfigurationException;
import org.jgap.impl.IntegerGene;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
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
        if (chromosome != null && chromosome.getGenes() != null) {
            for (Gene gene : chromosome.getGenes()) {
                Node node = this.tree.getNodeMap().get((Integer) gene.getAllele());
                if (node != null) {
                    this.takenNodes.add(node);
                }
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
            int connectionCount = 0;
            for (Node connection : node.getConnections()) {
                if (this.takenNodes.contains(connection)) {
                    connectionCount++;
                }
                List<Node> testList = new ArrayList<Node>();
                for (Node connection2 : node.getConnections()) {
                    if (connection2 != connection) {
                        testList.add(connection2);
                    }
                }
                testList.add(node);
                if (connection.getConnections().containsAll(testList) && connection.getConnections().size() == testList.size()) {
                    removeableNodes.add(connection);
                }
            }
            if (connectionCount <= 1 && !removeableNodes.contains(node)) { // can be 0 on start nodes
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

    public String toURL() {
        byte[] b = new byte[(this.getTakenNodes().size()) * 2 + 6];
        b[0] = 0;
        b[1] = 0;
        b[2] = 0;
        b[3] = 2;
        b[4] = (byte)(3); // 3 = witch I think
        b[5] = 0;
        int pos = 6;
        for (Node node : this.takenNodes) {
            ByteBuffer buffer = ByteBuffer.allocate(4).order(ByteOrder.nativeOrder());
            buffer.putInt(node.getId());
            byte[] nodeByte = buffer.array();
            b[pos++] = nodeByte[1];
            b[pos++] = nodeByte[0];
        }
        return "http://www.pathofexile.com/passive-skill-tree/" + Base64.encodeBase64String(b).replace("/", "_").replace("+", "-");
    }
}
