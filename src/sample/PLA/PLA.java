package sample.PLA;

import sample.utils.TableData;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by caiomcg on 24/02/2017.
 */
public class PLA {
    private int currentLineCounter;
    private String lines;

    private ArrayList<TableData> tableOfSymbols;

    private String intDigits;
    private String floatDigits;
    private String identifier;

    private ArrayList<String> keywords;
    private ArrayList<String> delimiter;
    private ArrayList<String> comparisonOperators;
    private ArrayList<String> additives;
    private ArrayList<String> multiplicatives;

    public PLA(String content) {
        this.currentLineCounter  = 1;
        this.lines               = content;

        this.tableOfSymbols      = new ArrayList<>();

        this.intDigits           = "[0-9]+";
        this.floatDigits         = "[0-9]+[.][0-9]+";
        this.identifier          = "([0-9]+|[a-z]|[A-Z]|_)+";

        this.keywords            = new ArrayList<>(Arrays.asList("program", "var", "integer", "real", "boolean", "procedure", "begin",
                    "end", "if", "then", "else", "while", "do", "not"));
        this.delimiter           = new ArrayList<>(Arrays.asList(";", ".", ":", "(", ")", ",", ":="));
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
                classification = check(split);

                if (classification.equals("unknown")) {
                    try {
                        classification = check(split.substring(0, split.length() - 1));
                        if (!classification.equals("unknown")) {
                            this.tableOfSymbols.add(new TableData(split.substring(0, split.length() - 1), classification, Integer.toString(currentLineCounter)));
                            classification = check(split.substring(split.length() - 1));
                            if (!classification.equals("unknown")) {
                                this.tableOfSymbols.add(new TableData(split.substring(split.length() - 1), classification, Integer.toString(currentLineCounter)));
                            }
                        }
                    }catch(StringIndexOutOfBoundsException exception){}
                } else {
                    this.tableOfSymbols.add(new TableData(split, classification, Integer.toString(currentLineCounter)));
                }
            }
            currentLineCounter++;
        }
        return tableOfSymbols;
    }

    private String check(String split) {

        if (this.keywords.contains(split)) {
            return "Keyword";
        } else if (this.delimiter.contains(split)) {
            return "Delimiter";
        } else if (this.comparisonOperators.contains((split))) {
            return "Comparison Operator";
        } else if (this.additives.contains(split)) {
            return "Additive Operator";
        } else if (this.multiplicatives.contains(split)) {
            return "Multiplicative Operator";
        } else if (split.matches(this.intDigits)){
            return "Integer Digit";
        } else if (split.matches(this.floatDigits)) {
            return "Floating Point Digit";
        } else if (split.matches(this.identifier)) {
            return "Identifier";
        }
        return "unknown";
    }

    private void discardComments(String line) {
        lines = line.replaceAll("\\{(?s).*?}", "");
    }

    private void removeExtraSpaces(String line) {
        lines = line.trim().replaceAll(" +", " ");
    }
}
