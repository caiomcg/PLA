package sample.utils;

import jdk.nashorn.internal.parser.Token;

/**
 * Created by caiomcg on 27/02/2017.
 */
public class TableData {
    private String token;
    private String classification;
    private String line;

    public TableData(String token, String classification, String line) {
        this.token = token;
        this.classification = classification;
        this.line = line;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getClassification() {
        return classification;
    }

    public void setClassification(String classification) {
        this.classification = classification;
    }

    public String getLine() {
        return line;
    }

    public void setLine(String line) {
        this.line = line;
    }

    @Override
    public String toString() {
        return "" + token + " - " + classification + "["+ line + "]";
    }
}
