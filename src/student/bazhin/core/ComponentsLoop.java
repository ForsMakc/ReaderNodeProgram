package student.bazhin.core;

import student.bazhin.interfaces.IComponent;
import student.bazhin.interfaces.IData;

import java.util.LinkedList;

public class ComponentsLoop implements IComponent {

    protected LinkedList<IComponent> componentsList;

    public ComponentsLoop() {
        componentsList = new LinkedList<>();
    }

    public void addComponent(IComponent component) {
        componentsList.add(component);
    }

    @Override
    public IData perform() {
        new Thread(() -> {
            while (true) {
                if (!componentsList.isEmpty()) {
                    componentsList.element().perform();
                    componentsList.poll();
                }
            }
        }).start();
        return null;
    }
}
