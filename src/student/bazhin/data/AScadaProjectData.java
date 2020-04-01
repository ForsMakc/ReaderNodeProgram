package student.bazhin.data;

import student.bazhin.core.View;
import student.bazhin.interfaces.IData;
import student.bazhin.interfaces.IVisualComponent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;

public abstract class AScadaProjectData implements IVisualComponent, IData, Serializable{

    protected int id;
    protected String path;
    protected String scadaName;
    protected AuthData authData;
    protected String scadaProjectName;
    protected boolean status;

    @Override
    public void render(View view) {

        if (view != null) {
            JButton itemButton = new JButton(scadaName + " (" + scadaProjectName + ")");
            itemButton.addActionListener(new ActionListener() {

                private View view;
                private AScadaProjectData sender;

                public ActionListener init(AScadaProjectData sender, View view) {
                    this.sender = sender;
                    this.view = view;
                    return this;
                }

                public void actionPerformed(ActionEvent e) {
                    view.getBottomPanel().removeAll();

                    JLabel scadaStatusLabel = new JLabel("Связь с проектом");
                    if (status) {
                        scadaStatusLabel.setForeground(Color.green);
                    } else {
                        scadaStatusLabel.setForeground(Color.red);
                    }
                    view.addComponent(scadaStatusLabel,view.getBottomPanel());

                    JLabel scadaNameLabel = new JLabel("Тип SCADA-системы: " + scadaName);
                    view.addComponent(scadaNameLabel,view.getBottomPanel());

                    JLabel scadaPathLabel = new JLabel("Путь к SCADA проекту:");
                    JTextField scadaPathEdit = new JTextField(path);
                    view.addComponent(scadaPathLabel,view.getBottomPanel());
                    view.addComponent(scadaPathEdit,view.getBottomPanel());

                    JLabel scadaLoginLabel = new JLabel("Логин SCADA проекта:");
                    JTextField scadaLoginEdit = new JTextField(authData.getLogin());
                    view.addComponent(scadaLoginLabel,view.getBottomPanel());
                    view.addComponent(scadaLoginEdit,view.getBottomPanel());

                    JLabel scadaPassLabel = new JLabel("Пароль SCADA проекта:");
                    JTextField scadaPassEdit = new JTextField(authData.getPassword());
                    view.addComponent(scadaPassLabel,view.getBottomPanel());
                    view.addComponent(scadaPassEdit,view.getBottomPanel());

                    JButton delButton = new JButton("Удалить SCADA проект");
                    JButton updateButton = new JButton("Обновить SCADA проект");
                    view.addComponent(delButton,view.getBottomPanel());
                    view.addComponent(updateButton,view.getBottomPanel());
//                    JPanel ButtonContainer = new JPanel(new FlowLayout(FlowLayout.LEFT));
//                    ButtonContainer.add(delButton);
//                    ButtonContainer.add(updateButton);
//                    view.addComponent(ButtonContainer,view.getBottomPanel());
                }

            }.init(this,view));
            view.addComponent(itemButton,view.getTopPanel());
        }
    }
}
