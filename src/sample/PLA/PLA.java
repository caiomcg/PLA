package sample.PLA;

import sample.utils.TableData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Pattern;

/**
 * Created by caiomcg on 24/02/2017.
 */
public class PLA {
    private int currentLineCounter;
    private String lines;

    private ArrayList<TableData> tableOfSymbols;

    private String attribution;

    private Pattern intDigits;
    private Pattern floatDigits;

    private ArrayList<String> keywords;
    private ArrayList<String> delimiter;
    private ArrayList<String> comparisonOperators;
    private ArrayList<String> additives;
    private ArrayList<String> multiplicatives;

    public PLA(String content) {
        this.currentLineCounter  = 1;
        this.lines               = content;

        this.tableOfSymbols      = new ArrayList<>();

        this.attribution         = ":=";

        this.intDigits           = Pattern.compile("[0-9]+");
        this.floatDigits         = Pattern.compile("[0-9]+[.][0-9]+");

        this.keywords            = new ArrayList<>(Arrays.asList("program", "var", "integer", "real", "boolean", "procedure", "begin",
                    "end", "if", "then", "else", "while", "do", "not"));
        this.delimiter           = new ArrayList<>(Arrays.asList(";", ".", ":", "(", ")", ","));
        this.comparisonOperators = new ArrayList<>(Arrays.asList("=", "<", ">", "<=", ">=", "<>"));
        this.additives            = new ArrayList<>(Arrays.asList("+", "-", "or"));
        this.multiplicatives     = new ArrayList<>(Arrays.asList("*", "/", "and"));
    }

    public ArrayList<TableData> analyze() {
        String classification;
        discardComments(lines);
        removeExtraSpaces(lines);

        for (String line : lines.split("\\n")) {
            System.out.println("Testing line - " + currentLineCounter + " - " + line);
            for (String split : line.split("\\s+")) { //ASSUMING A SPACE IS USED TO DELIMIT TOKENS
                if (this.keywords.contains(split)) {
                    classification = "Keyword";
                } else if (this.delimiter.contains(split)) {
                    classification = "Delimiter";
                } else if (this.comparisonOperators.contains((split))) {
                    classification = "Comparison Operator";
                } else if (this.additives.contains(split)) {
                    classification = "Additive Operator";
                } else if (this.multiplicatives.contains(split)) {
                    classification = "Multiplicative Operator";
                } else if (split.equals("")) {

                }
                this.tableOfSymbols.add(new TableData(split, "Keyword", Integer.toString(currentLineCounter)));
            }
            currentLineCounter++;
        }

        return tableOfSymbols;
    }

    private void discardComments(String line) {
        lines = line.replaceAll("\\{(?s).*?}", "");
    }

    private void removeExtraSpaces(String line) {
        lines = line.trim().replaceAll(" +", " ");
    }
}
