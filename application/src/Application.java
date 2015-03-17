import org.jgap.*;
import org.jgap.impl.DefaultConfiguration;
import org.jgap.impl.IntegerGene;

public class Application {
    public void run() throws Exception {
        Configuration conf = new DefaultConfiguration();

        int targetAmount = 99;
        FitnessFunction myFunc = new MinimizingMakeChangeFitnessFunction( targetAmount );

        conf.setFitnessFunction( myFunc );

        // Now we need to tell the Configuration object how we want our
        // Chromosomes to be setup. We do that by actually creating a
        // sample Chromosome and then setting it on the Configuration
        // object. As mentioned earlier, we want our Chromosomes to
        // each have four genes, one for each of the coin types. We
        // want the values of those genes to be integers, which represent
        // how many coins of that type we have. We therefore use the
        // IntegerGene class to represent each of the genes. That class
        // also lets us specify a lower and upper bound, which we set
        // to sensible values for each coin type.
        // --------------------------------------------------------------
        Gene[] sampleGenes = new Gene[ 4 ];

        sampleGenes[0] = new IntegerGene(conf, 0, 3 );  // Quarters
        sampleGenes[1] = new IntegerGene(conf, 0, 2 );  // Dimes
        sampleGenes[2] = new IntegerGene(conf, 0, 1 );  // Nickels
        sampleGenes[3] = new IntegerGene(conf, 0, 4 );  // Pennies

        Chromosome sampleChromosome = new Chromosome(conf, sampleGenes );

        conf.setSampleChromosome( sampleChromosome );

        // Finally, we need to tell the Configuration object how many
        // Chromosomes we want in our population. The more Chromosomes,
        // the larger the number of potential solutions (which is good
        // for finding the answer), but the longer it will take to evolve
        // the population each round. We'll set the population size to
        // 500 here.
        // --------------------------------------------------------------
        conf.setPopulationSize( 500 );

        Genotype population = Genotype.randomInitialGenotype(conf);
        IChromosome bestSolutionSoFar;

        for( int i = 0; i < 100; i++ )
        {
            population.evolve();
        }
        bestSolutionSoFar = population.getFittestChromosome();

        System.out.println( "The best solution contained the following: " );

        System.out.println(MinimizingMakeChangeFitnessFunction.getNumberOfCoinsAtGene(bestSolutionSoFar, 0) + " quarters.");

        System.out.println(MinimizingMakeChangeFitnessFunction.getNumberOfCoinsAtGene(bestSolutionSoFar, 1) + " dimes.");

        System.out.println(MinimizingMakeChangeFitnessFunction.getNumberOfCoinsAtGene(bestSolutionSoFar, 2) + " nickels.");

        System.out.println(MinimizingMakeChangeFitnessFunction.getNumberOfCoinsAtGene(bestSolutionSoFar, 3) + " pennies.");

        System.out.println("For a total of " + MinimizingMakeChangeFitnessFunction.amountOfChange(bestSolutionSoFar) + " cents in " + MinimizingMakeChangeFitnessFunction.getTotalNumberOfCoins(bestSolutionSoFar) + " coins." );
    }

    public static void main(String[] args) {
        try {
            new Application().run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}