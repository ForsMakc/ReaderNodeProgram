package student.bazhin.data;

import java.util.HashMap;

public class NodeData {
    private static NodeData instance;
    protected String identificationKey;
    protected HashMap<String,String> nodeData;

    protected NodeData(String identificationKey) {
        this.identificationKey = identificationKey;
    }

    public static NodeData getInstance() {
        try {
            if (instance == null) {
                throw new NullPointerException("Key isn`t set");
            }
        } catch (NullPointerException e) {
            System.out.println("Ключ не установлен! Чтобы получить экземпляр класса, введите ключ в аргументе");
        }
        return instance;
    }

    public static NodeData getInstance(String identificationKey) {
        if (instance == null) {
            instance = new NodeData(identificationKey);
        }
        return instance;
    }

    public String getIdentificationKey() {
        return identificationKey;
    }

    public void setNodeData(HashMap<String, String> nodeData) {
        this.nodeData = nodeData;
    }

    public HashMap<String, String> getNodeData() {
        return nodeData;
    }

    public void setData(String key, String data) {
        nodeData.put(key,data);
    }

    public String getData(String key) {
        return nodeData.get(key);
    }
}
