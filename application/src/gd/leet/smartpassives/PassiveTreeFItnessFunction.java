package gd.leet.smartpassives;

import gd.leet.smartpassives.model.Node;
import org.jgap.FitnessFunction;
import org.jgap.IChromosome;

import java.util.HashMap;
import java.util.List;

public class PassiveTreeFItnessFunction extends FitnessFunction {
    private HashMap<String, Integer> targetStats;

    public PassiveTreeFItnessFunction(HashMap<String, Integer> targetStats) {
        this.targetStats = targetStats;
    }

    protected double evaluate(IChromosome iChromosome) {
        double fitness = this.percentageOfStats(iChromosome);
        return 0;
    }

    private List<Node> extractValidNodes(IChromosome iChromosome) {

    }

    private double percentageOfStats(IChromosome iChromosome) {

        return 0;
    }
}
