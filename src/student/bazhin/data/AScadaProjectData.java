package student.bazhin.data;

import student.bazhin.core.Core;
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

    protected JTextField scadaPathEdit;
    protected JTextField scadaProjectEdit;
    protected JTextField scadaLoginEdit;
    protected JTextField scadaPassEdit;

    public int getId() {
        return id;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setAuthData(AuthData authData) {
        this.authData = authData;
    }

    public void setScadaName(String scadaName) {
        this.scadaName = scadaName;
    }

    public void setScadaProjectName(String scadaProjectName) {
        this.scadaProjectName = scadaProjectName;
    }

    protected abstract void updateScadaSource();

    public abstract boolean validateScadaData();

    @Override
    public void render(View view) {
        if (view != null) {
            JButton itemButton = new JButton(scadaName + " (" + scadaProjectName + ")");
            itemButton.setFont(new Font("Arial", Font.BOLD, 20));
            if (status) {
                itemButton.setForeground(Color.blue);
            } else {
                itemButton.setForeground(Color.red);
            }
            itemButton.addActionListener(new ItemClickHandler(this,view));
            view.addComponent(itemButton,view.getTopPanel());
        }
    }

    protected abstract class ClickHandler implements ActionListener{

        protected View view;
        protected AScadaProjectData sender;

        public ClickHandler(AScadaProjectData sender, View view) {
            this.sender = sender;
            this.view = view;
        }

    }

    protected class ItemClickHandler extends ClickHandler  {

        public ItemClickHandler(AScadaProjectData sender, View view) {
            super(sender,view);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            view.getBottomPanel().removeAll();

            JLabel scadaStatusLabel;
            if (status) {
                scadaStatusLabel = new JLabel("Связь с проектом установлена");
                scadaStatusLabel.setForeground(Color.blue);
            } else {
                scadaStatusLabel = new JLabel("Связь с проектом прервана");
                scadaStatusLabel.setForeground(Color.red);
            }
            view.addComponent(scadaStatusLabel,view.getBottomPanel());

            JLabel scadaNameLabel = new JLabel("Тип SCADA-системы: " + scadaName);
            view.addComponent(scadaNameLabel,view.getBottomPanel());

            JLabel scadaPathLabel = new JLabel("Путь к SCADA проекту:");
            scadaPathEdit = new JTextField(path);
            view.addComponent(scadaPathLabel,view.getBottomPanel());
            view.addComponent(scadaPathEdit,view.getBottomPanel());

            JLabel scadaProjectLabel = new JLabel("Название SCADA проекта:");
            scadaProjectEdit = new JTextField(scadaProjectName);
            view.addComponent(scadaProjectLabel,view.getBottomPanel());
            view.addComponent(scadaProjectEdit,view.getBottomPanel());

            JLabel scadaLoginLabel = new JLabel("Логин SCADA проекта:");
            scadaLoginEdit = new JTextField(authData.getLogin());
            view.addComponent(scadaLoginLabel,view.getBottomPanel());
            view.addComponent(scadaLoginEdit,view.getBottomPanel());

            JLabel scadaPassLabel = new JLabel("Пароль SCADA проекта:");
             scadaPassEdit = new JTextField(authData.getPassword());
            view.addComponent(scadaPassLabel,view.getBottomPanel());
            view.addComponent(scadaPassEdit,view.getBottomPanel());

            JButton updateButton = new JButton("Обновить SCADA проект");
            updateButton.addActionListener(new UpdateClickHandler(sender,view));
            view.addComponent(updateButton,view.getBottomPanel());

            JButton delButton = new JButton("Удалить SCADA проект");
            delButton.addActionListener(new DeleteClickHandler(sender,view));
            view.addComponent(delButton,view.getBottomPanel());
        }

    }

    protected class DeleteClickHandler extends ClickHandler  {

        public DeleteClickHandler(AScadaProjectData sender, View view) {
            super(sender,view);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            view.getBottomPanel().removeAll();
            view.update(view.getBottomPanel());
            Core.getInstance().getStorage().removeScadaProject(sender);
        }

    }

    protected class UpdateClickHandler extends ClickHandler  {

        public UpdateClickHandler(AScadaProjectData sender, View view) {
            super(sender,view);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            status = false; //todo надо обязательно вернуть в статус true, если апдейт удался

            String newPath = scadaPathEdit.getText();
            if (!newPath.equals(path)) {
                path = newPath;
            }

            String newProjectName = scadaProjectEdit.getText();
            if (!newProjectName.equals(scadaProjectName)) {
                scadaProjectName = newProjectName;
            }

            String newLogin = scadaLoginEdit.getText();
            if (!newLogin.equals(authData.getLogin())) {
                authData.setLogin(newLogin);
            }

            String newPassword = scadaPassEdit.getText();
            if (!newPassword.equals(authData.getPassword())) {
                authData.setPassword(newPassword);
            }

            updateScadaSource();
            view.getBottomPanel().removeAll();
            view.update(view.getBottomPanel());
            Core.getInstance().getStorage().updateScadaProjects();
        }

    }

}
