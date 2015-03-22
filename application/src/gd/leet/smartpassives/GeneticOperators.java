package gd.leet.smartpassives;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import gd.leet.smartpassives.model.Build;
import gd.leet.smartpassives.model.Node;
import org.jgap.GeneticOperator;
import org.jgap.IChromosome;
import org.jgap.InvalidConfigurationException;
import org.jgap.Population;

import org.jgap.util.ICloneable;

/**
 * Mostly based on https://github.com/infinity0/Application/blob/master/Application/src/java/com/fray/evo/EcGeneticUtil.java
 */
public class GeneticOperators {
    static GeneticOperator getDeletionOperator(final Application app)
    {
        return new GeneticOperator()
        {
            @Override
            public void operate(Population population, List arg1)
            {
                for (int i = 0; i < population.size(); i++)
                {
                    if (Math.random() > app.BASE_MUTATION_RATE/app.CHROMOSOME_LENGTH)
                        continue;
                    IChromosome chromosome = (IChromosome) ((ICloneable) population.getChromosome(i)).clone();
                    Build build = new Build(app.TREE, chromosome);
                    int tries = ThreadLocalRandom.current().nextInt(1) + 1; // remove a max of 2 nodes
                    for (int j = 0; j < tries; ++j) {
                        List<Node> removeableNodes = build.getRemoveableNodes();
                        if (removeableNodes.size() >= 1) {
                            Node toRemove = removeableNodes.get(ThreadLocalRandom.current().nextInt(removeableNodes.size()));
                            build.getTakenNodes().remove(toRemove);
                        }
                    }

                    try {
                        build.setGenes(chromosome);
                    } catch (InvalidConfigurationException e) {
                        e.printStackTrace();
                    }
                    arg1.add(chromosome);
                }
            }
        };
    }

    static GeneticOperator getInsertionOperator(final Application app)
    {
        return new GeneticOperator()
        {
            @Override
            public void operate(Population population, List arg1)
            {
                for (int i = 0; i < population.size(); i++)
                {
                    if (Math.random() > app.BASE_MUTATION_RATE/app.CHROMOSOME_LENGTH)
                        continue;
                    IChromosome chromosome = (IChromosome) ((ICloneable) population.getChromosome(i)).clone();
                    Build build = new Build(app.TREE, chromosome);
                    int tries = ThreadLocalRandom.current().nextInt(5) + 1; // add a max of 6 nodes
                    for (int j = 0; j < tries; ++j) {
                        List<Node> availableNodes = build.getAvailableNodes();
                        if (availableNodes.size() >= 1) {
                            Node toAdd = availableNodes.get(ThreadLocalRandom.current().nextInt(availableNodes.size()));
                            build.getTakenNodes().add(toAdd);
                        }
                    }
                    try {
                        build.setGenes(chromosome);
                    } catch (InvalidConfigurationException e) {
                        e.printStackTrace();
                    }
                    arg1.add(chromosome);
                }
            }
        };
    }
}