package gd.leet.smartpassives.model;

import gd.leet.smartpassives.TreeJSONParser;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

// @TODO: PARSE FROM SkillTree.txt(POE Skill Tree)
public class ParsedSkillTree extends Tree {
    public void fill() {
        TreeJSONParser parser = new TreeJSONParser("Skilltree.json");
        try {
            parser.parse(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getLowerBound() {
        int lowest = Integer.MAX_VALUE;
        for (Map.Entry<Integer, Node> entry : this.getNodeMap().entrySet()) {
            if (entry.getKey() < lowest) {
                lowest = entry.getKey();
            }
        }
        return lowest;
    }

    public int getUpperBound() {
        int highest = Integer.MIN_VALUE;
        for (Map.Entry<Integer, Node> entry : this.getNodeMap().entrySet()) {
            if (entry.getKey() > highest) {
                highest = entry.getKey();
            }
        }
        return highest;
    }
}
