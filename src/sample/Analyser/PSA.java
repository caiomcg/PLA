package sample.Analyser;

import sample.utils.TableData;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by caiomcg on 16/05/17.
 */
public class PSA implements Analyser {
    private int index;
    private int loops;
    private ArrayList<TableData> res;
    private PSEA semantic;

    public PSA(ArrayList<TableData> res) {
        this.index = 0;
        this.res = res;
        this.semantic = new PSEA();
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
            semantic.newProcedureScope();
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
                semantic.leftProcedureScope();
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
            semantic.removeStack();
            System.out.println("Found END");
            moveStackReference();
            return true;
        }

        if (res.get(index).getClassification().equals("Identifier")) {
            System.out.println("Found Identifier");
            moveStackReference();

            if (res.get(index).getToken().equals(":=")) {
                if (!semantic.isTypeValid(res.get(index - 1))) {
                    index--;
                    return false;
                }
                System.out.println("Found equals");
                moveStackReference();
                if (!validateExpression()) {
                    return false;
                }
                return validateBody();
            }

            if (res.get(index).getToken().equals("(")) {
                String procedure = res.get(index-1).getToken();
                moveStackReference();
                if (!consumeParams()) {
                    return false;
                }
                if (res.get(index).getToken().equals(";")) {
                    moveStackReference();
                    if (!semantic.procedureIsVisible(procedure)) {
                        return false;
                    }
                    return validateBody();
                }
            }
        }

        if (res.get(index).getToken().equals("else")) {
            if (!hasIf)
                return false;
            moveStackReference();
            return validateBody();
        }

        if (res.get(index).getToken().equals("if") || res.get(index).getToken().equals("while")) {

            if (res.get(index).getToken().equals("if")) {
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
                    semantic.insertStack();
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
                semantic.tempToStack(res.get(index).getToken());
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
            semantic.insertTempData(res.get(index).getToken());
            moveStackReference();
            if (res.get(index).getToken().equals(",")) {
                moveStackReference();
                return consumeVariables();
            } else if (res.get(index).getToken().equals(":") ||
                    res.get(index).getToken().equals(")")) {
                System.out.println("::::::::::::");
                moveStackReference();
                return true;
            }
        }
        if (res.get(index).getToken().equals(")")) {
            moveStackReference();
            return true;
        }
        return false;
    }

    private boolean consumeParams() {
        System.out.println("Validating Params " + res.get(index).getToken());
        if (res.get(index).getClassification().equals("Identifier") || isValue()) {
            moveStackReference();
            if (res.get(index).getToken().equals(",")) {
                moveStackReference();
                return consumeParams();
            } else if (res.get(index).getToken().equals(")")) {
                System.out.println("::::::::::::");
                moveStackReference();
                return true;
            }
        }
        if (res.get(index).getToken().equals(")") && res.get(index - 1).getToken().equals("(")) {
            moveStackReference();
            return true;
        }
        return false;
    }

    private boolean validateProcedure() {
        System.out.println("Validating Procedure - " + res.get(index).toString());

        if (res.get(index).getClassification().equals("Identifier")) {
            semantic.insertProcedure(res.get(index).getToken());
            moveStackReference();
            System.out.println("Found identifier");
            if (res.get(index).getToken().equals("(")) {
                semantic.insertStack();
                moveStackReference();
                System.out.println("Found Parentheses");
                while (true) {

                    System.out.println("-------------------");
                    System.out.println("PROCEDURE CLOSE ) " + res.get(index).toString());

                    if (res.get(index).getToken().equals(")")) {
                        moveStackReference();
                        System.out.println("Consume: " + res.get(index).toString());

                        if(res.get(index).getToken().equals(";")) {
                            System.out.println("OK");
                            moveStackReference();
                            return true;
                        }
                        return false;
                    }
                    System.out.println("-------------------");


                    if (consumeVariables()) {
                        if (res.get(index).getClassification().equals("Keyword")) {
                            moveStackReference();
                            System.out.println("IN - " + res.get(index).toString());
                            if (res.get(index).getToken().equals(";")) {
                                moveStackReference();
                                System.out.println("Continuing");
                                continue;
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
                res.get(index).getClassification().equals("Floating Point Digit");
    }

    private boolean isOperator() {
        return res.get(index).getClassification().equals("Additive Operator") ||
                res.get(index).getClassification().equals("Multiplicative Operator");
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
            if (!semantic.isTypeValid(res.get(index))) {
                return false;
            }
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
                semantic.cleanInitialValue();
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

    public boolean boolNumberExpr(boolean firstExp) {

        ArrayList comparisonOperators = new ArrayList<>(Arrays.asList("=", "<", ">", "<=", ">=", "<>"));

        if (res.get(index).getToken().equals("(")) {
            parenthesis++;
            moveStackReference();
            return validateExpression();
        }

        //numero ou real
        if ((semantic.convertType(res.get(index))).equals("integer") ||
                (semantic.convertType(res.get(index))).equals("real")) {
            if (!semantic.isTypeValid(res.get(index))) {
                return false;
            }
            moveStackReference();

            while (res.get(index).getToken().equals(")")) {
                if (parenthesis < 1)
                    return false;
                parenthesis--;
                moveStackReference();
            }

            if (comparisonOperators.contains(res.get(index).getToken())) {
                if (!firstExp)
                    return false;
                moveStackReference();
                return true;
            }

            //PEGA OPERADOR + - * /
            if (isOperator()) {
                moveStackReference();
                return boolNumberExpr(firstExp);
            }

            //SEGUNDA EXP E NO FINAL
            if (!firstExp &&
                    (res.get(index).getToken().equals("then") || res.get(index).getToken().equals("do"))) {
                moveStackReference();
                return true;
            }
        }
        return false;
    }

    public boolean boolBooleanExpr(boolean firstExp) {

        ArrayList comparisonOperators = new ArrayList<>(Arrays.asList("=", "<", ">", "<=", ">=", "<>"));
        ArrayList andOr = new ArrayList<>(Arrays.asList("or", "and"));

        if (res.get(index).getToken().equals("(")) {
            parenthesis++;
            moveStackReference();
            return validateExpression();
        }

        //Var booleana, true ou false
        if ((semantic.convertType(res.get(index))).equals("boolean")) {
            if (!semantic.isTypeValid(res.get(index))) {
                return false;
            }
            moveStackReference();

            while (res.get(index).getToken().equals(")")) {
                if (parenthesis < 1)
                    return false;
                parenthesis--;
                moveStackReference();
            }

            //== > < <>...
            if (comparisonOperators.contains(res.get(index).getToken())) {
                if (!firstExp)
                    return false;
                moveStackReference();
                return true;
            }

            // AND e OR
            if (andOr.contains(res.get(index).getToken())) {
                moveStackReference();
                return boolBooleanExpr(firstExp);
            }

            //SEGUNDA EXPR e NO FINAL
            if (!firstExp &&
                    (res.get(index).getToken().equals("then") || res.get(index).getToken().equals("do"))) {
                moveStackReference();
                return true;
            }
        }
        return false;
    }


    public boolean checkBoolean() {
        int stackReference = index;
        parenthesis = 0;

        semantic.cleanInitialValue();
        if (boolNumberExpr(true)) {
            semantic.cleanInitialValue();
            if (boolNumberExpr(false)) {
                semantic.cleanInitialValue();
                return true;
            }
        } else {
            //INDEX recupera valor inicial pois primeira exp nao eh numerica
            index = stackReference;
            if (boolBooleanExpr(true)) {
                semantic.cleanInitialValue();
                if (boolBooleanExpr(false)) {
                    semantic.cleanInitialValue();
                    return true;
                }
            }
        }
        return false;
    }
}














