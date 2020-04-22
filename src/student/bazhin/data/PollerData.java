package student.bazhin.data;

import student.bazhin.interfaces.IData;

import java.util.HashMap;

public class PollerData implements IData {

    HashMap<String,Object> pollerData;

    public PollerData(){
        pollerData = new HashMap<>();
    }

    public void put(String key, Object value){
        pollerData.put(key,value);
    }

    public Object get(String key){
        return pollerData.get(key);
    }
}
