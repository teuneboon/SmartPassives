package gd.leet.smartpassives.model;

import gd.leet.smartpassives.TreeJSONParser;

import java.io.IOException;

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
}
