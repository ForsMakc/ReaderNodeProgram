package student.bazhin.data;

import student.bazhin.interfaces.IData;

import java.util.ArrayList;
import java.util.Collections;

public class ScadaData implements IData {

    public ArrayList<String> scadaDataList;

    public ScadaData(String... data){
        scadaDataList = new ArrayList<>();
        Collections.addAll(scadaDataList, data);
    }

}
