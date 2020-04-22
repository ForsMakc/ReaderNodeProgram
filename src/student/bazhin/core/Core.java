package student.bazhin.core;

import student.bazhin.components.Connector;
import student.bazhin.data.NodeData;

public class Core{

    protected View view;
    protected Storage storage;
    protected Connector connector;
    protected boolean working;
    protected static Core instance;

    protected Core() {}

    protected void init() {
        working = true;
        view = new View();
        storage = new Storage();
        connector = new Connector();
        connector.perform();
    }

    public static Core getInstance() {
        if (instance == null) {
            instance = new Core();
            instance.init();
        }
        return instance;
    }

    public View getView(){
        return view;
    }

    public Storage getStorage() {
        return storage;
    }

    public Connector getConnector() {
        return connector;
    }

    public String getNodeId() {
        return NodeData.getInstance().getIdentificationKey();
    }

    public void setNodeId(String nodeId) {
        NodeData.getInstance(nodeId);
    }
}
