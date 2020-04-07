package student.bazhin.components.scadaProject;

import student.bazhin.core.Core;
import student.bazhin.data.ScadaData;
import student.bazhin.interfaces.IData;

import java.io.File;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.Rectangle;
import java.io.IOException;
import java.awt.AWTException;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import javax.swing.*;
import javax.swing.filechooser.FileSystemView;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Vector;

import static student.bazhin.helper.ActionWithStorage.GET;

public class MasterScada3Project extends AScadaProject implements Serializable {

    public MasterScada3Project(int id, String scadaName) {
        super(id,scadaName);
        blocking = true;

        keys.put("dataBasePath","Путь до базы данных SCADA-проекта");
        fields.put("dataBasePath","");
        labels.put("dataBasePath",new JLabel("Путь до базы данных SCADA-проекта"));
        edits.put("dataBasePath",new JTextField());
    }

    private static File getHomeDir() {
        FileSystemView fsv = FileSystemView.getFileSystemView();
        return fsv.getHomeDirectory();
    }

    private static BufferedImage grabScreen() {
        try {
            return new Robot().createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize())) ;
        } catch (SecurityException | AWTException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public IData perform() {
        try {
            ImageIO.write(grabScreen(), "png", new File(getHomeDir(), "screen.png"));
        } catch (IOException e) {
            System.out.println("IO exception"+e);
        }

        //todo получать данные scada проекта
        if (status) {
            return new ScadaData(id + " " + scadaName);
        } else {
            return null;
        }
    }

    @Override
    public boolean validateScadaData() {
//        if ((!path.equals("")) && (!scadaName.equals("")) && (!scadaProjectName.equals("")) && (!authData.getLogin().equals("")) && (!authData.getPassword().equals(""))) {
//            //проверка бд
//            if (dataBasePath.equals("")) {
//                //проверить существование файла БД
//                return false;
//            }
//            //проверка scada директории
//            if (!Files.exists(Paths.get(path))) {
//                return false;
//            }
//            //проверка содержимого scada директории
//            if (!Files.exists(Paths.get(path + "/ErrorLogs")) || !Files.exists(Paths.get(path + "/Объект")) || !Files.exists(Paths.get(path + "/Система"))) {
//                return false;
//            }
//            return true;
//        }
        return true;
    }

    @Override
    public boolean connectToScadaProject() {
        Vector<AScadaProject> scadaProjectsStorage = Core.getInstance().getStorage().actionWithStorage(GET,null);
        for (AScadaProject scadaProject : scadaProjectsStorage) {
            if ((scadaProject.getBlockingType()) && (scadaProject.getStatus()) && (scadaProject != this)) {
                JOptionPane.showMessageDialog(Core.getInstance().getView(), "Нельзя подключать одновременно несколько блокирующих проектов!");
                return false;
            }
        }
        return true;
    }
}
