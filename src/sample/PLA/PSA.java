package sample.PLA;

import sample.utils.TableData;

import java.util.ArrayList;

/**
 * Created by caiomcg on 16/05/17.
 */
public class PSA implements Analyser {
    private int index;
    private int loops;
    private ArrayList<TableData> res;

    public PSA(ArrayList<TableData> res) {
        this.index = 0;
        this.res = res;
    }

    @Override
    public ArrayList<TableData> analyze() throws RuntimeException {
        if (!validateProgram()) { // Validate program ? ;
            throw new RuntimeException("Failed at line: " + res.get(index).getLine() + " - bad token: " + res.get(index).getToken());
        }

        if (!recursiveAnalysis()) {
            throw new RuntimeException("Failed at line: " + res.get(index).getLine() + " - bad token: " + res.get(index).getToken());
        }

        return res;
    }

    boolean localProcedure = false;

    private boolean recursiveAnalysis() {
        System.out.println("PRE VAR--------------");
        if (res.get(index).getToken().equals("var")) {
            moveStackReference();
            loops = 0;
            if (!validateVariableList()) {
                return false;
            }
        }

        System.out.println("PRE PROCEDURE---------------");
        if (res.get(index).getToken().equals("procedure")) {
            localProcedure = true;
            moveStackReference();
            if (!validateProcedure()) {
                System.out.println("NOPE");
                return false;
            }
            return recursiveAnalysis();
        }

        System.out.println("PRE BODY-----------------");
        if (res.get(index).getToken().equals("begin")) {
            System.out.println("Found BEGIN AT BODY");
            moveStackReference();
            hasIf = false;
            if (!validateBody()) {
                System.out.println("DEU AGUIA");
                return false;
            } else if (res.get(index).getToken().equals(";")) {
                moveStackReference();
                localProcedure = false;
                return recursiveAnalysis(); //It is the end of a procedure
            } else if (res.get(index).getToken().equals(".")) {
                if (localProcedure)
                    return false;
                return true; //The end of the program
            }
        }
        System.out.println("LEAVING");
        return false;
    }

    boolean hasIf = false;

    private boolean validateBody() {
        System.out.println("VALIDATING BODY " + res.get(index).toString());
        if (res.get(index).getToken().equals("end")) {
            System.out.println("Found END");
            moveStackReference();
            return true;
        }

        if (res.get(index).getClassification().equals("Identifier")) {
            System.out.println("Found Identifier");
            moveStackReference();
            if (res.get(index).getToken().equals(":=")) {
                System.out.println("Found equals");
                moveStackReference();
                if (!validateExpression()) {
                    return false;
                }
                return validateBody();
            }
        }

        if (res.get(index).getToken().equals("else")) {
            if(!hasIf)
                return false;
            moveStackReference();
            return validateBody();
        }

        if (res.get(index).getToken().equals("if") || res.get(index).getToken().equals("while")) {

            if(res.get(index).getToken().equals("if")) {
                hasIf = true;
            }

            System.out.println("Found if or while");
            moveStackReference();
            if (!validateExpression()) {
                return false;
            }
            System.out.println("After validating expression");
            System.out.println("------" + res.get(index).toString());

            if (res.get(index).getToken().equals("begin")) {
                moveStackReference();
                if (!validateBody()) {
                    return false;
                }
                System.out.println("HERE");
                if (res.get(index).getToken().equals(";")) {
                    moveStackReference();
                    return validateBody();
                }
            }
            return validateBody();
        }

        return false; //RETURN FALSE!!
    }

    private boolean validateProgram() {
        if (res.get(index).getToken().equals("program")) {
            moveStackReference();
            System.out.println("Found program");
            if (res.get(index).getClassification().equals("Identifier")) {
                moveStackReference();
                System.out.println("Found identifier");
                if (res.get(index).getToken().equals(";")) {
                    moveStackReference();
                    System.out.println("Found ;");
                    return true;
                }
            }
        }
        return false;
    }

    private boolean validateVariableList() {
        System.out.println("VVL - " + res.get(index).toString());

        if (res.get(index).getClassification().equals("Keyword")) {
            return loops != 0;
        }


        if (consumeVariables()) {
            if (res.get(index).getClassification().equals("Keyword")) {
                moveStackReference();
                if (res.get(index).getToken().equals(";")) {
                    moveStackReference();
                    loops++;
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
        if (res.get(index).getClassification().equals("Identifier")) {
            moveStackReference();
            if (res.get(index).getToken().equals(",")) {
                moveStackReference();
                return consumeVariables();
            } else if (res.get(index).getToken().equals(":")) {
                System.out.println("::::::::::::");
                moveStackReference();
                return true;
            }
        }
        return false;
    }

    private boolean validateProcedure() {
        System.out.println("Validating Procedure - " + res.get(index).toString());

        if (res.get(index).getClassification().equals("Identifier")) {
            moveStackReference();
            System.out.println("Found identifier");
            if (res.get(index).getToken().equals("(")) {
                moveStackReference();
                System.out.println("Found Parentheses");
                while (true) {
                    if (consumeVariables()) {
                        if (res.get(index).getClassification().equals("Keyword")) {
                            moveStackReference();
                            System.out.println("IN - " + res.get(index).toString());
                            if (res.get(index).getToken().equals(";")) {
                                moveStackReference();
                                System.out.println("Continuing");
                                continue;
                            }
                            if (res.get(index).getToken().equals(")")) {
                                moveStackReference();
                                System.out.println("ON CLOSE");
                                if (res.get(index).getToken().equals(";")) {
                                    moveStackReference();
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

    private void moveStackReference() {
        index++;
    }


    private boolean isValue() {
        System.out.println("Inside is value");
        System.out.println(res.get(index).toString());
        return res.get(index).getClassification().equals("Identifier") ||
                res.get(index).getClassification().equals("Integer Digit") ||
                res.get(index).getClassification().equals("Floating Point Digit") ||
                res.get(index).getToken().equals("true") ||
                res.get(index).getToken().equals("false");
    }

    private boolean isOperator() {
        return res.get(index).getClassification().equals("Additive Operator") ||
                res.get(index).getClassification().equals("Multiplicative Operator") ||
                res.get(index).getClassification().equals("Comparison Operator") ||
                res.get(index).getToken().equals("and") ||
                res.get(index).getToken().equals("or");
    }

    int parenthesis = 0;

    private boolean validateExpression() {
        //possui parentesis?
        if (res.get(index).getToken().equals("(")) {
            parenthesis++;
            moveStackReference();
            return validateExpression();
        }

        //Checa se tem var/num/booleana
        if (isValue()) {
            System.out.println("Found identifier - EXP");
            moveStackReference();

            //fecha parentesis?
            while (res.get(index).getToken().equals(")")) {
                if (parenthesis < 1)
                    return false;
                parenthesis--;
                moveStackReference();
            }

            //checa se chegou ao fim da expressao
            System.out.println("END OF EXP " + res.get(index).toString());
            if ((res.get(index).getToken().equals("then") ||
                    res.get(index).getToken().equals(";") ||
                    res.get(index).getToken().equals("do") &&
                            parenthesis == 0)) {
                moveStackReference();
                System.out.println("END OF EXP2 " + res.get(index).toString());
                return true;
            }

            if (isOperator()) {
                moveStackReference();
                return validateExpression();
            }
        }
        return false;
    }

}














