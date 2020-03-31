package student.bazhin.components;

import student.bazhin.core.Core;
import student.bazhin.core.View;
import student.bazhin.interfaces.IData;
import student.bazhin.interfaces.IVisualComponent;

import javax.swing.*;

public class Test implements IVisualComponent {

    int type;

    public Test(int type) {
        this.type = type;
    }

    @Override
    public void render(View view) {
        view.addButton(new JButton("Тестовая кнопка"));
        //на батон навесить добавление следующего логического компонента в цикл компонентов
    }

    @Override
    public IData perform() {
        render(Core.getInstance().getView());
        return null;
    }
}
