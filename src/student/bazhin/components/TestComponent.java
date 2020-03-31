package student.bazhin.components;

import student.bazhin.core.Core;
import student.bazhin.core.View;
import student.bazhin.interfaces.IVisualComponent;
import student.bazhin.data.ScadaProjectData;

import javax.swing.*;

public class TestComponent implements IVisualComponent {

    int type;

    public TestComponent(int type) {
        this.type = type;
    }

    @Override
    public void render(View view) {
        view.addButton(new JButton("Тестовая кнопка"));
        //на батон навесить добавление следующего логического компонента в цикл компонентов
    }

    @Override
    public ScadaProjectData perform() {
        render(Core.getInstance().getView());
        return null;
    }
}
