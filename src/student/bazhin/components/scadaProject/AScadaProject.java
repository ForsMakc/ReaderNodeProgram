package student.bazhin.components.scadaProject;

import student.bazhin.core.Core;
import student.bazhin.core.View;
import student.bazhin.data.PocketData;
import student.bazhin.data.ScadaData;
import student.bazhin.interfaces.IComponent;
import student.bazhin.interfaces.IData;
import student.bazhin.interfaces.IVisualComponent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import static student.bazhin.helper.ActionWithStorage.*;


public abstract class AScadaProject implements IVisualComponent, Serializable{

    ScadaData scadaData = null;

    protected volatile int id;
    protected volatile boolean status;
    protected volatile boolean blocking;
    protected volatile String scadaName;

    protected volatile Map<String,String> keys = new HashMap<>();
    protected volatile Map<String,String> fields = new HashMap<>();
    protected volatile Map<String,JLabel> labels = new HashMap<>();
    protected volatile Map<String,JTextField> edits = new HashMap<>();

    JButton connectionButton;
    JButton updateButton;
    JButton delButton;

    public AScadaProject(int id, String scadaName) {
        this.id = id;
        status = false;
        blocking = false;
        this.scadaName = scadaName;

        keys.put("path","Путь до директории SCADA-проекта");
        keys.put("scadaProjectName","Название SCADA-проекта");
        keys.put("login","Логин пользователя SCADA-проекта");
        keys.put("password","Пароль пользователя SCADA-проекта");

        for (String key: keys.keySet()){
            fields.put(key,"");
            labels.put(key,new JLabel(keys.get(key)));
            edits.put(key,new JTextField());
        }
    }

    protected abstract boolean validateScadaData();

    protected abstract boolean connectToScadaProject();

    protected abstract APoller createScadaPoller();

    protected abstract AConverter createDataConverter();

    protected abstract class APoller implements IComponent{}

    protected abstract class AConverter implements IComponent{}

    protected abstract class ClickHandler implements ActionListener{

        protected View view;
        protected AScadaProject sender;

        public ClickHandler(AScadaProject sender, View view) {
            this.view = view;
            this.sender = sender;
        }

    }


    @Override
    public IData perform() {
        //todo получать данные scada проекта
        if (status) {
            APoller aPoller = createScadaPoller();
            aPoller.perform();
            AConverter aConverter = createDataConverter();
            return aConverter.perform();
        } else {
            return null;
        }
    }

    @Override
    public void render(View view) {
        if (view != null) {
            JButton itemButton = new JButton(scadaName + " (" + fields.get("scadaProjectName") + ")");
            if (status) {
                itemButton.setForeground(Color.blue);
            } else {
                itemButton.setForeground(Color.red);
            }
            if (blocking) {
                itemButton.setFont(new Font("Arial", Font.BOLD, 20));
            } else {
                itemButton.setFont(new Font("Arial", Font.ITALIC, 20));
            }
            itemButton.addActionListener(new ItemClickHandler(this,view));
            view.addComponent(itemButton,view.getTopPanel());
        }
    }

    public void renderScadaFields(View view) {
        for (String key: keys.keySet()){
            JTextField edit = edits.get(key);
            edit.setText(fields.get(key));
            view.addComponent(labels.get(key),view.getBottomPanel());
            view.addComponent(edit,view.getBottomPanel());
        }
    }

    public boolean updateScadaFields() {
        for (String key: keys.keySet()){
            JTextField edit = edits.get(key);
            fields.put(key,edit.getText());
        }
        return validateScadaData();
    }


    public int getId() {
        return id;
    }

    public boolean getStatus() {
        return status;
    }

    public boolean getBlockingType() {
        return blocking;
    }

    protected class ItemClickHandler extends ClickHandler  {

        public ItemClickHandler(AScadaProject sender, View view) {
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

            JLabel scadaBlockingLabel;
            if (blocking) {
                scadaBlockingLabel = new JLabel("Блокирующий SCADA-проект");
            } else {
                scadaBlockingLabel = new JLabel("Неблокирующий SCADA-проект");
            }
            view.addComponent(scadaBlockingLabel,view.getBottomPanel());

            JLabel scadaNameLabel = new JLabel("Тип SCADA-системы: " + scadaName);
            view.addComponent(scadaNameLabel,view.getBottomPanel());

            renderScadaFields(view);

            if (status) {
                connectionButton = new JButton("Отключить SCADA проект");
                connectionButton.addActionListener(new DisconnectClickHandler(sender,view));
            } else {
                connectionButton = new JButton("Подключить SCADA проект");
                connectionButton.addActionListener(new ConnectClickHandler(sender,view));
            }
            view.addComponent(connectionButton,view.getBottomPanel());

            updateButton = new JButton("Обновить SCADA проект");
            updateButton.addActionListener(new UpdateClickHandler(sender,view));
            view.addComponent(updateButton,view.getBottomPanel());

            delButton = new JButton("Удалить SCADA проект");
            delButton.addActionListener(new DeleteClickHandler(sender,view));
            view.addComponent(delButton,view.getBottomPanel());
        }

    }

    protected class DisconnectClickHandler extends ClickHandler  {

        public DisconnectClickHandler(AScadaProject sender, View view) {
            super(sender,view);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            status = false;
            Core.getInstance().getStorage().actionWithStorage(VIEW,null);
            view.getBottomPanel().removeAll();
            view.update(view.getBottomPanel());
            JOptionPane.showMessageDialog(view, "SCADA проект успешно отключён!");
        }

    }

    protected class ConnectClickHandler extends ClickHandler  {

        public ConnectClickHandler(AScadaProject sender, View view) {
            super(sender,view);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (connectToScadaProject()) {
                status = true;
                Core.getInstance().getStorage().actionWithStorage(VIEW,null);
                view.getBottomPanel().removeAll();
                view.update(view.getBottomPanel());
                JOptionPane.showMessageDialog(view, "SCADA проект успешно подключён!");
            } else {
                JOptionPane.showMessageDialog(view, "Не удалось подключить SCADA проект!");
            }
        }

    }

    protected class DeleteClickHandler extends ClickHandler  {

        public DeleteClickHandler(AScadaProject sender, View view) {
            super(sender,view);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            view.getBottomPanel().removeAll();
            view.update(view.getBottomPanel());
            Core.getInstance().getStorage().actionWithStorage(DELETE,sender);
        }

    }

    protected class UpdateClickHandler extends ClickHandler  {

        public UpdateClickHandler(AScadaProject sender, View view) {
            super(sender,view);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            status = false;
            view.getBottomPanel().removeAll();
            view.update(view.getBottomPanel());
            Core.getInstance().getStorage().actionWithStorage(UPDATE, () -> {
                boolean canUpdate = updateScadaFields();
                return (!canUpdate) ? null : new IData() {
                    @Override
                    public String toString() {
                        return "IData";
                    }
                };
            });
        }

    }

}
