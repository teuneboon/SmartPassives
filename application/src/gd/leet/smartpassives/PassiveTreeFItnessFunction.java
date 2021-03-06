package gd.leet.smartpassives;

import gd.leet.smartpassives.model.Build;
import gd.leet.smartpassives.model.Node;
import gd.leet.smartpassives.model.Tree;
import org.jgap.FitnessFunction;
import org.jgap.IChromosome;

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
        double percentage = percentageOfStats(iChromosome, this.tree, this._class, this.targetStats);
        double fitness = percentage * 120.0;
        if (percentage >= 100) {
            fitness += (double) (120 - this.amountOfNodes(iChromosome));
            fitness += 2000; // else a build with 1000 nodes and 0% is better :D
        } else {
            fitness += (double) this.amountOfNodes(iChromosome); // we want as many valid nodes as possible if we're not complete yet
        }
        return fitness;
    }

    public static List<Node> extractValidNodes(IChromosome iChromosome, Tree tree, String _class) {
        Build build = new Build(tree, iChromosome);
        return build.getTakenNodes();
    }

    public static HashMap<String, Integer> getStats(List<Node> nodes) {
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
        result.put("raw_% increased maximum Energy Shield", result.get("% increased maximum Energy Shield"));
        if (result.containsKey("+ to Intelligence")) {
            int energyShieldIntBonus = result.get("+ to Intelligence") / 5;
            if (result.containsKey("% increased maximum Energy Shield")) {
                result.put("% increased maximum Energy Shield", result.get("% increased maximum Energy Shield") + energyShieldIntBonus);
            } else {
                result.put("% increased maximum Energy Shield", energyShieldIntBonus);
            }
        }

        if (result.containsKey("% more maximum Energy Shield")) {
            float multiplier = 1f + ((float) result.get("% more maximum Energy Shield") / 100f);
            result.put("% increased maximum Energy Shield", (int) ((float) result.get("% increased maximum Energy Shield") * multiplier));
        }
        return result;
    }

    public static double percentageOfStats(IChromosome iChromosome, Tree tree, String _class, HashMap<String, Integer> targetStats) {
        HashMap<String, Integer> stats = getStats(PassiveTreeFitnessFunction.extractValidNodes(iChromosome, tree, _class));
        double[] percentages = new double[targetStats.size()];
        int i = 0;
        for (Map.Entry<String, Integer> entry : targetStats.entrySet()) {
            int value = 0;
            if (stats.containsKey(entry.getKey())) {
                value = stats.get(entry.getKey());
            }
            double percentage = (double)value / (double)entry.getValue();
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
