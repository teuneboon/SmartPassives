package gd.leet.smartpassives;

import gd.leet.smartpassives.model.Node;
import gd.leet.smartpassives.model.Tree;
import org.json.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TreeJSONParser {
    private String input_file;

    public TreeJSONParser(String input_file) {
        this.input_file = input_file;
    }

    private String get_file_contents() throws IOException {
        Path path = Paths.get(this.input_file);
        return Files.readAllLines(path).get(0);
    }

    public void parse(Tree tree) throws IOException {
        Pattern statPattern = Pattern.compile("(\\D*)(\\d+)(\\D*)"); // find a number in a text

        HashMap<Integer, Integer> connections = new HashMap<Integer, Integer>();

        JSONObject obj = new JSONObject(this.get_file_contents());
        JSONArray nodes = obj.getJSONArray("nodes");
        for (int i = 0; i < nodes.length(); ++i) {
            JSONObject node = nodes.getJSONObject(i);
            tree.getNodeMap().put(node.getInt("id"), new Node(node.getString("dn")));

            JSONArray stats = node.getJSONArray("sd");
            for (int k = 0; k < stats.length(); ++k) {
                String stat = stats.getString(k);
                Matcher m = statPattern.matcher(stat);
                if (m.find()) {
                    tree.getNodeMap().get(node.getInt("id")).getStats().put(m.group(1) + m.group(3), Integer.parseInt(m.group(2)));
                }
            }

            JSONArray outs = node.getJSONArray("out");

            for (int j = 0; j < outs.length(); ++j) {
                connections.put(node.getInt("id"), outs.getInt(j));
            }
        }

        for (Map.Entry<Integer, Integer> entry : connections.entrySet()) {
            tree.connect(entry.getKey(), entry.getValue());
        }

        tree.setStartNodesForClass("witch", Arrays.asList(tree.getNodeMap().get(57226), tree.getNodeMap().get(57264)));
    }
}
