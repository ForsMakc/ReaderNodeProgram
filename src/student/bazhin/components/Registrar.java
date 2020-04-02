package student.bazhin.components;

import student.bazhin.core.Core;
import student.bazhin.core.View;
import student.bazhin.data.AScadaProjectData;
import student.bazhin.data.AuthData;
import student.bazhin.factory.ScadaFactory;
import student.bazhin.interfaces.IData;
import student.bazhin.interfaces.IVisualComponent;

import javax.swing.*;

public class Registrar implements IVisualComponent {

    protected JTextField scadaPathEdit;
    protected JTextField scadaLoginEdit;
    protected JTextField scadaPassEdit;
    protected JTextField scadaProjectEdit;
    protected JComboBox<String> scadaComboBox;

    @Override
    public IData perform() {
        render(Core.getInstance().getView());
        return null;
    }

    @Override
    public void render(View view) {
        view.getBottomPanel().removeAll();

        JLabel scadaNameLabel = new JLabel("Тип SCADA-системы:");
        view.addComponent(scadaNameLabel,view.getBottomPanel());
        scadaComboBox = new JComboBox<>(ScadaFactory.scadaSystemsList);
        view.addComponent(scadaComboBox,view.getBottomPanel());

        JLabel scadaPathLabel = new JLabel("Путь к SCADA проекту:");
        scadaPathEdit = new JTextField();
        view.addComponent(scadaPathLabel,view.getBottomPanel());
        view.addComponent(scadaPathEdit,view.getBottomPanel());

        JLabel scadaProjectLabel = new JLabel("Название SCADA проекта:");
        scadaProjectEdit = new JTextField();
        view.addComponent(scadaProjectLabel,view.getBottomPanel());
        view.addComponent(scadaProjectEdit,view.getBottomPanel());

        JLabel scadaLoginLabel = new JLabel("Логин SCADA проекта:");
        scadaLoginEdit = new JTextField();
        view.addComponent(scadaLoginLabel,view.getBottomPanel());
        view.addComponent(scadaLoginEdit,view.getBottomPanel());

        JLabel scadaPassLabel = new JLabel("Пароль SCADA проекта:");
        scadaPassEdit = new JTextField();
        view.addComponent(scadaPassLabel,view.getBottomPanel());
        view.addComponent(scadaPassEdit,view.getBottomPanel());

        JButton updateButton = new JButton("Добавить SCADA проект");
        updateButton.addActionListener(e -> addScada());
        view.addComponent(updateButton,view.getBottomPanel());
    }

    protected void addScada() {
        View view = Core.getInstance().getView();
        String scadaName = (String)scadaComboBox.getSelectedItem();

        AScadaProjectData scadaProjectData = ScadaFactory.createScadaProject(scadaName);
        scadaProjectData.setScadaName(scadaName);
        scadaProjectData.setPath(scadaPathEdit.getText());
        scadaProjectData.setScadaProjectName(scadaProjectEdit.getText());
        scadaProjectData.setAuthData(new AuthData(scadaLoginEdit.getText(),scadaPassEdit.getText()));

        if (scadaProjectData.validateScadaData()) {
            Core.getInstance().getStorage().insertScadaProject(scadaProjectData);
        } else {
            JOptionPane.showMessageDialog(view, "Не удалось добавить SCADA проект. Проверьте корректность данных!");
        }

        view.getBottomPanel().removeAll();
        view.update(view.getBottomPanel());
    }

}
