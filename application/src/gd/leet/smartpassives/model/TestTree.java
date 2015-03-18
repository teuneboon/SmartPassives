package gd.leet.smartpassives.model;

import java.util.Arrays;

public class TestTree extends Tree {
    public void fill() {
        // LEFT WITCH START
        this.getNodeMap().put(0, new Node("Spell Damage"));
        this.getNodeMap().get(0).getStats().put("% increased Spell Damage", 16);
        this.getNodeMap().put(1, new Node("Spell Damage"));
        this.getNodeMap().get(1).getStats().put("% increased Spell Damage", 12);
        this.getNodeMap().put(2, new Node("Spell Damage"));
        this.getNodeMap().get(2).getStats().put("% increased Spell Damage", 10);
        this.getNodeMap().put(3, new Node("Spell Damage"));
        this.getNodeMap().get(3).getStats().put("% increased Spell Damage", 10);
        this.getNodeMap().put(4, new Node("Spell Damage"));
        this.getNodeMap().get(4).getStats().put("% increased Spell Damage", 10);
        this.getNodeMap().put(5, new Node("Cast Speed"));
        this.getNodeMap().get(5).getStats().put("% increased Cast Speed", 3);
        this.getNodeMap().put(6, new Node("Cast Speed"));
        this.getNodeMap().get(6).getStats().put("% increased Cast Speed", 3);
        this.getNodeMap().put(7, new Node("Cast Speed"));
        this.getNodeMap().get(7).getStats().put("% increased Cast Speed", 3);
        this.getNodeMap().put(8, new Node("Intelligence"));
        this.getNodeMap().get(8).getStats().put("Intelligence", 10);
        this.getNodeMap().put(9, new Node("Intelligence"));
        this.getNodeMap().get(9).getStats().put("Intelligence", 10);
        this.getNodeMap().put(10, new Node("Intelligence"));
        this.getNodeMap().get(10).getStats().put("Intelligence", 10);
        this.getNodeMap().put(11, new Node("Elemental Dominion"));
        this.getNodeMap().get(11).getStats().put("% increased Cast Speed", 4);
        this.getNodeMap().get(11).getStats().put("% increased Elemental Damage with Spells", 16);
        this.getNodeMap().get(11).getStats().put("Intelligence", 20);

        // RIGHT WITCH START
        this.getNodeMap().put(12, new Node("Energy Shield and Mana Regeneration"));
        this.getNodeMap().get(12).getStats().put("% increased Mana Regeneration Rate", 25);
        this.getNodeMap().get(12).getStats().put("+ to maximum Energy Shield", 14);
        this.getNodeMap().put(13, new Node("Intelligence"));
        this.getNodeMap().get(13).getStats().put("Intelligence", 10);
        this.getNodeMap().put(14, new Node("Intelligence"));
        this.getNodeMap().get(14).getStats().put("Intelligence", 10);
        this.getNodeMap().put(15, new Node("Intelligence"));
        this.getNodeMap().get(15).getStats().put("Intelligence", 10);
        this.getNodeMap().put(16, new Node("Energy Shield"));
        this.getNodeMap().get(16).getStats().put("% increased maximum Energy Shield", 12);
        this.getNodeMap().put(17, new Node("Energy Shield"));
        this.getNodeMap().get(17).getStats().put("% increased maximum Energy Shield", 10);
        this.getNodeMap().put(18, new Node("Energy Shield and Recovery"));
        this.getNodeMap().get(18).getStats().put("% increased maximum Energy Shield", 6);
        this.getNodeMap().get(18).getStats().put("% faster start of Energy Shield Recharge", 10);
        this.getNodeMap().put(19, new Node("Energy Shield"));
        this.getNodeMap().get(19).getStats().put("% increased maximum Energy Shield", 10);
        this.getNodeMap().put(20, new Node("Mana and Mana Regeneration"));
        this.getNodeMap().get(20).getStats().put("% increased maximum Mana", 8);
        this.getNodeMap().get(20).getStats().put("% increased Mana Regeneration Rate", 10);
        this.getNodeMap().put(21, new Node("Mana and Mana Regeneration"));
        this.getNodeMap().get(21).getStats().put("% increased maximum Mana", 8);
        this.getNodeMap().get(21).getStats().put("% increased Mana Regeneration Rate", 10);
        this.getNodeMap().put(22, new Node("Mana and Mana Regeneration"));
        this.getNodeMap().get(22).getStats().put("% increased maximum Mana", 8);
        this.getNodeMap().get(22).getStats().put("% increased Mana Regeneration Rate", 10);
        this.getNodeMap().put(23, new Node("Deep Wisdom"));
        this.getNodeMap().get(23).getStats().put("+ to maximum Mana", 20);
        this.getNodeMap().get(23).getStats().put("+ to maximum Energy Shield", 20);
        this.getNodeMap().get(23).getStats().put("Intelligence", 20);

        // WITCH STUN AVOID
        this.getNodeMap().put(24, new Node("Avoid Interruption while Casting"));
        this.getNodeMap().get(24).getStats().put("% chance to Avoid interruption from Stuns while Casting", 15);
        this.getNodeMap().put(25, new Node("Practical Application"));
        this.getNodeMap().get(25).getStats().put("% chance to Avoid interruption from Stuns while Casting", 25);
        this.getNodeMap().get(25).getStats().put("Strength", 20);
        this.getNodeMap().get(25).getStats().put("Dexterity", 20);
        this.getNodeMap().put(26, new Node("Avoid Interruption while Casting"));
        this.getNodeMap().get(26).getStats().put("% chance to Avoid interruption from Stuns while Casting", 15);

        // witch spell damage start
        this.connect(0, 1);
        // spell damage branch @ witch start
        this.connect(1, 2);
        this.connect(2, 3);
        this.connect(3, 4);
        this.connect(4, 11);
        // cast speed branch @ witch start
        this.connect(1, 5);
        this.connect(5, 6);
        this.connect(6, 7);
        this.connect(7, 11);
        // left int branch @ witch start
        this.connect(0, 8);
        this.connect(8, 9);
        this.connect(9, 10);
        this.connect(10, 11);

        // witch es start
        this.connect(12, 16);
        // energy shield branch @ witch start
        this.connect(16, 17);
        this.connect(17, 18);
        this.connect(18, 19);
        this.connect(19, 23);
        // mana regen branch @ witch start
        this.connect(16, 20);
        this.connect(20, 21);
        this.connect(21, 22);
        this.connect(22, 23);
        // right int branch @ witch start
        this.connect(12, 13);
        this.connect(13, 14);
        this.connect(14, 15);
        this.connect(15, 23);

        // witch stun avoidance
        this.connect(11, 24);
        this.connect(24, 25);
        this.connect(25, 26);
        this.connect(26, 23);


        this.setStartNodesForClass("witch", Arrays.asList(this.getNodeMap().get(0), this.getNodeMap().get(12)));
    }
}
