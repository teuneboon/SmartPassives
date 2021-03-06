package gd.leet.smartpassives;

import gd.leet.smartpassives.model.Build;
import gd.leet.smartpassives.model.Node;
import gd.leet.smartpassives.model.ParsedSkillTree;
import gd.leet.smartpassives.model.Tree;
import org.jgap.*;
import org.jgap.event.GeneticEvent;
import org.jgap.event.GeneticEventListener;
import org.jgap.impl.DefaultConfiguration;
import org.jgap.impl.IntegerGene;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Application {
    public int CHROMOSOME_LENGTH = 120;
    public int POPULATION_SIZE = 200;
    public double BASE_MUTATION_RATE = 5;
    int STAGNATION_LIMIT_MIN = 50;
    public int NUM_THREADS = 8;
    public int MAX_NUM_THREADS = 32;
    public Tree TREE;

    public Double bestScore = (double) 0;
    public Double waterMark = (double) 0;
    public static Double[] bestScores;
    public static Integer[] evolutionsSinceDiscovery ;
    public Integer stagnationLimit = 0;
    private boolean newbestscore = false;
    private boolean firstrun = true;

    public void run() throws Exception {
        bestScore = (double) 0;
        bestScores = new Double[NUM_THREADS];
        evolutionsSinceDiscovery = new Integer[NUM_THREADS];
        TREE = new ParsedSkillTree();
        TREE.fill();

        for (int threadIndex = 0; threadIndex < NUM_THREADS; ++threadIndex) {
            spawn(threadIndex);
        }

//        for (int i = 0; i < 100; ++i) {
//            population.evolve();
//            IChromosome fittest = population.getFittestChromosome();
//            System.out.println(fittest.getFitnessValue());
//            System.out.println(PassiveTreeFitnessFunction.extractValidNodes(fittest, test, "witch"));
//        }
    }

    private static HashMap<String, Integer> getTargetStats() {
        HashMap<String, Integer> targetStats = new HashMap<String, Integer>();
        targetStats.put("% increased maximum Energy Shield", 310);
        targetStats.put("% increased Mana Regeneration Rate", 100);
        targetStats.put("% increased Spell Damage", 50);
        targetStats.put("% increased Cast Speed", 14);
        targetStats.put("% increased Critical Strike Chance for Spells", 250);
        targetStats.put("% increased Critical Strike Multiplier for Spells", 50);
        targetStats.put("% increased Critical Strike Chance", 150);
        targetStats.put("% increased Critical Strike Multiplier", 30);
        targetStats.put("% increased Lightning Damage", 90);
        targetStats.put("% reduced Mana Reserved", 22);
//        targetStats.put("Minions deal % increased Damage", 105);
//        targetStats.put("% increased effect of Flasks", 12);
//        targetStats.put("% reduced Mana Reserved", 22);
        return targetStats;
    }

    private void spawn(final int threadIndex) throws InvalidConfigurationException {
        bestScores[threadIndex] = (double) -1;
        evolutionsSinceDiscovery[threadIndex] = 0;

        HashMap<String, Integer> targetStats = getTargetStats();
        final PassiveTreeFitnessFunction fitnessFunction = new PassiveTreeFitnessFunction(TREE, targetStats, "witch");
        final Configuration conf = constructConfiguration(threadIndex, fitnessFunction, TREE);
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
                } else {
                    evolutionsSinceDiscovery[threadIndex]++;
                }

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
        ParsedSkillTree skillTree = new ParsedSkillTree();
        skillTree.fill();

        List<Node> validNodes = PassiveTreeFitnessFunction.extractValidNodes(fittest, skillTree, "witch");
        Build build = new Build(skillTree, fittest);

        System.out.println(PassiveTreeFitnessFunction.percentageOfStats(fittest, skillTree, "witch", getTargetStats()) + "% in " + validNodes.size() + " nodes");
        System.out.println(validNodes);
        System.out.println(build.toURL());
    }

    private Configuration constructConfiguration(final int threadIndex, PassiveTreeFitnessFunction fitnessFunc, Tree tree) throws InvalidConfigurationException {
        final Configuration conf = new DefaultConfiguration(threadIndex + " thread.", threadIndex + " thread.");
        conf.setFitnessFunction(fitnessFunc);
        conf.getGeneticOperators().clear();
        conf.addGeneticOperator(GeneticOperators.getInsertionOperator(this));
        conf.addGeneticOperator(GeneticOperators.getDeletionOperator(this));
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
        Build build = new Build(TREE);
        for (int i = 0; i < CHROMOSOME_LENGTH; i++)
            try
            {
                IntegerGene g = new IntegerGene(conf);
                List<Node> availableNodes = build.getAvailableNodes();
                Node toAdd = availableNodes.get(ThreadLocalRandom.current().nextInt(availableNodes.size()));
                g.setAllele(TREE.getIdToIndex().get(toAdd.getId()));
                genes.add(g);
            }
            catch (InvalidConfigurationException e)
            {
                e.printStackTrace();
            }
        return genes.toArray(new Gene[genes.size()]);
    }
}