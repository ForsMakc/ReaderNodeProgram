package student.bazhin.data;

import student.bazhin.components.scadaProject.AScadaProject;

import java.io.*;
import java.util.HashMap;
import java.util.Vector;

public class NodeData implements Serializable {

    protected static NodeData instance;
    protected String identificationKey;
    protected HashMap<String,String> nodeData;
    protected final String NODE_DATA_FILE_PATH = "res/node.ser";

    protected NodeData(String identificationKey) {
        this.identificationKey = identificationKey;
    }

    protected NodeData() {}

    public static NodeData getInstance() {
        try {
            if (instance == null) {
                instance = new NodeData();
                instance.deserialize();
                if (instance.identificationKey == null) {
                    instance = null;
                    throw new NullPointerException("Key isn`t set");
                }
            }
        } catch (NullPointerException e) {
            System.out.println("Ключ не установлен! Чтобы получить экземпляр класса, введите ключ в аргументе");
        }
        return instance;
    }

    public static NodeData getInstance(String identificationKey) {
        if (instance == null) {
            instance = new NodeData(identificationKey);
            instance.serialize();
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

    protected void serialize() {
        try {
            FileOutputStream fos = new FileOutputStream(NODE_DATA_FILE_PATH, false);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(identificationKey);
            oos.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void deserialize() {
        try {
            FileInputStream fis = new FileInputStream(NODE_DATA_FILE_PATH);
            ObjectInputStream ois = new ObjectInputStream(fis);
            identificationKey = (String)ois.readObject();
            ois.close();
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Ошибка чтения сериализованного файла!");
        } catch (ClassNotFoundException e) {
            System.out.println("Класс не найден");
            e.printStackTrace();
        }
    }

}
