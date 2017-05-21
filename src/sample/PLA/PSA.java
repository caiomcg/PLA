package sample.PLA;

import sample.utils.TableData;

import java.util.ArrayList;

/**
 * Created by caiomcg on 16/05/17.
 */
public class PSA implements Analyser {
    private int index;
    private ArrayList<TableData> res;

    public PSA(ArrayList<TableData> res) {
        this.index = 0;
        this.res = res;
    }

    @Override
    public ArrayList<TableData> analyze() throws RuntimeException {
        if (!validateProgram()) { // Validate program ? ;
            throw new RuntimeException("Failed at line: " + res.get(index++).getLine());
        }

        if (!recursiveAnalysis()) {
            throw new RuntimeException("Failed at line: " + res.get(index+1).toString());
        }

        return res;
    }

    private boolean recursiveAnalysis() {
        System.out.println("PRE VAR--------------");
        if (res.get(index).getToken().equals("var")) {
            index++;
            if (!validateVariableList()) {
                return false;
            }
        }

        System.out.println("PRE PROCEDURE---------------");
        if (res.get(index).getToken().equals("procedure")) {
            index++;
            if (!validateProcedure()) {
                System.out.println("NOPE");
                return false;
            }
            return recursiveAnalysis();
        }

        System.out.println("PRE BODY-----------------");
        if (res.get(index).getToken().equals("begin")) {
            index++;
            return true;
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
        System.out.println("VVL - "+ res.get(index).toString());
        if(res.get(index).getClassification().equals("Keyword"))
            return true;

        if (consumeVariables()) {
            if (res.get(index++).getClassification().equals("Keyword")) {
                if (res.get(index++).getToken().equals(";")) {
                    return validateVariableList();
                }
            } else {
                return false;
            }
        }
        return false;
    }

    private boolean consumeVariables() {
        System.out.println("Validating for " + res.get(index).getToken());
        if (res.get(index++).getClassification().equals("Identifier")) {
            if (res.get(index).getToken().equals(",")) {
                index++;
                return consumeVariables();
            } else if (res.get(index).getToken().equals(":")) {
                System.out.println("::::::::::::");
                index++;
                return true;
            }
        }
        return false;
    }

    private boolean validateProcedure() {
        System.out.println("Validating Procedure - " + res.get(index).toString());

        if (res.get(index++).getClassification().equals("Identifier")) {
            System.out.println("Found identifier");
            if (res.get(index++).getToken().equals("(")) {
                System.out.println("Found Parentheses");
                while (true) {
                    if (consumeVariables()) {
                        if (res.get(index++).getClassification().equals("Keyword")) {
                            System.out.println("IN - " + res.get(index).toString());
                            if (res.get(index).getToken().equals(";")) {
                                index++;
                                System.out.println("Continuing");
                                continue;
                            }
                            if (res.get(index).getToken().equals(")")) {
                                index++;
                                System.out.println("ON CLOSE");
                                if (res.get(index).getToken().equals(";")) {
                                    index++;
                                    System.out.println("ON ;");
                                    return true;
                                }
                                return false;
                            }
                            System.out.println("SKIPPED");
                        }
                    } else {
                        return false;
                    }
                }
            }
        }
        return true;
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
        return true;
    }
}














