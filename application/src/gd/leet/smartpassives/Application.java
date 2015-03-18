package gd.leet.smartpassives;

import gd.leet.smartpassives.model.TestTree;
import gd.leet.smartpassives.model.Tree;
import org.jgap.*;
import org.jgap.event.GeneticEvent;
import org.jgap.event.GeneticEventListener;
import org.jgap.impl.DefaultConfiguration;
import org.jgap.impl.IntegerGene;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class Application {
    public int CHROMOSOME_LENGTH = 120;
    public int POPULATION_SIZE = 200;
    public double BASE_MUTATION_RATE = 5;
    int STAGNATION_LIMIT_MIN = 50;

    public Double bestScore = (double) 0;
    public Double waterMark = (double) 0;
    public static Double[] bestScores;
    public static Integer[] evolutionsSinceDiscovery ;
    public Integer stagnationLimit = 0;
    private boolean newbestscore = false;
    private boolean firstrun = true;

    public void run() throws Exception {
        bestScore = (double) 0;
        bestScores = new Double[1];
        evolutionsSinceDiscovery = new Integer[1];
        spawn(0);

//        for (int i = 0; i < 100; ++i) {
//            population.evolve();
//            IChromosome fittest = population.getFittestChromosome();
//            System.out.println(fittest.getFitnessValue());
//            System.out.println(PassiveTreeFitnessFunction.extractValidNodes(fittest, test, "witch"));
//        }
    }

    private void spawn(final int threadIndex) throws InvalidConfigurationException {
        bestScores[threadIndex] = (double) 0;

        Tree test = new TestTree();
        test.fill();

        HashMap<String, Integer> targetStats = new HashMap<String, Integer>();
        targetStats.put("Intelligence", 30);
        targetStats.put("% increased Cast Speed", 6);
        targetStats.put("% increased Spell Damage", 40);
        targetStats.put("% increased maximum Mana", 8);
        final PassiveTreeFitnessFunction fitnessFunction = new PassiveTreeFitnessFunction(test, targetStats, "witch");
        final Configuration conf = constructConfiguration(fitnessFunction, test);
        final Genotype population = Genotype.randomInitialGenotype(conf);

        if (firstrun) {
            firstrun = false;
        } else {

        }

        final Thread thread = new Thread(population);

        conf.getEventManager().addEventListener(GeneticEvent.GENOTYPE_EVOLVED_EVENT, new GeneticEventListener()
        {
            @Override
            public void geneticEventFired(GeneticEvent a_firedEvent)
            {
                Collections.shuffle(conf.getGeneticOperators());
                BASE_MUTATION_RATE += .001;
                if (BASE_MUTATION_RATE >= CHROMOSOME_LENGTH / 2)
                    BASE_MUTATION_RATE = 1;
                IChromosome fittestChromosome = population.getFittestChromosome();
                double fitnessValue = fittestChromosome.getFitnessValue();
                if (fitnessValue > bestScores[threadIndex])
                {
                    bestScores[threadIndex] = fitnessValue;
                    evolutionsSinceDiscovery[threadIndex] = 0;
                    BASE_MUTATION_RATE = 1;
                }
                else
                    evolutionsSinceDiscovery[threadIndex]++;

                int highestevosSinceDiscovery = 0;
                for(int i = 0; i < bestScores.length; i++ )
                {
                    if(bestScores[i] >= bestScore)
                    {
                        if(evolutionsSinceDiscovery[i] > highestevosSinceDiscovery)
                            highestevosSinceDiscovery = evolutionsSinceDiscovery[i];
                    }
                }

                stagnationLimit = (int)Math.ceil(highestevosSinceDiscovery * (.5));

                if(fitnessValue < bestScore)
                {
                    if (evolutionsSinceDiscovery[threadIndex] > Math.max(stagnationLimit, STAGNATION_LIMIT_MIN) && fitnessValue < waterMark)
                    {
                        //Stagnation. Suicide village and try again.
                        System.out.println("Restarting thread " + threadIndex);
                        try {
                            spawn(threadIndex);
                        }
                        catch (InvalidConfigurationException e)
                        {
                            e.printStackTrace();
                        }
                        thread.interrupt();
                    }
                    else if(evolutionsSinceDiscovery[threadIndex] > Math.max(stagnationLimit, STAGNATION_LIMIT_MIN) * 3)
                    {
                        //Stagnation. Suicide village and try again.
                        System.out.println("Restarting thread " + threadIndex);
                        try {
                            spawn(threadIndex);
                        }
                        catch (InvalidConfigurationException e)
                        {
                            e.printStackTrace();
                        }
                        thread.interrupt();
                    }
                }
                else if (evolutionsSinceDiscovery[threadIndex] > Math.max(stagnationLimit, STAGNATION_LIMIT_MIN))
                {
                    if(newbestscore)
                    {
                        waterMark = fitnessValue;
                    }

                    int totalevoSinceDiscoveryOnBest = 0;
                    int numBestThreads = 0;

                    for(int i = 0; i < bestScores.length; i++ )
                    {
                        if(bestScores[i] >= bestScore)
                        {
                            numBestThreads++;
                            totalevoSinceDiscoveryOnBest += evolutionsSinceDiscovery[i];
                        }
                    }

                    if(totalevoSinceDiscoveryOnBest > Math.max(stagnationLimit, STAGNATION_LIMIT_MIN) * 3 * numBestThreads)
                    {
                        // Deadlock, open this thread.
                        System.out.println("Restarting thread " + threadIndex);
                        try
                        {
                            spawn(threadIndex);
                        }
                        catch (InvalidConfigurationException e)
                        {
                            e.printStackTrace();
                        }
                        thread.interrupt();
                    }
                }

                synchronized (bestScore)
                {
                    if (fitnessValue > bestScore)
                    {
                        BASE_MUTATION_RATE = 1;
                        bestScore = fitnessValue;
                        newbestscore = true;

                        displayChromosome(fittestChromosome);
                    }
                }
            }

        });
        thread.setPriority(Thread.MIN_PRIORITY);
        thread.start();
    }

    public static void main(String[] args) {
        try {
            new Application().run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void displayChromosome(IChromosome fittest) {
        Tree test = new TestTree();
        test.fill();

        System.out.println(fittest.getFitnessValue());
        System.out.println(PassiveTreeFitnessFunction.extractValidNodes(fittest, test, "witch"));
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