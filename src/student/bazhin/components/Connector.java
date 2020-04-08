package student.bazhin.components;

import student.bazhin.components.scadaProject.AScadaProject;
import student.bazhin.core.Core;
import student.bazhin.data.PocketData;
import student.bazhin.interfaces.IComponent;
import student.bazhin.interfaces.IData;

import java.io.*;
import java.net.Socket;
import java.util.ConcurrentModificationException;
import java.util.Vector;

import static student.bazhin.helper.ActionWithStorage.CALLBACK;
import static student.bazhin.helper.ActionWithStorage.GET;

public class Connector implements IComponent {

    protected static final int serverPort = 3345;
    protected static final String serverHost = "localhost";

    protected Socket clientSocket = null;
    protected PrintWriter serverOut = null;
    protected BufferedReader serverIn = null;

    @Override
    public IData perform() {
        if (initConnection()) {
            Core.getInstance().getView().setConnectionInfo(true);
            startSending();
        }
        startSending();
        return null;
    }

    protected boolean initConnection() {
        try {
            clientSocket = new Socket(serverHost, serverPort);
        } catch (IOException e) {
            e.printStackTrace();
            closeConnection();
            return false;
        }
        try {
            serverOut = new PrintWriter(clientSocket.getOutputStream(),true);
        } catch (IOException e) {
            e.printStackTrace();
            closeConnection();
            return false;
        }
        try {
            serverIn = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
            closeConnection();
            return false;
        }
        if (clientSocket.isConnected()) {
            handleInitResponse(sendDataToServer(getInitData()));
        }
        return haveConnection();
    }

    protected boolean haveConnection() {
        return sendDataToServer(new PocketData("test")) != null;
    }

    protected PocketData getInitData() {
        return new PocketData("Hello!");
    }

    protected void handleInitResponse(PocketData initResponse) {
        //todo обработка ответа инициализации сервера
    }

    protected void startSending() {
        new Thread(() -> {
//            while (haveConnection()) {
            while (true) {
                try {
                    Core.getInstance().getStorage().actionWithStorage(CALLBACK, () -> {
                        Vector<AScadaProject> scadaProjectsStorage;
                        scadaProjectsStorage = Core.getInstance().getStorage().actionWithStorage(GET,null);
                        for (AScadaProject scadaProject : scadaProjectsStorage) {
//                            if (haveConnection()) {
                                PocketData data = (PocketData)scadaProject.perform();
                                // PocketData response = sendDataToServer(data);
                                PocketData response = null;
                                if (data != null) {
                                    response = sendDataToServer(data.add(" Номер скада проекта: " + scadaProjectsStorage.indexOf(scadaProject)));
                                }
                                if (response != null) {
                                    //todo обработка ответа сервера
                                }
//                            } else {
//                                break;
//                            }
                        }
                        return null;
                    });
                } catch (ConcurrentModificationException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    protected PocketData sendDataToServer(PocketData dataToServer) {
        if (dataToServer != null) {
            try {
                serverOut.println(dataToServer);
                System.out.println("Данные отправлены:" + dataToServer);

                String line;
                StringBuilder serverResponse = new StringBuilder();
                while (!(line = serverIn.readLine()).equals("")) {
                    serverResponse.append(line);
                }
                System.out.println("Данные получены:" + serverResponse);
                return new PocketData(serverResponse.toString());
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Ошибка соединения с сервером");
                closeConnection();
                return null;
            }
        }
        return null;
    }

    protected void closeConnection() {
        Core.getInstance().getView().setConnectionInfo(false);
        try {
            if (clientSocket != null) {
                clientSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            if (serverIn != null) {
                serverIn.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (serverOut != null) {
            serverOut.close();
        }

    }

}
