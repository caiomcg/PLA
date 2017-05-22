package sample.Analyser;

import sample.utils.TableData;

import java.lang.reflect.Array;
import java.util.*;

/**
 * Created by araujojordan on 22/05/17.
 */
public class PSEA {

    private ArrayList<HashMap<String,String>> idsStacks;
    private ArrayList<String> tempData;
    private TableData initialValue;

    private static final String integer  = "[0-9]+";
    private static final String floating = "[0-9]+[.][0-9]+";
    private  ArrayList<String> booleanType;

    public PSEA() {
        tempData = new ArrayList<>();
        idsStacks = new ArrayList<>();
        booleanType = new  ArrayList<>(Arrays.asList("true", "false"));
    }

    public void insertStack() {
        idsStacks.add(new HashMap<String, String>());
    }

    public boolean isOnStack(String value) {
        return idsStacks.get(idsStacks.size()-1).containsKey(value);
    }

    public String stackValue(String value) {
        for (int i = idsStacks.size() - 1; i >= 0 ; i--) {
            System.out.println("MAP ON LOOP");
            printMap(idsStacks.get(i));
            if (idsStacks.get(i).containsKey(value)) {
                return idsStacks.get(i).get(value);
            }
        }
        throw new RuntimeException("Variable " + value + " does not appear to be on scope.");
    }

    public void insertValue(String value, String type) {
        if (isOnStack(value)) {
            throw new RuntimeException("Duplicated value on scope - " + value);
        }

        idsStacks.get(idsStacks.size()-1).put(value,type);
    }

    public void insertTempData(String value) {
        tempData.add(value);
    }

    public void tempToStack(String type) {
        for(String variable : tempData) {
            insertValue(variable, type);
        }
        tempData.clear();
    }

    public void removeStack() {
        idsStacks.remove(idsStacks.size()-1);
    }

    private void printMap(Map mp) {
        Iterator it = mp.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            System.out.println(pair.getKey() + " = " + pair.getValue());
        }
    }

    public boolean isTypeValid(TableData data) {
        if(initialValue==null) {
            initialValue = data;
            return true;
        }

        String initial = convertType(initialValue);
        String newData = convertType(data);

        System.out.println("VERYFING DATA TYPES: " + initial + " op " + newData);

        if (initial.equals("integer")) {
            if (!newData.equals("integer")) {
                throw new RuntimeException("Invalid data type: Cannot apply " + initial + " to " + newData);
            }
        }
        if (initial.equals("real")) {
            if (newData.equals("real") || newData.equals("integer")) {
                return true;
            }
            throw new RuntimeException("Invalid data type: Cannot apply " + initial + " to " + newData);
        }

        return initial.equals(newData);
    }

    public void cleanInitialValue() {
        initialValue = null;
    }

    private String convertType(TableData data) {
        if (data.getToken().matches(integer)) {
            return "integer";
        }

        if (data.getToken().matches(floating)) {
            return "real";
        }

        if (booleanType.contains(data.getToken())) {
            return "boolean";
        }

        System.out.println("HERRRREEE");

        return stackValue(data.getToken());
    }
}
