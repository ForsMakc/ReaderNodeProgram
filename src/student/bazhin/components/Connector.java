package student.bazhin.components;

import student.bazhin.core.Core;
import student.bazhin.data.AScadaProject;
import student.bazhin.data.ScadaData;
import student.bazhin.interfaces.IComponent;
import student.bazhin.interfaces.IData;

import java.io.*;
import java.net.Socket;
import java.util.Vector;

public class Connector implements IComponent {

    protected static final String serverHost = "localhost";
    protected static final int serverPort = 3345;

    protected Socket clientSocket = null;
    protected PrintWriter serverOut = null;
    protected BufferedReader serverIn = null;


    @Override
    public IData perform() {
        try {
            clientSocket = new Socket(serverHost, serverPort);
            serverOut = new PrintWriter(clientSocket.getOutputStream(),true);
            serverIn = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            System.out.println("Клиент подключился к сокету.");

            if (clientSocket.isConnected()) {
                StringBuffer initServerData = getInitData();
                serverOut.println(initServerData);
                System.out.println("Данные отправлены:" + initServerData);

                String line = null;
                StringBuilder initServerResponse = new StringBuilder();
                while ((line = serverIn.readLine()) != null) {
                    initServerResponse.append(line);
                }
                System.out.println("Данные получены:" + initServerResponse);

                startSending();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void startSending() {
        Vector<AScadaProject> scadaProjectsStorage = Core.getInstance().getStorage().getScadaList();
        new Thread(() -> {
            while (clientSocket.isConnected()) {
                for (AScadaProject scadaProject : scadaProjectsStorage) {
                    if (scadaProject.getStatus()){
                        ScadaData data = (ScadaData)scadaProject.perform();
                        sendDataToServer(data);
                    }
                }
            }
        }).start();
    }

    private void sendDataToServer(ScadaData data) {
    }

    private StringBuffer getInitData() {
        return new StringBuffer("test");
    }

//                    // проверяем условие выхода из соединения
//                    if(clientCommand.equalsIgnoreCase("quit")){
//
//                        // если условие выхода достигнуто разъединяемся
//                        System.out.println("Client kill connections");
//                        Thread.sleep(2000);
//
//                        // смотрим что нам ответил сервер на последок перед закрытием ресурсов
//                        if(ois.read() > -1)     {
//                            System.out.println("reading...");
//                            String in = ois.readUTF();
//                            System.out.println(in);
//                        }
//
//                        // после предварительных приготовлений выходим из цикла записи чтения
//                        break;
//                    }
}
