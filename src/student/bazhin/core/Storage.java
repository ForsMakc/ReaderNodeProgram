package student.bazhin.core;

import student.bazhin.components.scadaProject.AScadaProject;
import student.bazhin.interfaces.IVisual;

import java.io.*;
import java.util.Vector;

public class Storage implements IVisual {

    protected final String STORAGE_FILE_PATH = "res/scada.ser";
    protected Vector<AScadaProject> scadaProjectsStorage;

    public Storage() {
        scadaProjectsStorage = new Vector<>();
        deserialize();
        for (AScadaProject scadaProject: scadaProjectsStorage) {
            scadaProject.validateScadaData();
        }
        render(Core.getInstance().getView());
    }

    public synchronized Vector<AScadaProject> actionWithScadaList(ENAM action) {
        return scadaProjectsStorage;
    }

    public int getNewId() {
        int maxId = 0;
        for (AScadaProject scadaProject: scadaProjectsStorage) {
            if (maxId < scadaProject.getId()) {
                maxId = scadaProject.getId();
            }
        }
        return maxId + 1;
    }

    public void insertScadaProject(AScadaProject scadaProject) {
        scadaProjectsStorage.add(scadaProject);
        updateScadaProjects();
    }

    public void removeScadaProject(AScadaProject scadaProject) {
        scadaProjectsStorage.remove(scadaProject);
        updateScadaProjects();
    }

    public void updateScadaProjects() {
        serialize();
        refresh();
    }

    protected void serialize() {
        try {
            FileOutputStream fos = new FileOutputStream(STORAGE_FILE_PATH,false);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(scadaProjectsStorage);
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
            scadaProjectsStorage = (Vector<AScadaProject>)ois.readObject();
            ois.close();
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Ошибка чтения сериализованного файла!");
            scadaProjectsStorage.clear();
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
            for (AScadaProject scadaProject: scadaProjectsStorage) {
                scadaProject.render(view);
            }
            view.update(view.getTopPanel());
        }
    }

}
