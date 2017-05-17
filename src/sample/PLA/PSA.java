package sample.PLA;

import sample.utils.TableData;

import java.util.ArrayList;

/**
 * Created by caiomcg on 16/05/17.
 */
public class PSA {
    private int index;
    private ArrayList<TableData> res;

    public PSA(ArrayList<TableData> res) {
        this.index = 0;
        this.res = res;
    }

    public ArrayList<TableData> analyze() throws RuntimeException {
        if (!validateProgram()) { // Validate program ? ;
            throw new RuntimeException("Failed at line: " + res.get(index).getLine());
        }

        if (!recursiveAnalysis()) {
            throw new RuntimeException("Failed at line: " + res.get(index).getLine());
        }

        return res;
    }

    private boolean recursiveAnalysis() {
        if (res.get(index).getToken().equals("var")) {
            index++;
            boolean x = validateVariableList();
        }

        if (res.get(index).getToken().equals("procedure")) {
            index++;
            return recursiveAnalysis();
        }

        if (res.get(index).getToken().equals("begin")) {
            index++;
            return validateComposite();
        }
        return false;
    }

    private boolean validateProgram() {
        if (res.get(index++).getToken().equals("program")) {
            System.out.println("Found program");
            if (res.get(index++).getClassification().equals("Identifier")) {
                System.out.println("Found identifier");
                if (res.get(index++).getToken().equals(";")) {
                    System.out.println("Found ;");
                    return true;
                }
            }
        }
        return false;
    }

    private boolean validateVariableList() {
        if(!res.get(index+1).getClassification().equals("Identifier"))
            return true;
        if (res.get(index++).getClassification().equals("Identifier")) {
            if(res.get(index++).getToken().equals(":")) {
                if(res.get(index++).getClassification().equals("Keyword"))
                    return validateVariableList();
            }
        }
        return false;
    }

    private boolean validateComposite() {
        if (res.get(index++).getToken().equals("End")) {
            return true;
        }

        if (res.get(index).getClassification().equals("Identifier")) {
            index++;
            if (res.get(index).getToken().equals(":=")) {
                index++;
               return validateExpression();
            }
        }
        return true;
    }

    private boolean validateExpression() {

        ArrayList<TableData> expressionList = new ArrayList<>();
        for(int i = index; !res.get(i).getToken().equals(";");i++) {
            expressionList.add(res.get(i));
        }





        if (res.get(index).getClassification().equals("Integer Digit") ||
                res.get(index).getClassification().equals("Floating Point Digit") ||
                res.get(index).getClassification().equals("Identifier")) {
            index++;


            if(res.get(index).getClassification().equals("Additive Operator") ||
                    res.get(index).getClassification().equals("Multiplicative Operator")) {
                return validateExpression();
            }
        }
    }
}














