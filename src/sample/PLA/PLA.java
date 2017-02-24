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
    private ArrayList<String> lines;

    public PLA(String content) {
        this.intDigits          = Pattern.compile("[0-9]+");
        this.floatDigits        = Pattern.compile("[0-9]+[.][0-9]+");

        this.currentLineCounter = 0;
        this.tableOfSymbols     = new ArrayList<>();
        this.lines              = new ArrayList<>();

        for (String line : content.split("\\n")) lines.add(line);
    }

    public ArrayList<Tuple<String, String, String>> analyze() {
        for (String line : lines) {

        }
        return tableOfSymbols;
    }
}
