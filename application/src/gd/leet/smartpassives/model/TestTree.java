package gd.leet.smartpassives.model;

import java.util.Arrays;

public class TestTree extends Tree {
    public void fill() {
        Node testNode = new Node();
        testNode.getStats().put("strength", 10);
        Node testNode2 = new Node();
        testNode2.getStats().put("intelligence", 10);
        Node testNode3 = new Node();
        testNode3.getStats().put("intelligence", 10);
        Node testNode4 = new Node();
        testNode4.getStats().put("intelligence", 10);
        Node testNode5 = new Node();
        testNode5.getStats().put("intelligence", 10);
        Node testNode6 = new Node();
        testNode6.getStats().put("intelligence", 10);
        Node testNode7 = new Node();
        testNode7.getStats().put("strength", 10);
        Node testNode8 = new Node();
        testNode8.getStats().put("strength", 10);
        Node testNode9 = new Node();
        testNode9.getStats().put("dexterity", 10);
        Node testNode10 = new Node();
        testNode10.getStats().put("intelligence", 30);
        Node testNode11 = new Node();
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

        this.setStartNodesForClass("witch", Arrays.asList(testNode, testNode2));
    }
}
