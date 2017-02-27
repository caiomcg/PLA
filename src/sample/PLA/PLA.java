package sample.PLA;

import sample.Utils.Tuple;

import java.util.ArrayList;
import java.util.regex.Pattern;

/**
 * Created by caiomcg on 24/02/2017.
 */
public class PLA {
    private Pattern intDigits;
    private Pattern floatDigits;

    private int currentLineCounter;
    private ArrayList<Tuple<String, String, String>> tableOfSymbols;
    private String lines;

    public PLA(String content) {
        this.intDigits          = Pattern.compile("[0-9]+");
        this.floatDigits        = Pattern.compile("[0-9]+[.][0-9]+");

        this.currentLineCounter = 0;
        this.tableOfSymbols     = new ArrayList<>();
        this.lines              = content;
    }

    public ArrayList<Tuple<String, String, String>> analyze() {
        discardComments(lines);
        for (String line : lines.split("\\n")) {
            System.out.println("Testing line - " + (currentLineCounter+1) + " - " + line);
            currentLineCounter++;
        }
        return tableOfSymbols;
    }

    private void discardComments(String line) {
        lines = line.replaceAll("\\{(?s).*?}", "");
    }

}
