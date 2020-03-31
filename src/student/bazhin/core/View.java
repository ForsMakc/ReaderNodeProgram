package student.bazhin.core;

import javax.swing.*;
import java.awt.*;

import static java.lang.Math.round;

public class View extends JFrame {

    int width = 550;
    int height = 750;

    protected JPanel topPanel;
    protected JPanel bottomPanel;

    protected JScrollPane topJScrollPain;
    protected JScrollPane bottomJScrollPain;


    public View() {
        super("Считывающий узел");
        init();
        setVisible(true);
    }

    public void addButton(JButton b) {
        add(b);
        invalidate();
    }

    public void addButton(JButton b, JPanel panel) {
        panel.add(b);
//        topJScrollPain.revalidate();
//        bottomJScrollPain.revalidate();
    }

    public JPanel getTopPanel() {
        return topPanel;
    }

    public JPanel getBottomPanel() {
        return bottomPanel;
    }

    protected void init() {
        setResizable(false);
        setMenu();
        setSplitFrame();
        setSize(width,height);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    protected void setMenu() {
        Font font = new Font("Verdana",Font.PLAIN,12);
        JMenuBar menuBar = new JMenuBar();
        JMenu appMenu = new JMenu("Приложение");
        appMenu.setFont(font);

        JMenuItem newMenuItem = new JMenuItem("Новый SCADA проект");
        newMenuItem.addActionListener(event -> System.out.println("test"));
        newMenuItem.setFont(font);
        appMenu.add(newMenuItem);

        menuBar.add(appMenu);
        setJMenuBar(menuBar);
    }

    protected void setSplitFrame() {
        topPanel = new JPanel();
        topPanel.setLayout(new GridLayout(0, 1, 5, 12));
        bottomPanel = new JPanel();

        topJScrollPain = new JScrollPane(topPanel);
        bottomJScrollPain = new JScrollPane(bottomPanel);

        JSplitPane splitVertical = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitVertical.setTopComponent(topJScrollPain);
        splitVertical.setBottomComponent(bottomJScrollPain);
        splitVertical.setDividerLocation((int)round(height * 0.5));
        add(splitVertical);

//        for (int i = 1; i < 40; i++) {
//            JButton b = new JButton("Скада " + i);
//            topPanel.add(b);
//        }
//        topJScrollPain.revalidate();
    }
}
