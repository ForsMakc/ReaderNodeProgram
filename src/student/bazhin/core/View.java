package student.bazhin.core;

import javax.swing.*;
import java.awt.*;

public class View extends JFrame {

    public View(int width, int heigth) {
        super("Считывающий узел");
        setLayout(new FlowLayout());
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(width,heigth);
    }

    public void addButton(JButton b) {
        add(b);
        invalidate();
    }
}
