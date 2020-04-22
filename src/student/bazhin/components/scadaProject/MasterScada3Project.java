package student.bazhin.components.scadaProject;

import student.bazhin.core.Core;
import student.bazhin.data.PocketData;
import student.bazhin.data.PollerData;
import student.bazhin.databases.ADatabase;
import student.bazhin.databases.FirebirdDatabase;
import student.bazhin.factory.database.DBCFactory;
import student.bazhin.interfaces.IData;

import java.io.*;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.Rectangle;
import java.awt.AWTException;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import javax.swing.*;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;

import static student.bazhin.helper.ActionWithStorage.GET;
import static student.bazhin.helper.ActionWithStorage.VIEW;
import static student.bazhin.helper.Constants.*;
import static student.bazhin.helper.PocketHeaders.DATA;

public class MasterScada3Project extends AScadaProject implements Serializable {

    ADatabase database;
    protected static final String DATABASE_TYPE_NAME = "Firebird";

    public MasterScada3Project(int id, String scadaName) {
        super(id,scadaName);
        blocking = true;
        database = DBCFactory.createDBConnection(DATABASE_TYPE_NAME);

        keys.put("dataBasePath","Путь до базы данных SCADA-проекта");
        fields.put("dataBasePath","");
        labels.put("dataBasePath",new JLabel("Путь до базы данных SCADA-проекта"));
        edits.put("dataBasePath",new JTextField());
    }

    protected static BufferedImage grabScreen() {
        try {
            return new Robot().createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize())) ;
        } catch (SecurityException | AWTException e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    protected boolean isWorking() {
        boolean result = true;
        String path = fields.get("path");
        File folder = new File(path + "/ErrorLogs");
        for (File file : Objects.requireNonNull(folder.listFiles())) {
            result = file.length() == 0;
        }

        boolean statusBefore = status;
        if (result) {
            status = true;
            if (!statusBefore) {
                updateScadaProjectList(Core.getInstance().getView());
            }
        } else {
            status = false;
            if (statusBefore) {
                updateScadaProjectList(Core.getInstance().getView());
            }
            JOptionPane.showMessageDialog(Core.getInstance().getView(), "SCADA-проект не находится в режиме исполнения!");
        }
        return result;
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
        
        //проверка бд
        if (database != null) {
            try {
                String dbName = path + "\\" + fields.get("dataBasePath");
                database.init(dbName);
                database.connect();
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
                return false;
            }
        }

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
        return validateScadaData() && isWorking();
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

        protected String rootDirectory = "\\Объект";
        protected List<String> improper = Arrays.asList("Мнемосхема","__Data","__Event","Отчет~","Журнал~","Тренды~","Рецепт","Окно объекта","Неквитированные сообщения","Активные сообщения","Изображение объекта","Основной журнал");

        protected ArrayList<String> getDirFiles(String path, String name, ArrayList<String> structData){
            File folder = new File(path);
            for (File file : Objects.requireNonNull(folder.listFiles())) {
                if ((file.isDirectory()) && (!improper.contains(file.getName()))) {
                    String curName = name + ((name.equals("")) ? "" : ".") + file.getName();
                    getDirFiles(file.toString(),curName,structData);
                    structData.add(curName);
                }
            }
            return structData;
        }

        protected void readDBData(PollerData pollerData) {
            String query = "select data.ITEMID,\"VALUE\",NAME,\"TIME\"\n" +
                    "from MASDATARAW as data\n" +
                    "join MASDATAITEMS as meta\n" +
                    "on data.ITEMID = meta.ITEMID\n" +
                    "where LAYER = 1\n" +
                    ((((FirebirdDatabase)database).lastTimestamp.equals("")) ? "" : "and \"TIME\" >= '" + (((FirebirdDatabase)database).lastTimestamp) + "'\n") +
                    "order by \"TIME\" desc";
            try {
                database.connect();
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
            }
            ADatabase.TableModel data = database.getData(query);
            String lastTimestamp = data.getValueAt(0,"TIME").toString();

            if (
                (((FirebirdDatabase)database).lastData != null) &&
                ((((FirebirdDatabase)database).lastData.size() != 0)) &&
                (data != null) &&
                (data.getRowCount() != 0)
            ) {
                boolean equals;
                int index = data.getRowCount() - 1;
                while ((index >= 0) && (data.getValueAt(index,"TIME").toString().equals(lastTimestamp))) {
                    equals = true;
                    for (int i = 0; i < ((FirebirdDatabase)database).lastData.size(); i++) {
                        equals = true;
                        ArrayList<Object> row = ((FirebirdDatabase)database).lastData.get(i);
                        for (int j = 0; j < data.getColumnCount(); j++) {
                            if (!data.getValueAt(index, j).equals(row.get(j))) {
                                equals = false;
                            }
                        }
                        if (equals) {
                            break;
                        }
                    }
                    if (equals) {
                        data.removeRow(index);
                        index = data.getRowCount() - 1;
                    } else {
                        index--;
                    }
                }
            }

            List<ArrayList<Object>> lastData = new ArrayList<>();
            int i = 0;
            if ((data.getRowCount() != 0)) {
                while ((i < data.getRowCount()) && (data.getValueAt(i,"TIME").toString().equals(lastTimestamp))) {
                    lastData.add(data.getRow(i++));
                }
            }
            ((FirebirdDatabase)database).lastData = lastData;
            ((FirebirdDatabase)database).lastTimestamp = lastTimestamp;
        }


        @Override
        public IData perform() {
            PollerData pollerData = new PollerData();

            //Сбор структуры проекта
            pollerData.put(STRUCT_DATA_MAPKEY, getDirFiles(fields.get("path") + rootDirectory,"", new ArrayList<>()));

            //Сбор данных базы данных
            readDBData(pollerData);

            //Сбор бинарных данных
            String strData = "";
            try {
                BufferedImage scadaGrab = grabScreen();
                if (scadaGrab != null) {
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    ImageIO.write(scadaGrab, "jpg", bos);
                    byte[] byteData = bos.toByteArray();
                    strData = Base64.getEncoder().encodeToString(byteData);
                }
            } catch (IOException e) {
                System.out.println("IO exception" + e);
            }
            pollerData.put(FRAME_MAPKEY,strData);

            return pollerData;
        }

        @Override
        public IData perform(IData data) {
            return null;
        }
    }

    protected class Converter extends AConverter {

        @Override
        public IData perform(IData data) {
            PollerData pollerData = (PollerData)data;
            PocketData pocketData = new PocketData(DATA);

            HashMap<String,String> metaData = new HashMap<>();
            metaData.put(NODE_ID_MAPKEY,Core.getInstance().getNodeId());
            metaData.put(LOGIN_MAPKEY,fields.get(LOGIN_FIELD));
            metaData.put(PASSWORD_MAPKEY,fields.get(PASSWORD_FIELD));
            metaData.put(SPROJECT_ID_MAPKEY,String.valueOf(id));
            metaData.put(SPROJECT_NAME_MAPKEY,fields.get(SPROJECT_NAME_FIELD));
            metaData.put(SCADA_NAME_MAPKEY,scadaName);
            metaData.put(TIMESTAMP_MAPKEY,new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date()));

            pocketData.setMetaData(metaData);
            pocketData.setStructData((ArrayList<String>)pollerData.get(STRUCT_DATA_MAPKEY));
            pocketData.setValuesData(null);
            pocketData.setKeysMapData(null);
            pocketData.setBinaryFrame((String)pollerData.get(FRAME_MAPKEY));

            return pocketData;
        }

        @Override
        public IData perform() {
            return null;
        }
    }

}
