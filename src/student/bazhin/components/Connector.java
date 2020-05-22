package student.bazhin.components;

import student.bazhin.components.scadaProject.AScadaProject;
import student.bazhin.core.Core;
import student.bazhin.data.PocketData;
import student.bazhin.interfaces.IComponent;
import student.bazhin.interfaces.IData;

import java.io.*;
import java.net.Socket;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Vector;

import static student.bazhin.helper.ActionWithStorage.CALLBACK;
import static student.bazhin.helper.ActionWithStorage.GET;
import static student.bazhin.helper.Constants.NODE_ID_MAPKEY;
import static student.bazhin.helper.PocketHeaders.*;

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

        boolean result = false;
        if ((clientSocket.isConnected()) && (haveConnection())) {
            PocketData pocketData = new PocketData(INIT);
            try {
                String nodeId = Core.getInstance().getNodeId();
                HashMap<String,String> metaData = new HashMap<>();
                metaData.put(NODE_ID_MAPKEY,nodeId);
                pocketData.setMetaData(metaData);
                result = handleInitResponse(sendDataToServer(pocketData));
            } catch (NullPointerException e) { //если не получилось взять ключ узла
                result = handleInitResponse(sendDataToServer(pocketData));
            }
        }
        return result;
    }

    protected boolean haveConnection() {
        PocketData response = sendDataToServer(new PocketData(TEST));
        return (response != null) && (response.getHeader() == OK);
    }

    protected boolean handleInitResponse(PocketData initResponse) {
        boolean result = false;
        switch (initResponse.getHeader()) {
            case OK: {
                String nodeId = initResponse.getMetaData().get(NODE_ID_MAPKEY);
                if (!nodeId.equals("")) {
                    Core.getInstance().setNodeId(nodeId);
                }
                result = true;
                break;
            }
            case FAIL: {
                closeConnection();
                result = false;
                break;
            }
        }
        return result;
    }

    protected void startSending() {
        new Thread(() -> {
            while (haveConnection()) {
                try {
                    Core.getInstance().getStorage().actionWithStorage(CALLBACK, () -> {
                        Vector<AScadaProject> scadaProjectsStorage;
                        scadaProjectsStorage = Core.getInstance().getStorage().actionWithStorage(GET,null);
                        for (AScadaProject scadaProject : scadaProjectsStorage) {
                            PocketData data = (PocketData)scadaProject.perform();
                            if (data != null) {
                                PocketData response = sendDataToServer(data);
                                if (response != null) {
                                    //todo обработка ответа сервера
                                } else {
                                    break;
                                }
                            }
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
