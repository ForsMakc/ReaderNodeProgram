package student.bazhin.components;

import student.bazhin.components.scadaProject.AScadaProject;
import student.bazhin.core.Core;
import student.bazhin.core.View;
import student.bazhin.factory.scada.ScadaFactory;
import student.bazhin.interfaces.IData;
import student.bazhin.interfaces.IVisualComponent;

import javax.swing.*;

import static student.bazhin.helper.ActionWithStorage.INSERT;

public class Registrar implements IVisualComponent {

    protected AScadaProject scadaProject;
    protected JComboBox<String> scadaComboBox;

    @Override
    public IData perform() {
        render(Core.getInstance().getView());
        return null;
    }

    @Override
    public void render(View view) {
        scadaComboBox = new JComboBox<>(ScadaFactory.scadaSystemsList);
        scadaComboBox.addItemListener(e -> switchScada(view));

        view.getBottomPanel().removeAll();
        renderScadaComboBox(view);
        switchScada(view);
        renderAddButton(view);
    }

    protected void switchScada(View view) {
        String scadaName = (String)scadaComboBox.getSelectedItem();
        if (scadaName != null) {
            scadaProject = ScadaFactory.createScadaProject(scadaName);
            if (scadaProject != null) {
                view.getBottomPanel().removeAll();
                view.update(view.getBottomPanel());
                renderScadaComboBox(view);
                scadaProject.renderScadaFields(view);
            }
        }
    }

    protected void renderScadaComboBox(View view) {
        JLabel scadaNameLabel = new JLabel("Тип SCADA-системы:");
        view.addComponent(scadaNameLabel,view.getBottomPanel());
        view.addComponent(scadaComboBox,view.getBottomPanel());
    }

    protected void renderAddButton(View view) {
        JButton addButton = new JButton("Добавить SCADA проект");
        addButton.addActionListener(e -> addScada(view));
        view.addComponent(addButton,view.getBottomPanel());
    }

    protected void addScada(View view) {
        if (scadaProject != null) {
            if (scadaProject.updateScadaFields()) {
                Core.getInstance().getStorage().actionWithStorage(INSERT,scadaProject);
            } else {
                JOptionPane.showMessageDialog(view, "Не удалось добавить SCADA проект. Проверьте корректность данных!");
            }

            view.getBottomPanel().removeAll();
            view.update(view.getBottomPanel());
        } else {
            throw new NullPointerException("Адаптер SCADA-системы не найден!");
        }
    }

}
