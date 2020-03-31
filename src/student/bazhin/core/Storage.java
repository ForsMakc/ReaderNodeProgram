package student.bazhin.core;

import student.bazhin.data.MasterScada3ProjectData;
import student.bazhin.interfaces.IScadaProjectData;
import student.bazhin.interfaces.IVisual;

import java.io.*;
import java.util.ArrayList;

public class Storage implements IVisual {

    protected final String STORAGE_FILE_PATH = "res/scada.ser";
    protected ArrayList<IScadaProjectData> spdStorage;

    public Storage() {
        spdStorage = new ArrayList<>();
//        insertScadaProject(new MasterScada3ProjectData(1));
//        insertScadaProject(new MasterScada3ProjectData(2));
//        insertScadaProject(new MasterScada3ProjectData(3));
//        insertScadaProject(new MasterScada3ProjectData(4));
        refresh();
    }

    public void insertScadaProject(IScadaProjectData scadaProjectData) {
        spdStorage.add(scadaProjectData);
        serialize();
        refresh();
    }

    public void removeScadaProject(IScadaProjectData scadaProjectData) {
        spdStorage.remove(scadaProjectData);
        serialize();
        refresh();
    }

    protected void serialize() {
        try {
            FileOutputStream fos = new FileOutputStream(STORAGE_FILE_PATH,false);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(spdStorage);
            oos.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void deserialize() {
        try {
            FileInputStream fis = new FileInputStream(STORAGE_FILE_PATH);
            ObjectInputStream ois = new ObjectInputStream(fis);
            spdStorage = (ArrayList<IScadaProjectData>)ois.readObject();
            ois.close();
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.out.println("Класс не найден");
            e.printStackTrace();
        }
    }

    protected void refresh() {
        deserialize();
        render(Core.getInstance().getView());
    }

    @Override
    public void render(View view) {
        if (view != null) {
//            view.getTopPanel().removeAll();
            for (IScadaProjectData scadaProjectData: spdStorage) {
                scadaProjectData.render(view);
            }
        }
    }
}
