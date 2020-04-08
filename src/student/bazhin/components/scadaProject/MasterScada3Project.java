package student.bazhin.components.scadaProject;

import student.bazhin.core.Core;
import student.bazhin.data.PocketData;
import student.bazhin.data.ScadaData;
import student.bazhin.interfaces.IComponent;
import student.bazhin.interfaces.IData;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.Rectangle;
import java.io.IOException;
import java.awt.AWTException;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import javax.swing.*;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;

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

    private static BufferedImage grabScreen() {
        try {
            return new Robot().createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize())) ;
        } catch (SecurityException | AWTException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean validateScadaData() {
        //проверка наличия всех данных scada проекта
        for (String key: keys.keySet()) {
            if (fields.get(key).equals("")) {
                return false;
            }
        }

        //проверка scada директории
        String path = fields.get("path");
        if (!Files.exists(Paths.get(path))) {
            return false;
        }
        if (!Files.exists(Paths.get(path + "/ErrorLogs")) || !Files.exists(Paths.get(path + "/Объект")) || !Files.exists(Paths.get(path + "/Система"))) {
            return false;
        }
        
        //todo проверка бд

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

        boolean result = validateScadaData();
        if (result) {
            String path = fields.get("path");
            File folder = new File(path + "/ErrorLogs");
            for (File file : Objects.requireNonNull(folder.listFiles())) {
                result = file.length() == 0;
            }
            if (!result) {
                JOptionPane.showMessageDialog(Core.getInstance().getView(), "SCADA-проект не находится в режиме исполнения!");
            }
        }
        return result;
    }

    @Override
    protected APoller createScadaPoller() {
        return new Poller();
    }

    @Override
    protected AConverter createDataConverter() {
        return new Converter();
    }

    protected class Poller extends APoller {

        //todo В ОПИСАНИЕ АЛГОРИТОМВ
        protected String getDirFiles(String path, int level, List<String> improper) {
            int fileCount = 0;
            File folder = new File(path);
            StringBuilder result = new StringBuilder();
            for (File file : Objects.requireNonNull(folder.listFiles())) {
                if ((file.isDirectory()) && (!improper.contains(file.getName()))) {
                    String comma = "";
                    String bracketL = "";
                    String bracketR = "";
                    if (Arrays.asList(Objects.requireNonNull(folder.listFiles())).indexOf(file) == 0) {
                        bracketL = ": [";
                    } else {
                        comma = ", ";
                        if (Arrays.asList(Objects.requireNonNull(folder.listFiles())).indexOf(file) == Objects.requireNonNull(folder.listFiles()).length) {
                            bracketR = "]";
                        }
                    }
                    result.append(bracketL).append(comma).append("\"").append(file.getName()).append("\"").append(getDirFiles(path + "/" + file.getName(),++level,improper)).append(bracketR);
                    fileCount++;
                }
            }
            return result.toString();
        }

        @Override
        public IData perform() {
            //Сбор структуры
            int level = 0;
            List<String> improper = Arrays.asList("Мнемосхема","__Data","__Event","Отчет~","Журнал~","Тренды~","Рецепт","Окно объекта","Неквитированные сообщения","Активные сообщения","Изображение объекта","Основной журнал");
            String fileObjectsJsonStr = "\"Объект\"" + getDirFiles(fields.get("path" + "/Объект"),level,improper);
            String fileSystemJsonStr = "\"Система\"" + getDirFiles(fields.get("path" + "/Объект"),level,improper);
            String structJsonStr = "\"struct\":[" + fileObjectsJsonStr + " " + fileSystemJsonStr + "]";

            //Сбор данных БД
            String mapJsonStr = "\"map\":[]";
            String dataJsonStr = "\"data\":[]";

            //Сбор бинарных данных
            String strData = "";
            try {
                BufferedImage scadaGrab = grabScreen();
                if (scadaGrab != null) {
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    ImageIO.write(scadaGrab, "png", bos);
                    byte[] byteData = bos.toByteArray();
                    strData = new String(byteData,StandardCharsets.UTF_8);
                }
            } catch (IOException e) {
                System.out.println("IO exception" + e);
            }
            String binJsonStr = "\"bin\":[\"res\":[],\"frame\":\"" + strData + "\"]";

            return new ScadaData(structJsonStr,mapJsonStr,dataJsonStr,binJsonStr);
        }

    }

    protected class Converter extends AConverter {

        @Override
        public IData perform() {
            StringBuilder metaJsonStr = new StringBuilder("\"meta\" : [");
            int count = 0;
            String comma = "";
            for (String key: keys.keySet()){
                comma = (count == 0) ? "" : ",";
                if (Arrays.asList("scadaProjectName","login","password").contains(key)) {
                    metaJsonStr.append(comma).append("\"").append(key).append("\"").append(":").append("\"").append(fields.get(key)).append("\"");
                }
                count++;
            }
            metaJsonStr.append(",\"scadaId\":\"").append(id).append("\"").append(",\"nodeId\":\"").append(Core.getInstance().getNodeId()).append("\"");
            metaJsonStr.append(",\"timestamp\":\"").append(new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date())).append("\"]");
            scadaData.scadaDataList.add(metaJsonStr.toString());

            StringBuilder pocket = new StringBuilder();
            pocket.append("\"type\":\"data\"");
            for (String jsonStr: scadaData.scadaDataList) {
                pocket.append(",").append(jsonStr);
            }

            return new PocketData(pocket.toString());
        }

    }

}
