package gd.leet.smartpassives.model;

import java.util.Arrays;

public class TestTree extends Tree {
    public void fill() {
        Node testNode = new Node("strength 1");
        testNode.getStats().put("strength", 10);
        Node testNode2 = new Node("intelligence 1");
        testNode2.getStats().put("intelligence", 10);
        Node testNode3 = new Node("intelligence 2");
        testNode3.getStats().put("intelligence", 10);
        Node testNode4 = new Node("intelligence 3");
        testNode4.getStats().put("intelligence", 10);
        Node testNode5 = new Node("intelligence 4");
        testNode5.getStats().put("intelligence", 10);
        Node testNode6 = new Node("intelligence 5");
        testNode6.getStats().put("intelligence", 10);
        Node testNode7 = new Node("strength 2");
        testNode7.getStats().put("strength", 10);
        Node testNode8 = new Node("strength 3");
        testNode8.getStats().put("strength", 10);
        Node testNode9 = new Node("dexterity 2");
        testNode9.getStats().put("dexterity", 10);
        Node testNode10 = new Node("superint 1");
        testNode10.getStats().put("intelligence", 30);
        Node testNode11 = new Node("superstr 1");
        testNode11.getStats().put("strength", 30);

        testNode.connect(testNode3);
        testNode3.connect(testNode4);
        testNode3.connect(testNode5);
        testNode4.connect(testNode6);
        testNode5.connect(testNode7);
        testNode5.connect(testNode11);

        testNode2.connect(testNode8);
        testNode2.connect(testNode9);
        testNode2.connect(testNode10);
        testNode2.connect(testNode11);

        // ugly now cause I'm lazy
        Node[] nodes = {testNode, testNode2, testNode3, testNode4, testNode5, testNode6, testNode7, testNode8, testNode9, testNode10, testNode11};
        int i = 0;
        for (Node n : nodes) {
            this.getNodeMap().put(i++, n);
        }

        this.setStartNodesForClass("witch", Arrays.asList(testNode, testNode2));
    }
}
