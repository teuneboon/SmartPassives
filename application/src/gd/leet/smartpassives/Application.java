package gd.leet.smartpassives;

import gd.leet.smartpassives.model.TestTree;
import gd.leet.smartpassives.model.Tree;

public class Application {
    public int CHROMOSOME_LENGTH = 120;
    public int POPULATION_SIZE = 200;
    public double BASE_MUTATION_RATE = 5;
    int STAGNATION_LIMIT_MIN = 50;

    public void run() throws Exception {
        Tree test = new TestTree();
        test.fill();
        System.out.println(test.getStartNodesForClass("witch").get(0));
    }

    public static void main(String[] args) {
        try {
            new Application().run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}