package gd.leet.smartpassives;

import gd.leet.smartpassives.model.TestTree;
import gd.leet.smartpassives.model.Tree;
import org.jgap.*;
import org.jgap.impl.DefaultConfiguration;
import org.jgap.impl.IntegerGene;

import java.util.ArrayList;
import java.util.HashMap;

public class Application {
    public int CHROMOSOME_LENGTH = 120;
    public int POPULATION_SIZE = 200;
    public double BASE_MUTATION_RATE = 5;
    int STAGNATION_LIMIT_MIN = 50;

    public void run() throws Exception {
        Tree test = new TestTree();
        test.fill();

        HashMap<String, Integer> targetStats = new HashMap<String, Integer>();
        targetStats.put("strength", 30);
        targetStats.put("intelligence", 10);
        final PassiveTreeFitnessFunction fitnessFunction = new PassiveTreeFitnessFunction(test, targetStats, "witch");
        final Configuration conf = constructConfiguration(fitnessFunction, test);
        final Genotype population = Genotype.randomInitialGenotype(conf);
    }

    public static void main(String[] args) {
        try {
            new Application().run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Configuration constructConfiguration(PassiveTreeFitnessFunction fitnessFunc, Tree tree) throws InvalidConfigurationException {
        final Configuration conf = new DefaultConfiguration();
        conf.setFitnessFunction(fitnessFunc);
        conf.addGeneticOperator(GeneticOperators.getCleansingOperator(this));
        conf.addGeneticOperator(GeneticOperators.getInsertionOperator(this));
        conf.addGeneticOperator(GeneticOperators.getDeletionOperator(this));
        conf.addGeneticOperator(GeneticOperators.getTwiddleOperator(this));
        conf.addGeneticOperator(GeneticOperators.getSwapOperator(this));
        conf.setPopulationSize(POPULATION_SIZE);
        conf.setSelectFromPrevGen(1);
        conf.setPreservFittestIndividual(false);
        conf.setAlwaysCaculateFitness(false);
        conf.setKeepPopulationSizeConstant(false);
        Gene[] initialGenes = this.importInitialGenes(conf, tree);
        Chromosome c = new Chromosome(conf, initialGenes);
        conf.setSampleChromosome(c);
        return conf;
    }

    private Gene[] importInitialGenes(Configuration conf, Tree tree)
    {
        ArrayList<Gene> genes = new ArrayList<Gene>();
        for (int i = 0; i < CHROMOSOME_LENGTH; i++)
            try
            {
                IntegerGene g = new IntegerGene(conf, 0, tree.getNodeMap().size() - 1);
                g.setAllele(0);
                genes.add(g);
            }
            catch (InvalidConfigurationException e)
            {
                e.printStackTrace();
            }
        return genes.toArray(new Gene[genes.size()]);
    }
}