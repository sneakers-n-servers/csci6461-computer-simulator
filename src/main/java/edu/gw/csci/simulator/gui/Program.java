package edu.gw.csci.simulator.gui;

import java.util.ArrayList;

public class Program {

    private ArrayList<String> lines;
    private final String name;

    public Program(String name){
        this.name = name;
        lines = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void appendLine(String line){
        lines.add(line);
    }

    public void clearContents(){
        lines.clear();
    }

    public ArrayList<String> getLines(){
        return lines;
    }
}
