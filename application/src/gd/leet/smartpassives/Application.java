package gd.leet.smartpassives;

import gd.leet.smartpassives.model.TestTree;
import gd.leet.smartpassives.model.Tree;

public class Application {
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