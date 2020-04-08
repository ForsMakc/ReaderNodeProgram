package student.bazhin.data;

import student.bazhin.interfaces.IData;

public class PocketData implements IData {

    protected String scadaData;
    protected String nl = System.getProperty("line.separator");

    public PocketData(String scadaData) {
        this.scadaData = scadaData + nl;
    }

    public PocketData add(String s) {
        scadaData = scadaData + s + nl;
        return this;
    }

    public PocketData clear() {
        scadaData = "";
        return this;
    }

    @Override
    public String toString() {
        return scadaData;
    }
}
