package student.bazhin.core;

import student.bazhin.interfaces.IComponent;
import student.bazhin.data.ScadaProjectData;

import java.util.LinkedList;

public class Core implements IComponent{
    protected static Core instance;
    LinkedList<IComponent> componentsList;
    public View view;

    public View getView(){
        return view;
    }

    public static Core getInstance() {
        if (instance == null) {
            instance = new Core();
        }
        return instance;
    }

    private Core() {
        view = new View(750,500);
        componentsList = new LinkedList<>();
    }

    @Override
    public ScadaProjectData perform() {
        ScadaProjectData scadaProjectData = null;
        while (true) {
            if (!componentsList.isEmpty()) {
                componentsList.element().perform();
                componentsList.poll();
            }
        }
    }
}
