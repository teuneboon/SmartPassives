package gd.leet.smartpassives;

import gd.leet.smartpassives.model.Node;
import gd.leet.smartpassives.model.Tree;
import org.jgap.FitnessFunction;
import org.jgap.Gene;
import org.jgap.IChromosome;
import org.jgap.impl.IntegerGene;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PassiveTreeFitnessFunction extends FitnessFunction {
    private HashMap<String, Integer> targetStats;
    private Tree tree;
    private String _class; // eventually we want to calculate best class

    public PassiveTreeFitnessFunction(Tree tree, HashMap<String, Integer> targetStats, String _class) {
        this.targetStats = targetStats;
        this.tree = tree;
        this._class = _class;
    }

    protected double evaluate(IChromosome iChromosome) {
        double fitness = this.percentageOfStats(iChromosome) * 100.0;
        fitness += (double) (100 - this.amountOfNodes(iChromosome));
        return fitness;
    }

    public static List<Node> getActionsInOrder(IChromosome iChromosome, Tree tree) {
        List<Node> nodes = new ArrayList<Node>();
        for (Gene gene1 : iChromosome.getGenes()) {
            IntegerGene gene = (IntegerGene) gene1;
            Integer passiveId = (Integer) gene.getAllele();
            nodes.add(tree.getNodeMap().get(passiveId));
        }
        return nodes;
    }

    public static List<Node> extractValidNodes(IChromosome iChromosome, Tree tree, String _class) {
        List<Node> nodes = new ArrayList<Node>();
        List<Node> nodesToConsider = PassiveTreeFitnessFunction.getActionsInOrder(iChromosome, tree);
        for (Node node : nodesToConsider) {
            if (nodes.size() == 0) {
                // we are looking for starting node
                if (tree.getStartNodesForClass(_class).contains(node) && !nodes.contains(node)) {
                    nodes.add(node);
                }
            } else {
                for (Node node2 : nodes) {
                    if (node2.getConnections().contains(node) && !nodes.contains(node)) {
                        nodes.add(node);
                        break;
                    }
                }
            }
        }
        return nodes;
    }

    private HashMap<String, Integer> getStats(List<Node> nodes) {
        HashMap<String, Integer> result = new HashMap<String, Integer>();
        for (Node node : nodes) {
            for (Map.Entry<String, Integer> entry : node.getStats().entrySet()) {
                if (result.containsKey(entry.getKey())) {
                    result.put(entry.getKey(), result.get(entry.getKey()) + entry.getValue());
                } else {
                    result.put(entry.getKey(), entry.getValue());
                }
            }
        }
        return result;
    }

    private double percentageOfStats(IChromosome iChromosome) {
        HashMap<String, Integer> stats = this.getStats(PassiveTreeFitnessFunction.extractValidNodes(iChromosome, this.tree, this._class));
        double[] percentages = new double[this.targetStats.size()];
        int i = 0;
        for (Map.Entry<String, Integer> entry : this.targetStats.entrySet()) {
            int value = 0;
            if (stats.containsKey(entry.getKey())) {
                value = stats.get(entry.getKey());
            }
            double percentage = (double)value/(double)entry.getValue();
            if (percentage > 1) {
                percentage = 1;
            }
            percentages[i++] = percentage;
        }
        double sum = 0;
        for (double a : percentages) {
            sum += a;
        }
        return (sum / (double) percentages.length) * 100.0d;
    }

    private int amountOfNodes(IChromosome iChromosome) {
        return PassiveTreeFitnessFunction.extractValidNodes(iChromosome, this.tree, this._class).size();
    }
}
