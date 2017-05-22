package sample.Analyser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by araujojordan on 22/05/17.
 */
public class PSEA {

    private ArrayList<HashMap<String,String>> idsStacks;

    public PSEA() {
        idsStacks = new ArrayList<>();
    }

    public void insertStack() {
        idsStacks.add(new HashMap<String, String>());
    }

    public void insertValue(String value, String type) {
        idsStacks.get(idsStacks.size()-1).put(value,type);
    }

    public void removeStack() {
        idsStacks.remove(idsStacks.size()-1);
    }

//    public String getValueType(String value) {
//        for(HashMap map:idsStacks) {
//            if(!map.containsKey(value))
//                continue;
//            return map.get(value);
//        }
//    }

}
