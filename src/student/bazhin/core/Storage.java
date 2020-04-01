package student.bazhin.core;

import student.bazhin.data.AScadaProjectData;
import student.bazhin.data.MasterScada3ProjectData;
import student.bazhin.data.NewScadaProjectData;
import student.bazhin.interfaces.IVisual;

import java.io.*;
import java.util.ArrayList;

public class Storage implements IVisual {

    protected final String STORAGE_FILE_PATH = "res/scada.ser";
    protected ArrayList<AScadaProjectData> spdStorage;

    public Storage() {
        spdStorage = new ArrayList<>();
        insertScadaProject(new MasterScada3ProjectData(1,true));
        insertScadaProject(new NewScadaProjectData(1,100,false));
        insertScadaProject(new MasterScada3ProjectData(2,false));
        refresh();
    }

    public void insertScadaProject(AScadaProjectData scadaProjectData) {
        spdStorage.add(scadaProjectData);
        serialize();
        refresh();
    }

    public void removeScadaProject(AScadaProjectData scadaProjectData) {
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
            spdStorage = (ArrayList<AScadaProjectData>)ois.readObject();
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
            view.getTopPanel().removeAll();
            for (AScadaProjectData scadaProjectData: spdStorage) {
                scadaProjectData.render(view);
            }
        }
    }
}
