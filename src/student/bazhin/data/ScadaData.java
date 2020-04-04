package student.bazhin.data;

import student.bazhin.interfaces.IData;

public class ScadaData implements IData {

    protected String scadaData;
    protected String nl = System.getProperty("line.separator");

    public ScadaData(String scadaData) {
        this.scadaData = scadaData + nl;
    }

    public ScadaData add(String s) {
        scadaData = scadaData + s + nl;
        return this;
    }

    public ScadaData clear() {
        scadaData = "";
        return this;
    }

    @Override
    public String toString() {
        return scadaData;
    }
}
