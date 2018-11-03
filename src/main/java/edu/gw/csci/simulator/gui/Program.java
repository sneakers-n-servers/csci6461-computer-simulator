package edu.gw.csci.simulator.gui;

import java.util.ArrayList;
import java.util.List;

public class Program {

    private List<String> lines;
    private final String name;

    public Program(String name) {
        this.name = name;
        lines = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void appendLine(String line) {
        lines.add(line);
    }

    public void clearContents() {
        lines.clear();
    }

    public List<String> getLines() {
        List<String> copy = new ArrayList<>(lines);
        return copy;
    }

    public int size() {
        return lines.size();
    }
}
