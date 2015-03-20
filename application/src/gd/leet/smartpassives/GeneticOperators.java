package gd.leet.smartpassives;

import java.util.List;

import gd.leet.smartpassives.model.Build;
import org.jgap.Gene;
import org.jgap.GeneticOperator;
import org.jgap.IChromosome;
import org.jgap.InvalidConfigurationException;
import org.jgap.Population;
import org.jgap.impl.IntegerGene;

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
            public void operate(Population arg0, List arg1)
            {
                for (int i = 0; i < arg0.size(); i++)
                {
                    if (Math.random() > app.BASE_MUTATION_RATE/app.CHROMOSOME_LENGTH)
                        continue;
                    IChromosome chromosome = (IChromosome) ((ICloneable) arg0.getChromosome(i)).clone();
                    Gene[] beforeArray = chromosome.getGenes();
                    for (int j = (int) (Math.random() * beforeArray.length); j < beforeArray.length - 1; j++)
                        beforeArray[j] = beforeArray[j + 1];
                    try
                    {
                        chromosome.setGenes(beforeArray);
                    }
                    catch (InvalidConfigurationException e)
                    {
                        e.printStackTrace();
                    }
                    arg1.add(chromosome);
                }
            }
        };
    }

    static GeneticOperator getCleansingOperator(final Application app)
    {
        return new GeneticOperator()
        {
            @Override
            public void operate(Population arg0, List arg1)
            {
                if (Math.random() > app.BASE_MUTATION_RATE/app.CHROMOSOME_LENGTH)
                    return;
                IChromosome best = arg0.determineFittestChromosome();
                for (int i = 0; i < best.getGenes().length; i++)
                {
                    IChromosome chromosome = (IChromosome) ((ICloneable) best).clone();
                    Gene[] beforeArray = chromosome.getGenes();
                    for (int j = (int) i; j < beforeArray.length - 1; j++)
                        beforeArray[j].setAllele(beforeArray[j + 1].getAllele());
                    try
                    {
                        chromosome.setGenes(beforeArray);
                    }
                    catch (InvalidConfigurationException e)
                    {
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
                    Gene[] beforeArray = chromosome.getGenes();
                    int randomPoint = (int) (Math.random() * beforeArray.length);
                    for (int j = randomPoint; j < beforeArray.length - 1; j++)
                        beforeArray[j + 1] = beforeArray[j];
                    beforeArray[randomPoint].setAllele((int) (Math.random() * ((IntegerGene) chromosome
                            .getGene(randomPoint)).getUpperBounds()));
                    try
                    {
                        chromosome.setGenes(beforeArray);
                    }
                    catch (InvalidConfigurationException e)
                    {
                        e.printStackTrace();
                    }
                    arg1.add(chromosome);
                }
            }
        };
    }

    private static GeneticOperator getLengthenOperator(final Application app)
    {
        return new GeneticOperator()
        {
            @Override
            public void operate(Population arg0, List arg1)
            {
                for (int i = 0; i < arg0.size(); i++)
                {
                    if (Math.random() > app.BASE_MUTATION_RATE/app.CHROMOSOME_LENGTH)
                        continue;
                    IChromosome chromosome = arg0.getChromosome(i);
                    Gene[] beforeArray = chromosome.getGenes();
                    Gene[] afterArray = new Gene[beforeArray.length + 1];
                    for (int j = 0; j < beforeArray.length; j++)
                        afterArray[j] = beforeArray[j];
                    afterArray[afterArray.length - 1] = afterArray[0].newGene();
                    try
                    {
                        chromosome.setGenes(afterArray);
                    }
                    catch (InvalidConfigurationException e)
                    {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
                IChromosome chromosome = arg0.getConfiguration().getSampleChromosome();
                Gene[] beforeArray = chromosome.getGenes();
                Gene[] afterArray = new Gene[beforeArray.length + 1];
                for (int j = 0; j < beforeArray.length; j++)
                    afterArray[j] = beforeArray[j];
                afterArray[afterArray.length - 1] = afterArray[0].newGene();
                try
                {
                    chromosome.setGenes(afterArray);
                }
                catch (InvalidConfigurationException e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        };
    }

    private static GeneticOperator getShortenOperator(final Application app)
    {
        return new GeneticOperator()
        {
            @Override
            public void operate(Population arg0, List arg1)
            {
                if (Math.random() > app.BASE_MUTATION_RATE/app.CHROMOSOME_LENGTH)
                    return;
                for (int i = 0; i < arg0.size(); i++)
                {
                    IChromosome chromosome = arg0.getChromosome(i);
                    Gene[] beforeArray = chromosome.getGenes();
                    Gene[] afterArray = new Gene[beforeArray.length - 1];
                    for (int j = 0; j < afterArray.length; j++)
                        afterArray[j] = beforeArray[j];
                    try
                    {
                        chromosome.setGenes(afterArray);
                    }
                    catch (InvalidConfigurationException e)
                    {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
                IChromosome chromosome = arg0.getConfiguration().getSampleChromosome();
                Gene[] beforeArray = chromosome.getGenes();
                Gene[] afterArray = new Gene[beforeArray.length - 1];
                for (int j = 0; j < afterArray.length; j++)
                    afterArray[j] = beforeArray[j];
                try
                {
                    chromosome.setGenes(afterArray);
                }
                catch (InvalidConfigurationException e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        };
    }

    static GeneticOperator getSwapOperator(final Application app)
    {
        return new GeneticOperator()
        {
            @Override
            public void operate(Population arg0, List arg1)
            {
                for (int i = 0; i < arg0.size(); i++)
                {
                    if (Math.random() > app.BASE_MUTATION_RATE/app.CHROMOSOME_LENGTH)
                        continue;
                    IChromosome chromosome = (IChromosome) ((ICloneable) arg0.getChromosome(i)).clone();
                    Gene[] beforeArray = chromosome.getGenes();
                    int randomPoint = (int) (Math.random() * beforeArray.length);
                    int randomPoint2 = (int) (Math.random() * beforeArray.length);
                    Gene swap = beforeArray[randomPoint];
                    beforeArray[randomPoint] = beforeArray[randomPoint2];
                    beforeArray[randomPoint2] = swap;
                    try
                    {
                        chromosome.setGenes(beforeArray);
                    }
                    catch (InvalidConfigurationException e)
                    {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    arg1.add(chromosome);
                }
            }
        };
    }

    static GeneticOperator getTwiddleOperator(final Application app)
    {
        return new GeneticOperator()
        {
            @Override
            public void operate(Population arg0, List arg1)
            {
                for (int i = 0; i < arg0.size(); i++)
                {
                    if (Math.random() > app.BASE_MUTATION_RATE/app.CHROMOSOME_LENGTH)
                        continue;
                    IChromosome chromosome = (IChromosome) ((ICloneable) arg0.getChromosome(i)).clone();
                    Gene[] beforeArray = chromosome.getGenes();
                    int randomPoint = (int) (Math.random() * beforeArray.length);
                    if (randomPoint < beforeArray.length - 1)
                    {
                        Gene swap = beforeArray[randomPoint];
                        beforeArray[randomPoint] = beforeArray[randomPoint + 1];
                        beforeArray[randomPoint + 1] = swap;
                    }
                    try
                    {
                        chromosome.setGenes(beforeArray);
                    }
                    catch (InvalidConfigurationException e)
                    {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    arg1.add(chromosome);
                }
            }
        };
    }

}