package student.bazhin.core;

import student.bazhin.interfaces.IComponent;
import student.bazhin.interfaces.IData;

import java.util.LinkedList;

public class Core implements IComponent{

    protected View view;
    protected Storage storage;
    protected boolean working;
    protected static Core instance;
    protected LinkedList<IComponent> componentsList;

    protected Core() {}

    protected void init() {
        working = true;
        view = new View();
        storage = new Storage();
        componentsList = new LinkedList<>();
    }

    public static Core getInstance() {
        if (instance == null) {
            instance = new Core();
            instance.init();
        }
        return instance;
    }

    public Storage getStorage() {
        return storage;
    }

    public View getView(){
        return view;
    }

    @Override
    public IData perform() {
        if (working) {
            while (true) {
                if (!componentsList.isEmpty()) {
                    componentsList.element().perform();
                    componentsList.poll();
                }
            }
        }
        return null;
    }

}
